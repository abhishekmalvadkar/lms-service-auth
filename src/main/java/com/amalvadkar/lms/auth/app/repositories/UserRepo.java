package com.amalvadkar.lms.auth.app.repositories;

import com.amalvadkar.lms.auth.app.entities.UserEntity;
import com.amalvadkar.lms.auth.app.exception.EmailAlreadyExistException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity,String> {

    String EMAIL_ALREADY_EXIST_ERR_MSG = "Email Already Exist";

    boolean existsByEmail(String email);

    @Query("select u from UserEntity u where u.email = :email and u.verificationToken = :token and u.deleteFlag = false")
    Optional<UserEntity> findByEmailAndToken(@Param("email") String email,
                                             @Param("token") String token);

    default void throwIfEmailExists(String email) {
        if (existsByEmail(email)) {
            throw new EmailAlreadyExistException(EMAIL_ALREADY_EXIST_ERR_MSG);
        }
    }

}
