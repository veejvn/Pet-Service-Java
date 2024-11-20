package com.group.pet_service.util.jwt;

import com.group.pet_service.dto.jwt.JWTPayloadDto;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.model.User;
import com.group.pet_service.util.UserUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Component
@RequiredArgsConstructor
public class AccessTokenUtil {
    @Value("${app.jwt.access.secret}")
    private String SECRET_KEY;

    @Value("${app.jwt.access.duration}")
    private long ACCESS_DURATION;

    public String generateToken(JWTPayloadDto jwtPayloadDto){
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(jwtPayloadDto.getId())
                    .issuer("pet-service.com")
                    .issueTime(new Date())
                    .expirationTime(new Date(
                            Instant.now().plus(ACCESS_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                    ))
                    .claim("id", jwtPayloadDto.getId())
                    .claim("email", jwtPayloadDto.getEmail())
                    .claim("scope", jwtPayloadDto.getScope())
                    .build();
            Payload payload = new Payload(jwtClaimsSet.toJSONObject());
            JWSObject jwsObject = new JWSObject(header, payload);
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw  new AppException(HttpStatus.UNAUTHORIZED,"JWT error" ,"jwt-e-01");
        }
    }



    private JWTPayloadDto verifyToken(String token){
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

            if (!signedJWT.verify(verifier)) {
                throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid JWT signature", "jwt-e-2");
            }

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            Date expirationTime = claimsSet.getExpirationTime();

            if (new Date().after(expirationTime)) {
                throw new AppException(HttpStatus.UNAUTHORIZED, "Token has expired", "jwt-e-03");
            }

            return JWTPayloadDto.builder()
                    .id(claimsSet.getStringClaim("id"))
                    .email(claimsSet.getStringClaim("email"))
                    .scope(claimsSet.getStringClaim("scope"))
                    .build();
        }catch (ParseException | JOSEException e){
            throw new AppException(HttpStatus.BAD_REQUEST,  "Failed to verify JWT token", "jwt-e-04");
        }
    }
}
