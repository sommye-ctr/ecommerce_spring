package org.example.ecommerce.service;

import org.example.ecommerce.payload.CategoryDTO;
import org.example.ecommerce.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories(int page, int limit, String sortBy, String sortOrder);
    CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(long id);
    CategoryDTO updateCategory(long id, CategoryDTO categoryDTO);
}
