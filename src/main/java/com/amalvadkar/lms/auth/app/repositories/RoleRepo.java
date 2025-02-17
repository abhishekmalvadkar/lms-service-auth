package com.amalvadkar.lms.auth.app.repositories;

import com.amalvadkar.lms.auth.app.entities.RoleEntity;
import com.amalvadkar.lms.auth.app.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<RoleEntity,String> {

    Optional<RoleEntity> findByCodeAndDeleteFlagFalse(String code);

    default RoleEntity fetchRoleBasedOnCode(){
       return findByCodeAndDeleteFlagFalse("CUSTOMER").orElseThrow(()-> new ResourceNotFoundException("Role not found"));
    }
}
