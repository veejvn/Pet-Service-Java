package com.group.e_commerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group.e_commerce.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Status status;
    int quantity;
    double totalCost;
    Timestamp start;
    Timestamp end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    @JsonBackReference
    Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id")
    @JsonBackReference
    Receipt receipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    @JsonBackReference
    User staff;
}
