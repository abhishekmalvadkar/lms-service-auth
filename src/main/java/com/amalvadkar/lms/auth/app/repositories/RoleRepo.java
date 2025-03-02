package com.amalvadkar.lms.auth.app.repositories;

import com.amalvadkar.lms.auth.app.entities.RoleEntity;
import com.amalvadkar.lms.auth.app.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.amalvadkar.lms.auth.app.constants.AppConstants.ROLE_NOT_FOUND_ERR_MSG;

public interface RoleRepo extends JpaRepository<RoleEntity, String> {

    @Query("""
        select r from RoleEntity r
        where r.code = :code
        and r.deleteFlag = false
        and r.active = true
        """)
    Optional<RoleEntity> findActiveRoleByCode(@Param("code") String code);

    default RoleEntity fetchRoleByCode(String roleCode) {
        return findActiveRoleByCode(roleCode)
                .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND_ERR_MSG));
    }
}
