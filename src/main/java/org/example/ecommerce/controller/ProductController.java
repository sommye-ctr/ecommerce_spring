package org.example.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.ecommerce.config.Constants;
import org.example.ecommerce.payload.ProductDTO;
import org.example.ecommerce.payload.ProductResponse;
import org.example.ecommerce.service.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ProductController {
    final ProductServiceImpl productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @PathVariable Long categoryId, @RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.addProduct(categoryId, productDTO), HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(name = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
                                                          @RequestParam(name = "limit", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit,
                                                          @RequestParam(name = "sortBy", defaultValue = Constants.DEFAULT_SORT_PRODUCT_BY) String sortBy,
                                                          @RequestParam(name = "sortOrder", defaultValue = Constants.DEFAULT_SORT_ORDER) String sortOrder) {
        return ResponseEntity.ok(productService.getAllProducts(page, limit, sortBy, sortOrder));
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getAllProductsByCategory(@RequestParam(name = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
                                                                    @RequestParam(name = "limit", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit,
                                                                    @RequestParam(name = "sortBy", defaultValue = Constants.DEFAULT_SORT_PRODUCT_BY) String sortBy,
                                                                    @RequestParam(name = "sortOrder", defaultValue = Constants.DEFAULT_SORT_ORDER) String sortOrder,
                                                                    @PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getAllProductsByCategory(categoryId, page, limit, sortBy, sortOrder));
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(productId, productDTO));
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getAllProductsByKeyword(@RequestParam(name = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
                                                                   @RequestParam(name = "limit", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit,
                                                                   @RequestParam(name = "sortBy", defaultValue = Constants.DEFAULT_SORT_PRODUCT_BY) String sortBy,
                                                                   @RequestParam(name = "sortOrder", defaultValue = Constants.DEFAULT_SORT_ORDER) String sortOrder,
                                                                   @PathVariable String keyword) {
        return new ResponseEntity<>(productService.getAllProductsByKeyword(keyword, page, limit, sortBy, sortOrder), HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam(name = "image") MultipartFile image) throws IOException {
        return ResponseEntity.ok(productService.updateProductImage(productId, image));
    }

}
