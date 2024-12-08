package org.example.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.ecommerce.config.Constants;
import org.example.ecommerce.payload.CategoryDTO;
import org.example.ecommerce.payload.CategoryResponse;
import org.example.ecommerce.service.CategoryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class CategoryController {

    CategoryServiceImpl categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
                                                             @RequestParam(name = "limit", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit,
                                                             @RequestParam(name = "sortBy", defaultValue = Constants.DEFAULT_SORT_CATEGORY_BY) String sortBy,
                                                             @RequestParam(name = "sortOrder", defaultValue = Constants.DEFAULT_SORT_ORDER) String sortOrder) {
        return ResponseEntity.ok(categoryService.getAllCategories(page, limit, sortBy, sortOrder));
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.addCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoryDTO);
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable long id) {
        return new ResponseEntity<>(categoryService.deleteCategory(id), HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO resp = categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

}
