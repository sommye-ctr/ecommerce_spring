package org.example.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.models.Category;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;

    private String name;
    private String description;
    private double price;
    private double specialPrice;
    private int quantity;
    private String image;
    private double discount;

    private Category category;
}
