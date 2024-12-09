package org.example.ecommerce.service;

import org.example.ecommerce.exceptions.AlreadyExistsException;
import org.example.ecommerce.exceptions.ResourceNotFoundException;
import org.example.ecommerce.models.Cart;
import org.example.ecommerce.models.CartItem;
import org.example.ecommerce.models.Product;
import org.example.ecommerce.payload.CartDTO;
import org.example.ecommerce.payload.ProductDTO;
import org.example.ecommerce.repositories.CartItemRepository;
import org.example.ecommerce.repositories.CartRepository;
import org.example.ecommerce.repositories.ProductRepository;
import org.example.ecommerce.utils.AuthUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AuthUtils authUtils;

    @Autowired
    public CartServiceImpl(ModelMapper modelMapper, ProductRepository productRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, AuthUtils authUtils) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.authUtils = authUtils;
    }

    @Override
    public CartDTO addCart(long productId, int quantity) {
        Cart cart = getCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
        if (cartItem != null) {
            throw new AlreadyExistsException(String.format("Cart item with product id %d already exists", productId),
                    "Cart item",
                    String.valueOf(productId));
        }
        if (product.getQuantity() == 0) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Product is out of stock");
        }
        if (product.getQuantity() < quantity) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Quantity of request exceeds that of the product stock");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setProductPrice(product.getSpecialPrice());
        newCartItem.setDiscount(product.getDiscount());
        CartItem saved = cartItemRepository.save(newCartItem);

        cart.setTotalPrice(cart.getTotalPrice() + product.getSpecialPrice() * quantity);
        cart.addCartItem(saved);
        cartRepository.save(cart);

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getId());
        cartDTO.setTotalPrice(cart.getTotalPrice());

        List<ProductDTO> productDTOList = cart.getCartItems()
                .stream()
                .map(
                        c -> {
                            ProductDTO productDTO = modelMapper.map(c.getProduct(), ProductDTO.class);
                            productDTO.setQuantity(quantity);
                            return productDTO;
                        }
                ).toList();
        cartDTO.setProducts(productDTOList);

        return cartDTO;
    }

    private Cart getCart() {
        Cart cart = cartRepository.findByUserId(authUtils.loggedInUser().getId());
        if (cart != null) {
            return cart;
        }

        Cart temp = new Cart();
        temp.setUser(authUtils.loggedInUser());
        return cartRepository.save(temp);
    }
}
