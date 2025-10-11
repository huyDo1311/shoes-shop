package com.cybersoft.shop.repository;

import com.cybersoft.shop.entity.RefreshToken;
import com.cybersoft.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken>  findByUser(User user);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
