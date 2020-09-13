package com.jjdev.photoapi.repository;

import com.jjdev.photoapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    public Role findRoleByName(String name);
}