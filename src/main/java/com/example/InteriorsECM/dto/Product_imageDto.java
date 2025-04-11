package com.example.InteriorsECM.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product_imageDto {
    @NotNull
    int image_id;
    @NotEmpty
    String image_url;
    boolean is_primary;
    @NotEmpty
    int product_id;
}
