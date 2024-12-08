package org.example.ecommerce.service;

import lombok.AllArgsConstructor;
import org.example.ecommerce.exceptions.AlreadyExistsException;
import org.example.ecommerce.exceptions.ResourceNotFoundException;
import org.example.ecommerce.models.Category;
import org.example.ecommerce.payload.CategoryDTO;
import org.example.ecommerce.payload.CategoryResponse;
import org.example.ecommerce.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @Override
    public CategoryResponse getAllCategories(int page, int limit, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Category> page1 = categoryRepository.findAll(pageable);

        List<Category> categories = page1.getContent();
        List<CategoryDTO> categoryDTOS = categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();

        return new CategoryResponse(categoryDTOS, page1.getNumber(), page1.getSize(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category1 = categoryRepository.findByName(categoryDTO.getName());
        if (category1 != null) {
            throw new AlreadyExistsException("Category", category1.getName());
        }
        Category savedCategory = categoryRepository.save(modelMapper.map(categoryDTO, Category.class));
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            categoryRepository.deleteById(id);
            return modelMapper.map(category.get(), CategoryDTO.class);
        }
        throw new ResourceNotFoundException("Category", id);
    }

    @Override
    public CategoryDTO updateCategory(long id, CategoryDTO newCategoryDTO) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category", id);
        }
        category.get().setName(newCategoryDTO.getName());
        Category savedCategory = categoryRepository.save(category.get());
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
