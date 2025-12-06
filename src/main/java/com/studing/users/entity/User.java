package com.studing.users.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "inserted_date_at_utc", nullable = false, updatable = false)
    private LocalDateTime insertedDateAtUtc;

    @Column(name = "updated_date_at_utc")
    private LocalDateTime updatedDateAtUtc;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserRole> userRoles = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        insertedDateAtUtc = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDateAtUtc = LocalDateTime.now();
    }

}