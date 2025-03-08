package com.amalvadkar.lms.auth.app.entities;

import com.amalvadkar.lms.auth.app.enums.UserStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity extends BaseEntity {

    public static final String SPACE = " ";
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "last_login_time")
    private Instant lastLoginTime;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "status" , nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_expire_time")
    private Instant otpExpiryTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    public String fullName() {
        return this.firstName + SPACE + this.lastName;
    }

    @PrePersist
    public void prePersistUser() {
        this.verificationToken = UUID.randomUUID().toString();
        this.status = UserStatusEnum.LOCKED;
    }

    public boolean isAccountLocked() {
        return UserStatusEnum.LOCKED == this.status;
    }
}
