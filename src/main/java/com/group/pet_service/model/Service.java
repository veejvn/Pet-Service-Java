package com.group.pet_service.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    double price;
    boolean disabled;
    String description;
    Timestamp createAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "service", orphanRemoval = true)
    Set<ServiceImage> images = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "service", orphanRemoval = true)
    Set<ServiceItem> items = new HashSet<>();
	@Override
	public String toString() {
		
		return "Service [id=" + id + ", name=" + name + ", price=" + price + ", disabled=" + disabled + ", description="
				+ description + ", createAt=" + createAt + ", images=" + images + ", items=" + items + "]";
	}
    
    
}
