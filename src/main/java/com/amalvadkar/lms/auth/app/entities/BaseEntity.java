package com.amalvadkar.lms.auth.app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="created_by",nullable = false)
    private UserEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="updated_by",nullable = false)
    private UserEntity updatedBy;

    @Column(name="created_on",nullable = false)
    private Instant createdOn;

    @Column(name="updated_on",nullable = false)
    private Instant updatedOn;

    @Column(name="delete_flag",nullable = false)
    private Boolean deleteFlag;

    @Column(name="active",nullable = false)
    private Boolean active;

    @PrePersist
    public void beforePersist(){
         this.id= UUID.randomUUID().toString();
        this.deleteFlag = Boolean.FALSE;
        this.createdOn = Instant.now();
        this.updatedOn =createdOn;
        this.active = Boolean.TRUE;

    }

}
