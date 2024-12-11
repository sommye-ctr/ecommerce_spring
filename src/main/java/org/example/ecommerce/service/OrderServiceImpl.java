package org.example.ecommerce.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.ecommerce.exceptions.APIException;
import org.example.ecommerce.exceptions.ResourceNotFoundException;
import org.example.ecommerce.models.*;
import org.example.ecommerce.payload.OrderDTO;
import org.example.ecommerce.payload.OrderItemDTO;
import org.example.ecommerce.payload.OrderRequestDTO;
import org.example.ecommerce.repositories.*;
import org.example.ecommerce.utils.AuthUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final AuthUtils authUtils;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;
    private final PaymentRepository paymentRepository;

    @Override
    public OrderDTO orderProducts(OrderRequestDTO orderRequestDTO) {
        User user = authUtils.loggedInUser();
        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
            throw new ResourceNotFoundException("Cart of user not found", "User", user.getId());
        }

        Address address = addressRepository.findById(orderRequestDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", orderRequestDTO.getAddressId()));

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setEmail(user.getEmail());
        order.setAddress(address);
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("accepted");

        Payment payment = new Payment(orderRequestDTO.getPaymentMethod(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage(),
                orderRequestDTO.getPgName());
        payment.setOrder(order);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);
        order = orderRepository.save(order);

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null || cartItems.isEmpty()){
            throw new APIException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);

        cartItems.forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();

            // Reduce stock quantity
            product.setQuantity(product.getQuantity() - quantity);

            // Save product back to the database
            productRepository.save(product);

            // Remove items from cart
            cartService.deleteProductInCart(cart.getId(), item.getProduct().getId());
        });

        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));
        orderDTO.setAddressId(orderRequestDTO.getAddressId());
        return orderDTO;
    }
}
