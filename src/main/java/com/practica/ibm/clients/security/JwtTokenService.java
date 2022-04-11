package com.practica.ibm.clients.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class JwtTokenService {

    private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8jeX4BZelWCWcORCe60Z" +
            "lyfN/yCESX1QpiuadoflMMUfQw73CzZalkOLFgIGwyOV+JesbFflmUDQK9BCbwLq" +
            "3r1KwMmfi1gYSzoNbFspT7YDjTi0RlBd2ZWwboetGHwQQtY2ZUisldaxzw+yoop8" +
            "49NNksBqZKbs3GNbIg5J5+HJkqxb4c4TuTHP0lXuRUOSdSUIHCkR2GjiPaxabgHr" +
            "89t9n/b53G5aCtK7kAZkH7MUu3uUVI7kXcPTVvhX81ExOOiyTSTLvlguzpVIw93x" +
            "Fezn6LnjdWDdtILBqkUQ3B7vGamh57eQcKBvSHy5TUbWesq7fZxTBN14X1DUm4se" +
            "EQIDAQAB";

    public JsonNode validateToken(String token) {
        System.out.println("validating token...");
        ObjectMapper objectMapper = new ObjectMapper();
        Claims claims = getClaimsFromToken(token);
        if (token != null && isTokenValid(claims)) {
            JwtTokenClaims jwtTokenClaims = getClaimsValues(claims);
            return objectMapper.valueToTree(jwtTokenClaims);
        }
        System.out.println("token validation failed");
        return objectMapper.createObjectNode().put("error", "Unauthorized request");
    }

    private boolean isTokenValid(Claims claims) {
        if (claims != null) {
            return !claims.get("email").toString().isEmpty();
        }
        return false;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(getPublicSigningKey()).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired, please login again");
        } catch (MalformedJwtException e) {
            System.out.println("Malformed token");
        }

        return claims;
    }

    @SneakyThrows
    private PublicKey getPublicSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    private JwtTokenClaims getClaimsValues(Claims claims) {
        JwtTokenClaims tokenClaims = new JwtTokenClaims();
        tokenClaims.setEmail(claims.get("email").toString());
        return tokenClaims;
    }

}
