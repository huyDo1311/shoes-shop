package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.entity.Order;
import com.cybersoft.shop.entity.OrderVariant;
import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.entity.Variant;
import com.cybersoft.shop.enums.OrderStatus;
import com.cybersoft.shop.repository.*;
import com.cybersoft.shop.response.order.OrderItemResponse;
import com.cybersoft.shop.response.order.OrderResponse;
import com.cybersoft.shop.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderVariantRepository orderVariantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VariantRepository variantRepository;
    @Autowired
    private ProductRepository productRepository;


    @Transactional
    @Override
    public OrderResponse addToCart(String email, String sku, int quantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found: " + email));

        Variant variant = variantRepository.findById(sku)
                .orElseThrow(()-> new RuntimeException("Variant not found by SKU: " + sku));

        float unitPrice = variant.getProduct().getPrice();

        Order order = orderRepository.findByUserAndStatus(user, OrderStatus.Pending)
                .orElseGet(()->{
                    Order order1 = Order.builder()
                            .user(user)
                            .status(OrderStatus.Pending)
                            .items(new ArrayList<>())
                            .build();
                    return orderRepository.save(order1);
                });
        if (order.getItems() == null) order.setItems(new ArrayList<>());
        order.addOrIncreaseItem(sku, unitPrice, quantity);
        order.recalcTotal();

        Order saved = orderRepository.save(order);

        return toRes(saved);
    }

    @Override
    public OrderResponse getCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found: " + email));
        Order cart = orderRepository.findByUserAndStatus(user, OrderStatus.Pending)
                .orElseGet(()->Order.builder()
                        .user(user)
                        .status(OrderStatus.Pending)
                        .build());
        return toRes(cart);
    }

    @Override
    public OrderResponse getById(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Order not found: " + id));
        return toRes(order);
    }

    @Override
    public OrderResponse updateStatus(int id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Order not found: " + id));
        OrderStatus newStatus = OrderStatus.valueOf(String.valueOf(status));
        order.setStatus(newStatus);
        order.recalcTotal();
        return toRes(orderRepository.save(order));
    }

    @Override
    public void delete(int id) {
        orderRepository.deleteById(id);

    }

    @Override
    @Transactional
    public OrderResponse updateCartItemQuantity(String email, String sku, int quantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found" + email));

        Order cart = orderRepository.findByUserAndStatus(user, OrderStatus.Pending)
                .orElseThrow(()-> new RuntimeException("Cart not found for user"));

        if(cart.getItems() == null)
            cart.setItems(new ArrayList<>());
        OrderVariant item = cart.getItems().stream()
                .filter(i -> i.getVariantSku().equals(sku))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("item not found in cart " +sku));
        if(quantity <= 0){
            cart.getItems().remove(item);
        }else {
            item.setQuantity(quantity);
        }
        cart.recalcTotal();

        return toRes(orderRepository.save(cart));
    }

    @Override
    @Transactional
    public OrderResponse removeCartItem(String email, String sku) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Order cart = orderRepository.findByUserAndStatus(user, OrderStatus.Pending)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + email));

        if (cart.getItems() == null) cart.setItems(new ArrayList<>());

        OrderVariant item = cart.getItems().stream()
                .filter(i -> i.getVariantSku().equals(sku))
                .findFirst()
                .orElseThrow(()  -> new RuntimeException("Item not found in cart :" +sku));

        cart.getItems().remove(item);

        cart.recalcTotal();
        return toRes(orderRepository.save(cart));
    }

    @Override
    public OrderResponse clearCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Order cart = orderRepository.findByUserAndStatus(user, OrderStatus.Pending)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + email));

        if (cart.getItems() != null) {
            cart.getItems().clear();
        }

        cart.setTotal(0f);

        return toRes(orderRepository.save(cart));
    }

    private OrderResponse toRes(Order o){
        var items = o.getItems() == null ? List.<OrderItemResponse>of() : mapItemsWithImages(o.getItems());

        return OrderResponse.builder()
                .id(o.getId())
                .userEmail(o.getUser() != null ? o.getUser().getEmail() : null)
                .status(o.getStatus() != null ? o.getStatus() : OrderStatus.Pending)
                .total(o.getTotal() == 0f ? 0f : o.getTotal())
                .items(items)
                .build();
    }

    private List<OrderItemResponse> mapItemsWithImages(List<OrderVariant> orderItem){

        var skus = orderItem.stream().map(OrderVariant::getVariantSku).toList();

        var variants = variantRepository.findBySkuIn(skus);

        var variantBySku = variants.stream().collect(Collectors.toMap(
                Variant::getSku, v -> v, (a,b) -> a
        ));

        return orderItem.stream().map(i ->{
            var v = variantBySku.get(i.getVariantSku());
            int productId = 0;
            String productname = null;
            String imageUrl = null;

            if(v != null && v.getProduct() != null){
                var p = v.getProduct();
                productId = p.getId();
                productname = p.getProductName();
                imageUrl = p.getThumbnail();
            }
            var lineTotal = i.getPrice()*i.getQuantity();

            return OrderItemResponse.builder()
                    .id(i.getId())
                    .sku(i.getVariantSku())
                    .productId(productId)
                    .productName(productname)
                    .imageUrl(imageUrl)
                    .quantity(i.getQuantity())
                    .price(i.getPrice())
                    .lineTotal(lineTotal)
                    .build();

        }).toList();
    }

}
