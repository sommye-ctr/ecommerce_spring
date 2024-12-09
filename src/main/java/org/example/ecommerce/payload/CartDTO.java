package org.example.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDTO {
    private Long cartId;
    private Double totalPrice;
    private List<ProductDTO> products = new ArrayList<>();
}