package org.example.ecommerce.service;

import jakarta.transaction.Transactional;
import org.example.ecommerce.exceptions.APIException;
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
import org.springframework.stereotype.Service;

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
            throw new APIException("Product is out of stock");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("Quantity of request exceeds that of the product stock");
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

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        return carts.stream()
                .map(
                        this::getDTOFromCartUpdatedQnt
                ).toList();
    }

    @Override
    public CartDTO getCartByUser() {
        long userId = authUtils.loggedInUser().getId();
        Cart cart = cartRepository.findByUserId(userId);

        return getDTOFromCartUpdatedQnt(cart);
    }

    @Transactional
    @Override
    public CartDTO updateProduct(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        Cart cart = cartRepository.findByUserId(authUtils.loggedInUser().getId());
        if (cart == null){
            throw new ResourceNotFoundException("Cart of user cannot be found!", "userId", authUtils.loggedInUser().getId());
        }

        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
        if (cartItem == null) {
            throw new ResourceNotFoundException("Cart item", cart.getId());
        }

        if (quantity <= 0){
            throw new APIException("Quantity cannot be zero or negative");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Quantity of request exceeds that of the product stock");
        }

        double valueToSub = cartItem.getProductPrice() * cartItem.getQuantity();
        cartItem.setQuantity(quantity);
        cartItem.setProduct(product);
        cartItem.setDiscount(product.getDiscount());
        cartItem.setProductPrice(product.getSpecialPrice());
        CartItem saved = cartItemRepository.save(cartItem);

        double valueToAdd = saved.getProductPrice() * saved.getQuantity();
        cart.setTotalPrice(cart.getTotalPrice() + valueToAdd - valueToSub);
        Cart savedCart = cartRepository.save(cart);

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(savedCart.getId());
        cartDTO.setTotalPrice(cart.getTotalPrice());

        List<Product> p = cart.getCartItems().stream().map(CartItem::getProduct).toList();
        List<ProductDTO> productDTOList = p.stream().map(c -> modelMapper.map(c, ProductDTO.class)).toList();

        productDTOList.forEach(k -> k.setQuantity(quantity));
        cartDTO.setProducts(productDTOList);
        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductInCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findByUserId(cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart of user cannot be found!", "userId", authUtils.loggedInUser().getId());
        }

        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cartId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("Cart item", cartId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - cartItem.getProductPrice() * cartItem.getQuantity());

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);
        cartRepository.save(cart);
        return "Product deleted successfully!";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cartId);

        if (cartItem == null) {
            throw new APIException("Product " + product.getName() + " not available in the cart!!!");
        }

        double cartPrice = cart.getTotalPrice()
                - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice
                + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.save(cartItem);
    }

    private CartDTO getDTOFromCartUpdatedQnt(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getId());
        cartDTO.setTotalPrice(cart.getTotalPrice());

        List<ProductDTO> productDTOList = cart.getCartItems()
                .stream()
                .map(
                        c -> {
                            ProductDTO productDTO = modelMapper.map(c.getProduct(), ProductDTO.class);
                            productDTO.setQuantity(c.getQuantity());
                            return productDTO;
                        }
                ).toList();
        cartDTO.setProducts(productDTOList);
        return cartDTO;
    }

    private CartDTO getDTOFromCart(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getId());
        cartDTO.setTotalPrice(cart.getTotalPrice());
        List<Product> p = cart.getCartItems().stream().map(CartItem::getProduct).toList();
        List<ProductDTO> productDTOList = p.stream().map(c -> modelMapper.map(c, ProductDTO.class)).toList();
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
