package com.group.pet_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    Long weight;
    Integer height;
    String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", orphanRemoval = true)
    Set<PetImage> images = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", orphanRemoval = true)
    Set<Receipt> receipts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id")
    @JsonBackReference
    Species species;
}
