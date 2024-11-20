package com.group.pet_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetService {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    double price;
    boolean disabled;
    String description;
    Timestamp createAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "petService", orphanRemoval = true)
    Set<ServiceImage> images = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "petService", orphanRemoval = true)
    Set<ServiceItem> items = new HashSet<>();
}
