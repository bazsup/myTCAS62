package th.in.tcas.services;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import th.in.tcas.DTO.JWTResponse;
import th.in.tcas.exception.JWTExpiredException;
import th.in.tcas.models.User;

import java.util.Date;

@Service
public class JWTService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiresec}")
    private long EXPIRE_IN_SECOND;


    private long getExpireMillisecond() {
        int MILLISECOND = 1000;
        return System.currentTimeMillis() + EXPIRE_IN_SECOND * MILLISECOND;
    }

    public JWTResponse createToken(User user) {
        JWTResponse authResponse = new JWTResponse();
        Date expiredDate = new Date(this.getExpireMillisecond());
        String token = Jwts.builder().setSubject("UserService").claim("userId", "" + user.getId())
                .setExpiration(expiredDate).signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        authResponse.setToken(token);
        authResponse.setExpiryDate(expiredDate.getTime());
        return authResponse;
    }

    public long getUserIdFromToken(String token) {
        int bearerLength = 7;
        String tokenFormat = token.substring(bearerLength);
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(tokenFormat);
            String userId = (String) claims.getBody().get("userId");
            return Long.parseLong(userId);
        } catch (ExpiredJwtException e) {
            throw new JWTExpiredException(String.format("Token Expired for %s.", e.getClaims().toString()),
                    e.getCause());
        }
    }
}
