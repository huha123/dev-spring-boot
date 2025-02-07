package dev.huha123.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.huha123.app.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
