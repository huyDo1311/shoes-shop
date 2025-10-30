package com.cybersoft.shop.repository;

import com.cybersoft.shop.entity.Order;
import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByUserAndStatus(User user, OrderStatus status);

    Optional<Order> findByVnpTxnRef(String vnp_TxnRef);
}
