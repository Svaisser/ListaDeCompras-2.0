package br.com.svaisser.listaCompras.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil {

    private String secretKey = "OdeioJava_123";
    private static final int MAX_LOGINS = 3;

    public String generateToken(Integer idUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("loginCount", 0); // Contador de logins inicializad

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(idUser.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h de validade
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Integer extractIdUser(String token) {
        String IdUserString = getClaims(token).getSubject();
        return Integer.parseInt(IdUserString); // Converte de String para Integer
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, Integer idUser) {
        Claims claims = getClaims(token);

        Integer loginCount = claims.get("loginCount", Integer.class);
        if (loginCount >= MAX_LOGINS) {
            throw new IllegalStateException("Token expirado por limite de logins.");
        }

        // Incrementa o contador de logins
        claims.put("loginCount", loginCount + 1);
        System.out.println("Contador de Login: " + loginCount + 1);

        return (extractIdUser(token).equals(idUser) && !isTokenExpired(token));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
