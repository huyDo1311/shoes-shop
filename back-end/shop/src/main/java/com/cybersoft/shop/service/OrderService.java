package com.cybersoft.shop.service;

import com.cybersoft.shop.enums.OrderStatus;
import com.cybersoft.shop.request.CheckoutRequest;
import com.cybersoft.shop.request.MomoStatusUpdateRequest;
import com.cybersoft.shop.request.OrderCancelRequest;
import com.cybersoft.shop.request.VNPStatusUpdateRequest;
import com.cybersoft.shop.response.order.OrderResponse;

public interface OrderService {
    OrderResponse addToCart(String email, String sku, int quantity);
    OrderResponse getCart(String email);
    OrderResponse getById(int id);
    OrderResponse updateStatus(int id, String newStatus);
    void delete(int id);

    OrderResponse updateCartItemQuantity(String email, String sku, int quantity);
    OrderResponse removeCartItem(String email, String sku);
    OrderResponse clearCart(String email);
    OrderResponse checkout(CheckoutRequest request);
    OrderResponse cancel(int id, OrderCancelRequest request);
    OrderResponse VNPStatusUpdate(VNPStatusUpdateRequest request);
    OrderResponse MomoStatusUpdate(MomoStatusUpdateRequest request);
}
