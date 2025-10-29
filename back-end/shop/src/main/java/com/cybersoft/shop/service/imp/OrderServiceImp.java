package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.entity.Order;
import com.cybersoft.shop.entity.OrderVariant;
import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.entity.Variant;
import com.cybersoft.shop.enums.OrderStatus;
import com.cybersoft.shop.repository.*;
import com.cybersoft.shop.request.CheckoutRequest;
import com.cybersoft.shop.request.OrderCancelRequest;
import com.cybersoft.shop.response.order.OrderItemResponse;
import com.cybersoft.shop.response.order.OrderResponse;
import com.cybersoft.shop.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        //check tồn kho
        int stock = variant.getQuantity();

        Order existingCart = orderRepository.findByUserAndStatus(user, OrderStatus.Pending).orElse(null);
        int alreadyInCart = 0;

        if(existingCart != null && existingCart.getItems() != null){
            alreadyInCart = existingCart.getItems().stream()
                    .filter(i -> sku.equals(i.getVariantSku()))
                    .mapToInt(OrderVariant::getQuantity)
                    .findFirst().orElse(0);
        }

        if(alreadyInCart + quantity > stock){
            throw new RuntimeException("Quantity exceeds stock (" + stock + ")");
        }

        float unitPrice = variant.getProduct().getPrice();

        Order cart = (existingCart != null) ? existingCart
                :orderRepository.save(
                        Order.builder()
                                .user(user)
                                .status(OrderStatus.Pending)
                                .items(new ArrayList<>())
                                .build()
        );
        if(cart.getStatus() != OrderStatus.Pending){
            throw new RuntimeException("cart is not modifiable ( status = " +cart.getStatus() + ")");
        }
        if(cart.getItems() == null) cart.setItems(new ArrayList<>());
        cart.addOrIncreaseItem(sku, unitPrice, quantity);
        cart.recalcTotal();

        return toRes(orderRepository.save(cart));
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

    @Transactional
    @Override
    public OrderResponse updateStatus(int id, String newStatus) {
        OrderStatus newOS = OrderStatus.valueOf(newStatus);
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Order not found: " + id));

        OrderStatus oldOS = order.getStatus();

        if(oldOS.ordinal() > newOS.ordinal()){
            throw new RuntimeException("Cannot move status backward");
        }
        if(oldOS == OrderStatus.Pending && newOS == OrderStatus.WaitConfirm && !order.isStockDeducted()){
            adjustInventory(order, -1);
            order.setStockDeducted(true);
        }

        if(oldOS == OrderStatus.WaitConfirm && newOS == OrderStatus.Cancelled && order.isStockDeducted()){
            adjustInventory(order, +1);
            order.setStockDeducted(false);
        }

        if(oldOS == OrderStatus.Shipped && newOS == OrderStatus.DeliveryFailed && order.isStockDeducted()){
            adjustInventory(order, +1);
            order.setStockDeducted(false);
        }
        if(oldOS == OrderStatus.Delivered && newOS == OrderStatus.Cancelled){
           throw new RuntimeException("Cannot cancel delivered order");
        }

        order.setStatus(newOS);
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
            Variant variant = variantRepository.findBySku(sku)
                    .orElseThrow(() -> new RuntimeException("Variant not found by SKU: " + sku));
            int stock = variant.getQuantity();
            if (quantity > stock) {
                throw new RuntimeException("Quantity exceeds stock (" + stock + ")");
        }
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

    @Transactional
    @Override
    public OrderResponse checkout(CheckoutRequest request) {
        String email = request.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found: " + email));

        Order cart = orderRepository.findByUserAndStatus(user,OrderStatus.Pending)
                .orElseThrow(()-> new RuntimeException("No pending cart for user"));

        if(cart.getItems() == null || cart.getItems().isEmpty()){
            throw new RuntimeException("Cart is empty");
        }

        OrderResponse updated = updateStatusInternal(cart, OrderStatus.WaitConfirm);

        return updated;
    }

    @Transactional
    @Override
    public OrderResponse cancel(int id, OrderCancelRequest request) {
        String email = request.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found " + email));

        Order order = orderRepository.findByUserAndStatus(user, OrderStatus.WaitConfirm)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if(order.getUser() == null || !order.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Permission denied to cancel this order");
        }
        if(order.getStatus() == OrderStatus.Delivered){
            throw new RuntimeException("Cannot cancel an order that has been delivered");
        }
        if(order.getStatus() == OrderStatus.Cancelled){
            return toRes(order);
        }
        return updateStatus(id, String.valueOf(OrderStatus.Cancelled));
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
            String imageUrl = "";
            int sizeValue = 0;
            String colorName = "";
            int variantStock = 0;

            if(v != null && v.getProduct() != null){
                var p = v.getProduct();
                productId = p.getId();
                productname = p.getProductName();
                imageUrl = p.getThumbnail() != null ? p.getThumbnail() : "";
            }
            var c = v.getColor();
            if(c != null){
                colorName = c.getColorName() != null ? c.getColorName(): "";
            }
            var s = v.getSize();
            if(s != null){
                sizeValue = s.getSizeValue();
            }
            variantStock = v.getQuantity();
            var lineTotal = i.getPrice()*i.getQuantity();

            return OrderItemResponse.builder()
                    .id(i.getId())
                    .sku(i.getVariantSku())
                    .productId(productId)
                    .productName(productname)
                    .imageUrl(imageUrl)
                    .colorName(colorName)
                    .sizeValue(sizeValue)
                    .quantity(i.getQuantity())
                    .variantStock(variantStock)
                    .price(i.getPrice())
                    .lineTotal(lineTotal)
                    .build();

        }).toList();
    }

    private void adjustInventory(Order order, int direction) {
        List<String> skus = order.getItems().stream().map(OrderVariant::getVariantSku).toList();
        List<Variant> variants = variantRepository.findBySkuInForUpdate(skus);
        var bySku = variants.stream()
                .collect(Collectors.toMap(Variant::getSku, v -> v, (a, b) -> a));

        for (OrderVariant item : order.getItems()) {
            Variant v = bySku.get(item.getVariantSku());
            if (v == null) continue;

            int newQty = v.getQuantity() + (direction * item.getQuantity());
            if (direction < 0 && newQty < 0)
                throw new RuntimeException("Out of stock for " + v.getSku());

            v.setQuantity(newQty);
        }
        variantRepository.saveAll(variants);
    }

    private OrderResponse updateStatusInternal(Order order, OrderStatus newStatus){
        OrderStatus oldStatus = order.getStatus();

        if(oldStatus == OrderStatus.Pending && newStatus == OrderStatus.WaitConfirm
        && !order.isStockDeducted()){
            adjustInventory(order, -1);
            order.setStockDeducted(true);
        }
        order.setStatus(newStatus);
        return toRes(orderRepository.save(order));
    }

}
