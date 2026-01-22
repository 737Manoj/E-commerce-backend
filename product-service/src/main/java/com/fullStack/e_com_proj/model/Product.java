package com.fullStack.e_com_proj.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("category")
    private String category;

    @JsonProperty("releaseDate")
    private Date releaseDate;

    @JsonProperty("productAvailable")
    @Column(nullable = false)
    private Boolean productAvailable;

    @JsonProperty("stockQuantity")
    private Integer stockQuantity;

    @JsonProperty("imageName")
    private String imageName;

    @JsonProperty("imageType")
    private String imageType;

    @Lob
    @JsonProperty("imageData")
    private byte[] imageData;

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageType() {
        return imageType;
    }

    public byte[] getImageData() {
        return imageData;
    }
}
