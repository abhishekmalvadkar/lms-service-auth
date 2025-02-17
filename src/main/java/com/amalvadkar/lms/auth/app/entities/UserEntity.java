package com.amalvadkar.lms.auth.app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name="users")
@Getter
@Setter
public class UserEntity extends BaseEntity {

    public static final String SPACE = " ";
    @Column(name="first_name",nullable = false)
    private String firstName;

    @Column(name="last_name",nullable = false)
    private String lastName;

    @Column(name="email",nullable = false)
    private String email;

    @Column(name = "last_login_time",nullable = false)
    private Instant lastLoginTime;

    @Column(name = "verification_token")
    private String verificationToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id",nullable = false)
     private RoleEntity role;

    public String fullName(){
        return this.firstName + SPACE + this.lastName;
    }


}
