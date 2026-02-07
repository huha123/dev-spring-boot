package dev.huha123.app.service;

import dev.huha123.app.dto.RoleDto;
import dev.huha123.app.entity.RoleEntity;
import dev.huha123.app.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public RoleDto createRole(RoleDto roleDto) {
        RoleEntity parent = null;
        if (roleDto.parentId() != null) {
            parent = roleRepository.findById(roleDto.parentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent role not found"));
        }

        RoleEntity role = RoleEntity.builder()
                .roleName(roleDto.name())
                .parent(parent)
                .build();

        return RoleDto.fromEntity(roleRepository.save(role));
    }

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(RoleDto::fromEntity)
                .collect(Collectors.toList());
    }

    public RoleDto getRoleById(Long id) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        return RoleDto.fromEntity(role);
    }

    @Transactional
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        if (roleDto.name() != null) {
            role.setRoleName(roleDto.name());
        }

        RoleEntity parent = null;
        if (roleDto.parentId() != null) {
            parent = roleRepository.findById(roleDto.parentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent role not found"));
        }
        role.setParent(parent);

        return RoleDto.fromEntity(role);
    }

    @Transactional
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public List<RoleDto> getRoleHierarchy() {
        // 루트 역할(부모가 null)들을 찾아서 계층 구조 구성
        return roleRepository.findAll().stream()
                .filter(role -> role.getParent() == null)
                .map(this::buildHierarchy)
                .collect(Collectors.toList());
    }

    private RoleDto buildHierarchy(RoleEntity roleEntity) {
        RoleDto dto = RoleDto.fromEntity(roleEntity);

        // 자식 역할들을 재귀적으로 구성
        List<RoleDto> children = roleRepository.findAll().stream()
                .filter(role -> role.getParent() != null && role.getParent().getId().equals(roleEntity.getId()))
                .map(this::buildHierarchy)
                .collect(Collectors.toList());

        dto = dto.withChildren(children);
        return dto;
    }
}