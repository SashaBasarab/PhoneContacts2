package com.example.phonecontacts2.repository;

import com.example.phonecontacts2.entity.Role;
import com.example.phonecontacts2.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(UserRole name);

}
