package org.example.ecommerce.repositories;

import org.example.ecommerce.models.Category;
import org.example.ecommerce.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByNameLikeIgnoreCase(String keyword);

    Page<Product> findByCategory(Category category, Pageable pageable);

    Page<Product> findByNameLikeIgnoreCase(String s, Pageable pageable);
}
