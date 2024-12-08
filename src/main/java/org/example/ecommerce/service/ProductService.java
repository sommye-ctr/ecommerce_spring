package org.example.ecommerce.service;

import org.example.ecommerce.models.Product;
import org.example.ecommerce.payload.ProductDTO;
import org.example.ecommerce.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductDTO updateProduct(Long productId,ProductDTO productDTO);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

    ProductResponse getAllProducts(int page, int limit, String sortBy, String sortOrder);

    ProductResponse getAllProductsByCategory(Long categoryId, int page, int limit, String sortBy, String sortOrder);

    ProductResponse getAllProductsByKeyword(String keyword, int page, int limit, String sortBy, String sortOrder);
}
