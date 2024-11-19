package com.group.pet_service.service;

import com.group.pet_service.dto.request.*;
import com.group.pet_service.dto.response.AuthResponse;
import com.group.pet_service.dto.response.IntrospectResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.enums.Role;
import com.group.pet_service.model.Token;
import com.group.pet_service.model.User;
import com.group.pet_service.repository.TokenRepository;
import com.group.pet_service.repository.UserRepository;
import com.group.pet_service.util.PasswordUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    TokenRepository tokenRepository;
    PasswordUtil passwordUtil;

    @NonFinal
    @Value("${app.jwt.signerKey}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${app.jwt.valid-duration}")
    private long VALID_DURATION;

    @NonFinal
    @Value("${app.jwt.refresh-duration}")
    private long REFRESH_DURATION;

    public void register(AuthRequest request){
        boolean existedUser = userRepository.existsByEmail(request.getEmail());
        if(existedUser){
            throw new AppException(HttpStatus.BAD_REQUEST, "Email has existed", "auth-e-01");
        }
    }

    public AuthResponse verifyRegister(AuthRequest request){
        register(request);

        String hashedPassword = passwordUtil.encodePassword(request.getPassword());
        request.setPassword(hashedPassword);

        Set<Role> roles = new HashSet<>();

        roles.add(Role.USER);

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        user.setRoles(roles);
        userRepository.save(user);

        var token = generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse login(AuthLoginRequest request){
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Email User not found", "auth-e-02"));

        if(!user.getRoles().contains(request.getRole())){
            throw new AppException(HttpStatus.FORBIDDEN, "Insufficient permissions", "auth-e-03");
        }

        boolean isMatchPassword = passwordUtil.checkPassword(request.getPassword(), user.getPassword());

        if(!isMatchPassword)
            throw new AppException(HttpStatus.BAD_REQUEST, "Wrong password", "auth-e-03");

        String token = generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("pet-service.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw  new AppException(HttpStatus.UNAUTHORIZED,"JWT error" ,"jwt-e-01");
        }
    }

    private  String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.toString());
                });
        return  stringJoiner.toString();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e){
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signedToken = verifyToken(request.getToken(), true);

            String jit = signedToken.getJWTClaimsSet().getJWTID();
            log.info(jit);
            Token invalidateToken = Token.builder()
                    .id(jit)
                    .build();

            tokenRepository.save(invalidateToken);
        }catch (AppException e) {
            log.info("Token already expired");
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
            ? new Date(signedJWT
                .getJWTClaimsSet()
                .getExpirationTime()
                .toInstant()
                .plus(REFRESH_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
        : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if(!(verified && expiryTime.after(new Date())))
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid JWT signature or Token has expired","jwt-e-02");

        if(tokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(HttpStatus.BAD_REQUEST, "Token existed", "jwt-e-03");
        }

        return signedJWT;
    }

    public AuthResponse refreshToken(RefreshRequest request)
            throws ParseException, JOSEException {
        var signJWT = verifyToken(request.getToken(), true);

        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        Token invalidateToken = Token.builder()
                .id(jit)
                .build();

        tokenRepository.save(invalidateToken);

        var username = signJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Username not found", "jwt-e-04"));

        var token = generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
