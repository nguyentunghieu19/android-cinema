package com.cinema.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Thời gian hết hạn token: 24 giờ
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // Secret Key dùng để ký JWT
    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    /**
     * Sinh JWT
     */
    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();

    }

    /**
     * Lấy Username từ JWT
     */
    public String extractUsername(String token) {

        return extractAllClaims(token).getSubject();

    }

    /**
     * Kiểm tra JWT có hợp lệ không
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);

    }

    /**
     * Kiểm tra Token hết hạn chưa
     */
    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());

    }

    /**
     * Lấy thời gian hết hạn
     */
    private Date extractExpiration(String token) {

        return extractAllClaims(token).getExpiration();

    }

    /**
     * Đọc toàn bộ Claims trong JWT
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

}