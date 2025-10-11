package com.cybersoft.shop.component;

import com.cybersoft.shop.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret-key}")// chu ky cua website minh
    private String secretKey;

    @Value("${jwt.expiration.access}")
    private long ACCESS_TOKEN_EXPIRATION;

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roleNames = user.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().getRoleName())
                .toList();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", roleNames);
        String subject = getSubject(user);
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        try {
            String token = Jwts.builder()
                    .claims(claims)
                    .subject(subject)
                    .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION * 1000L))
                    .signWith(key)
                    .compact();
            return token;
        }catch (Exception e) {
            //you can "inject" Logger, instead System.out.println
            throw new InvalidParameterException("Cannot create jwt token, error: " + e.getMessage());
            //return null;
        }
    }


    public boolean validateToken(String token) {
        try {
            // Giải mã khóa bí mật để xác thực token
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

            // Parse và kiểm tra chữ ký JWT
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            // Kiểm tra hạn token
            return !isTokenExpired(token);

        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during token validation: {}", e.getMessage());
        }

        return false;
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public Claims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        return claimsJws.getPayload();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()  // Khởi tạo JwtParserBuilder
                .verifyWith(getSignInKey())  // Sử dụng verifyWith() để thiết lập signing key
                .build()  // Xây dựng JwtParser
                .parseSignedClaims(token)  // Phân tích token đã ký
                .getPayload();  // Lấy phần body của JWT, chứa claims
    }

    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private SecretKey getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        //Keys.hmacShaKeyFor(Decoders.BASE64.decode("TaqlmGv1iEDMRiFp/pHuID1+T84IABfuA0xXh4GhiUI="));
        return Keys.hmacShaKeyFor(bytes);
    }

    private static String getSubject(User user) {
        return user.getEmail();
    }

    //    public String getSubject(String token) {
//        return  extractClaim(token, Claims::getSubject);
//    }


//    public boolean validateToken(String token) {
//        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
//        try {
//            Jws<Claims> claimsJws = Jwts.parser()
//                    .verifyWith(key)
//                    .build()
//                    .parseSignedClaims(token);
//
//            Claims claims = claimsJws.getPayload();
//            Date expiration = claims.getExpiration();
//            if (expiration != null && expiration.before(new Date())) {
//                System.out.println("Token has expired");
//                return false;
//            }
//
//            return true; // Token hợp lệ
//        } catch (Exception e) {
//            System.out.println("Invalid JWT token: " + e.getMessage());
//            return false;
//        }
//    }



}
