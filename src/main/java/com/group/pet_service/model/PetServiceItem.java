package com.group.pet_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group.pet_service.enums.PetServiceItemStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetServiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    LocalDateTime start;
    LocalDateTime end;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    PetServiceItemStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    @JsonBackReference
    PetService petService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id")
    @JsonBackReference
    Receipt receipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    @JsonBackReference
    User staff;

    @PrePersist
    protected void onCreate() {
        this.status = PetServiceItemStatus.PENDING;
    }
}
