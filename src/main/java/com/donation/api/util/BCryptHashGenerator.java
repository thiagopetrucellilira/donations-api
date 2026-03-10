package com.donation.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        String hash = encoder.encode(password);
        
        System.out.println("=== GERADOR DE HASH BCRYPT ===");
        System.out.println("Senha: " + password);
        System.out.println("Hash BCrypt: " + hash);
        System.out.println("Verificação: " + encoder.matches(password, hash));
        System.out.println("=============================");
        
        // Testar com hashes conhecidos
        String[] knownHashes = {
            "$2a$10$e0MYzXyjpJS7Pd0RVvHqHOxHbkdrqcCauG5DhLEKKlOxlw3sJjdMa",
            "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi",
            "$2a$10$N9qo8uLOickgx2ZMRZoMye7VPnGLc.kGUyPHfB0Ps9Zd8zCEK8Wj6"
        };
        
        System.out.println("\n=== TESTE DE HASHES CONHECIDOS ===");
        for (int i = 0; i < knownHashes.length; i++) {
            boolean matches = encoder.matches(password, knownHashes[i]);
            System.out.println("Hash " + (i + 1) + ": " + matches + " - " + knownHashes[i]);
        }
    }
}
