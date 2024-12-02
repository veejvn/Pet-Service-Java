package com.group.pet_service.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    Integer price;
    Boolean disabled;
    String description;
    Timestamp createAt;
    String image;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "petService", orphanRemoval = true)
    Set<PetServiceItem> items = new HashSet<>();

    @Override
    public String toString() {

        return "Service [id=" + id + ", name=" + name + ", price=" + price + ", disabled=" + disabled + ", description="
                + description + ", createAt=" + createAt + ", images=" + image + ", items=" + items + "]";
    }

    @PrePersist
    protected void onCreate() {
        this.disabled = false;
        this.createAt = Timestamp.from(Instant.now());
    }
}
