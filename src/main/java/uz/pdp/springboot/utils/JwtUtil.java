package uz.pdp.springboot.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_HS256_KEY = "34e7f550e2715c958372db43db8f68c0ea686807bf7eab4f22f2ee673df0f929";

    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, 10 * 60 * 1000); // 10 daqiqa
    }

    // 2-token (1 oy davomida amal qiladi)
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, 30L * 24 * 60 * 60 * 1000); // 1 oy
    }

    // Token yaratish funksiyasi
    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_HS256_KEY)
                .compact();
    }

    // Tokenni tasdiqlash funksiyasi
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    // Token ichidan foydalanuvchini olish
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Tokenni tekshirish
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Token muddati
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Claim olish uchun umumiy funksiyalar
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_HS256_KEY).parseClaimsJws(token).getBody();
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_HS256_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
