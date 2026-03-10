package com.donation.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTester {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String rawPassword = "123456";
        String hashedPassword = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";
        
        System.out.println("Password raw: " + rawPassword);
        System.out.println("Password hash: " + hashedPassword);
        System.out.println("Matches: " + encoder.matches(rawPassword, hashedPassword));
        
        // Gerar um novo hash para comparar
        String newHash = encoder.encode(rawPassword);
        System.out.println("New hash generated: " + newHash);
        System.out.println("New hash matches: " + encoder.matches(rawPassword, newHash));
    }
}
