package com.fullStack.e_com_proj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {

    private Integer id;

    private String name;

    private String description;

    private String brand;

    private BigDecimal price;

    private String category;

    private Date releaseDate;

    private Boolean productAvailable;

    private Integer stockQuantity;
}
