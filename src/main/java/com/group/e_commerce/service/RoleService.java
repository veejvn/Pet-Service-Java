package com.group.e_commerce.service;

import com.group.e_commerce.dto.request.RoleRequest;
import com.group.e_commerce.dto.response.RoleResponse;
import com.group.e_commerce.mapper.RoleMapper;
import com.group.e_commerce.model.Role;
import com.group.e_commerce.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAll(){
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String id){
        roleRepository.deleteById(id);
    }
}
