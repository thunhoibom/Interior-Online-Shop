package com.example.InteriorsECM.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product_imageDto {
    @NotNull
    int image_id;
    @NotEmpty
    @Size(max = 255, message = "Image URL must not exceed 255 characters")
    String image_url;
    boolean is_primary;
    @NotEmpty
    int product_id;
}
