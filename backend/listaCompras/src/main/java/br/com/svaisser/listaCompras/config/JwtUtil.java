package br.com.svaisser.listaCompras.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtUtil {

    private String secretKey = "OdeioJava_123";

    public String generateToken(Integer idUser) {
        return Jwts.builder()
                .setSubject(idUser.toString()) // Armazena o ID do usuário como String
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 horas
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
        return (extractIdUser(token).equals(idUser) && !isTokenExpired(token));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
