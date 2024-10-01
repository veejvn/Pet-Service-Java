package com.group.pet_service.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    int totalItem;
    double totalCost;
    Timestamp createdAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receipt", orphanRemoval = true)
    Set<ServiceItem> items = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    @JsonBackReference
    Pet pet;
}
