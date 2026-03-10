package com.donation.api.security;

import com.donation.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("=== CustomUserDetailsService.loadUserByUsername() ===");
        System.out.println("Loading user by email: " + email);
        
        return userRepository.findByEmailAndEnabledTrue(email)
                .map(user -> {
                    System.out.println("User found: " + user.getEmail() + ", enabled: " + user.isEnabled());
                    return user;
                })
                .orElseThrow(() -> {
                    System.out.println("User not found or disabled for email: " + email);
                    return new UsernameNotFoundException("Usuário não encontrado com email: " + email);
                });
    }
}
