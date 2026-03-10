package com.donation.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    
    @Value("${app.jwt.secret}")
    private String secret;
    
    @Value("${app.jwt.expiration}")
    private Long expiration;
    
    private SecretKey getSigningKey() {
        try {
            // Garantir que a chave tenha pelo menos 256 bits (32 bytes) para HS256
            byte[] keyBytes = secret.getBytes("UTF-8");
            if (keyBytes.length < 32) {
                // Se a chave for muito pequena, expandir usando SHA-256
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
                keyBytes = digest.digest(keyBytes);
            }
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            logger.error("Erro ao gerar chave de assinatura: {}", e.getMessage());
            // Fallback para a chave original
            return Keys.hmacShaKeyFor(secret.getBytes());
        }
    }
    
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            logger.warn("Erro ao extrair username do token: {}", e.getMessage());
            return null;
        }
    }
    
    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (Exception e) {
            logger.warn("Erro ao extrair data de expiração do token: {}", e.getMessage());
            return null;
        }
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claims != null ? claimsResolver.apply(claims) : null;
        } catch (Exception e) {
            logger.warn("Erro ao extrair claim do token: {}", e.getMessage());
            return null;
        }
    }
    
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.warn("Assinatura JWT inválida: {}", e.getMessage());
            throw e; // Re-throw para tratamento específico no filtro
        } catch (ExpiredJwtException e) {
            logger.warn("Token JWT expirado: {}", e.getMessage());
            throw e; // Re-throw para tratamento específico no filtro
        } catch (MalformedJwtException e) {
            logger.warn("Token JWT mal formado: {}", e.getMessage());
            throw e; // Re-throw para tratamento específico no filtro
        } catch (Exception e) {
            logger.error("Erro inesperado ao processar token JWT: {}", e.getMessage());
            throw e;
        }
    }
    
    private Boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration != null && expiration.before(new Date());
        } catch (Exception e) {
            logger.warn("Erro ao verificar expiração do token: {}", e.getMessage());
            return true; // Considerar expirado em caso de erro
        }
    }
    
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }
    
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isValid = (username != null && 
                              username.equals(userDetails.getUsername()) && 
                              !isTokenExpired(token));
            
            if (isValid) {
                logger.debug("Token válido para usuário: {}", username);
            } else {
                logger.warn("Token inválido para usuário: {}", username);
            }
            
            return isValid;
        } catch (Exception e) {
            logger.error("Erro ao validar token: {}", e.getMessage());
            return false;
        }
    }
    
    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            
            boolean isNotExpired = !isTokenExpired(token);
            if (isNotExpired) {
                logger.debug("Token válido e não expirado");
            } else {
                logger.warn("Token expirado");
            }
            
            return isNotExpired;
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.warn("Token com assinatura inválida: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            logger.warn("Token expirado: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            logger.warn("Token mal formado: {}", e.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Token inválido: {}", e.getMessage());
            return false;
        }
    }
}
