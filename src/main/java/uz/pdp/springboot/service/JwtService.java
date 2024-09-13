package uz.pdp.springboot.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    // Access token yaratish
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 daqiqa amal qilish muddati
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();
    }

    // Refresh token yaratish
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 kun amal qilish muddati
                .signWith(SignatureAlgorithm.HS512, "refreshSecretKey")
                .compact();
    }

    // Refresh tokenni tasdiqlash
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey("refreshSecretKey").parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Token ichidan foydalanuvchi nomini olish
    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey("secretKey").parseClaimsJws(token).getBody().getSubject();
    }
}
