package org.example.ecommerce.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.payload.ProductDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Size(min = 3, message = "Product name must have atleast 3 characters.")
    private String name;
    @Size(min = 6, message = "Product description must have atleast 3 characters.")
    private String description;
    @DecimalMin(value = "1.0", message = "Price must be greater than 0.")
    private double price;
    private double specialPrice;
    private int quantity;
    private String image;
    private double discount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    public void update(ProductDTO product) {
        this.name = product.getName() == null ? this.name : product.getName();
        this.description = product.getDescription() == null ? this.description : product.getDescription();
        this.price = product.getPrice() == 0 ? this.price : product.getPrice();
        this.quantity = product.getQuantity() == 0 ? this.quantity : product.getQuantity();
        this.image = product.getImage() == null ? this.image : product.getImage();
        this.discount = product.getDiscount() == 0 ? this.discount : product.getDiscount();
        this.category = product.getCategory() == null ? this.category : product.getCategory();

        this.specialPrice = computeSpecialPrice(price, discount);
    }

    static public double computeSpecialPrice(double price, double discount) {
       return (price - (discount * 0.01) * price);
    }
}
