package org.example.ecommerce.service;

import org.example.ecommerce.exceptions.AlreadyExistsException;
import org.example.ecommerce.exceptions.ResourceNotFoundException;
import org.example.ecommerce.models.Category;
import org.example.ecommerce.models.Product;
import org.example.ecommerce.payload.ProductDTO;
import org.example.ecommerce.payload.ProductResponse;
import org.example.ecommerce.repositories.CategoryRepository;
import org.example.ecommerce.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileServiceImpl fileServiceImpl;

    @Value("${project.image}")
    private String path;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, FileServiceImpl fileServiceImpl) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileServiceImpl = fileServiceImpl;
    }

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Optional<Category> category = categoryRepository.findById(categoryId);

        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category", categoryId);
        }

        for (Product product : category.get().getProducts()) {
            if (product.getName().equals(productDTO.getName())) {
                throw new AlreadyExistsException("Product", productDTO.getName());
            }
        }

        Product product = modelMapper.map(productDTO, Product.class);
        product.setSpecialPrice(Product.computeSpecialPrice(product.getPrice(), product.getDiscount()));
        product.setImage("default.png");
        product.setCategory(category.get());

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new ResourceNotFoundException("Product", productId);
        }

        productOptional.get().update(productDTO);
        productRepository.save(productOptional.get());
        return modelMapper.map(productOptional.get(), ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new ResourceNotFoundException("Product", productId);
        }
        productRepository.deleteById(productId);
        return modelMapper.map(productOptional.get(), ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new ResourceNotFoundException("Product", productId);
        }

        String fileName = fileServiceImpl.uploadImage(path, image);
        productOptional.get().setImage(fileName);
        return modelMapper.map(productRepository.save(productOptional.get()), ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(int page, int limit, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Product> page1 = productRepository.findAll(pageable);

        return getProductResponse(pageable, page1);
    }

    @Override
    public ProductResponse getAllProductsByCategory(Long categoryId, int page, int limit, String sortBy, String sortOrder) {
        Optional<Category> category = categoryRepository.findById(categoryId);

        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category", categoryId);
        }

        Sort sort = sortOrder.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Product> page1 = productRepository.findByCategory(category.get(), pageable);

        return getProductResponse(pageable, page1);
    }

    @Override
    public ProductResponse getAllProductsByKeyword(String keyword, int page, int limit, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Product> page1 = productRepository.findByNameLikeIgnoreCase("%" + keyword + "%", pageable);

        return getProductResponse(pageable, page1);
    }

    private ProductResponse getProductResponse(Pageable pageable, Page<Product> page1) {
        List<Product> products = page1.getContent();
        List<ProductDTO> productDTOS = products.stream().map((o) -> modelMapper.map(o, ProductDTO.class)).toList();
        return new ProductResponse(productDTOS, pageable.getPageNumber(), pageable.getPageSize(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
    }
}
