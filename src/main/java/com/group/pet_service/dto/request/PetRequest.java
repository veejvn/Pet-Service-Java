package com.group.pet_service.dto.request;

import com.group.pet_service.model.PetImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetRequest {

    @NotBlank(message = "Pet name is required")
    String name;

    @NotNull(message = "Pet weight is required")
    Long weight;

    @NotNull(message = "Pet height is required")
    Integer height;

    String description;

    @NotEmpty(message = "At least one image path is required")
    Set<@NotBlank(message = "Image path cannot be blank") String> imageUrls;

    @NotBlank(message = "Pet species is required")
    String species_id;

}
