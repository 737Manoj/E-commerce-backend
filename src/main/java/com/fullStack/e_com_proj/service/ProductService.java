package com.fullStack.e_com_proj.service;

import com.fullStack.e_com_proj.dto.ProductResponseDto;
import com.fullStack.e_com_proj.model.Product;
import com.fullStack.e_com_proj.repository.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProductRepo repo;


    public List<ProductResponseDto> getAllProducts(){
        return repo.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public Product getProductById(int id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return repo.save(product);
    }

    @Transactional
    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return repo.save(product);
    }

    public void deleteProduct(int id) {
         repo.deleteById(id);
    }

    @Transactional
    public List<Product> searchProduct(String keyword) {
        return repo.searchProducts(keyword);
    }

    public ProductResponseDto toResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .brand(product.getBrand())
                .price(product.getPrice())
                .name(product.getName())
                .category(product.getCategory())
                .productAvailable(product.getProductAvailable())
                .description(product.getDescription())
                .releaseDate(product.getReleaseDate())
                .stockQuantity(product.getStockQuantity())
                .build();
    }
}
