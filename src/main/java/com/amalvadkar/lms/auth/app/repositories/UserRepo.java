package com.amalvadkar.lms.auth.app.repositories;

import com.amalvadkar.lms.auth.app.entities.UserEntity;
import com.amalvadkar.lms.auth.app.exception.ResourceAlreadyExistsException;
import com.amalvadkar.lms.auth.app.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, String> {

    boolean existsByEmail(String email);

    @Query("""
            select u from UserEntity u
            where u.email = :email
            and u.verificationToken = :token
            and u.deleteFlag = false""")
    Optional<UserEntity> findByEmailAndToken(@Param("email") String email,  @Param("token") String token);

    Optional<UserEntity> findByEmailAndDeleteFlagFalse(String email);

    Optional<UserEntity> findByOtpAndEmailAndDeleteFlagFalse(String otp, String email);

    default void throwIfEmailExists(String email) {
        if (existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email Already Exist");
        }
    }

    default UserEntity findUserOrThrow(String email) {
        return findByEmailAndDeleteFlagFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));
    }
}
