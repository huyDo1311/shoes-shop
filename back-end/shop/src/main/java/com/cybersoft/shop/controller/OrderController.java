package com.cybersoft.shop.controller;

import com.cybersoft.shop.entity.Order;
import com.cybersoft.shop.entity.Variant;
import com.cybersoft.shop.request.*;
import com.cybersoft.shop.response.ResponseObject;
import com.cybersoft.shop.response.order.OrderResponse;
import com.cybersoft.shop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/cart")
    public ResponseEntity<?> addToCart(@Valid @RequestBody CartAddRequest req){
        OrderResponse data = orderService.addToCart(req.getEmail(),req.getSku(), req.getQuantity());
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Add to cart successfully")
                        .status(HttpStatus.CREATED.value())
                        .data(data)
                        .build());
    }

    @PostMapping("/cart/get")
    public ResponseEntity<?> getCart(@RequestBody GetCartRequest getCartRequest) {
        OrderResponse data = orderService.getCart(getCartRequest.getEmail());
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get cart successfully")
                        .status(HttpStatus.OK.value())
                        .data(data)
                        .build());
    }
    @PutMapping("/cart/items")
    public ResponseEntity<?> updateItem(@Valid @RequestBody CartUpdateQuantityRequest req){
        OrderResponse data = orderService.updateCartItemQuantity(req.getEmail(), req.getSku(), req.getQuantity());
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message(req.getQuantity() <= 0 ? "Item removed from cart" : "Quantity updated")
                        .data(data)
                        .build()
        );
    }
    @DeleteMapping("/cart/items/delete")
    public ResponseEntity<?> removeItem(@RequestBody DeleteItemCartRequest deleteItemCartRequest){
        OrderResponse data = orderService.removeCartItem(deleteItemCartRequest.getEmail(), deleteItemCartRequest.getSku());
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Item removed")
                        .data(data)
                        .build()
        );
    }

    @DeleteMapping("/cart")
    public ResponseEntity<?> clearCart(@RequestParam("email") String email){
        OrderResponse data = orderService.clearCart(email);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Cart cleared")
                        .data(data)
                        .build()
        );
    }

    // tìm order theo id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        OrderResponse data = orderService.getById(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get Order detail successfully")
                        .status(HttpStatus.OK.value())
                        .data(data)
                        .build());
    }
    // cập nhật trạng thái order
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable int id,
                                          @Valid @RequestBody OrderStatusUpdateRequest req) {
        OrderResponse data = orderService.updateStatus(id, req.getStatus());
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Order status updated")
                        .status(HttpStatus.OK.value())
                        .data(data)
                        .build()
        );
    }

    // xóa order
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        orderService.delete(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Delete successfully")
                        .status(HttpStatus.NO_CONTENT.value())
                        .data(null)
                        .build());
    }

}
