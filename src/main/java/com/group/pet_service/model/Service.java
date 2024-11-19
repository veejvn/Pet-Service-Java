package com.group.pet_service.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
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
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    Set<ServiceImage> images = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "service", orphanRemoval = true)
    Set<ServiceItem> items = new HashSet<>();
	@Override
	public String toString() {
		
		return "Service [id=" + id + ", name=" + name + ", price=" + price + ", disabled=" + disabled + ", description="
				+ description + ", createAt=" + createAt + ", images=" + images + ", items=" + items + "]";
	}
    
    @PrePersist
    protected void onCreate(){
        this.disabled = false;
    }
}
