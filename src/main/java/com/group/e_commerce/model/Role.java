package com.group.e_commerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group.e_commerce.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    String name;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
            @JsonIgnore
    Set<User> users = new HashSet<>();
}
