package com.group.e_commerce.model;

import com.group.e_commerce.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String firstname;
    String lastname;
    boolean gender;
    LocalDate dob;
    String username;
    String password;
    String phoneNum;
    String email;
    Timestamp createdAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    Set<UserImage> images = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    Set<Token> tokens = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user",orphanRemoval = true)
    Set<Receipt> receipts = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "staff",orphanRemoval = true)
    Set<ServiceItem> items = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }
}
