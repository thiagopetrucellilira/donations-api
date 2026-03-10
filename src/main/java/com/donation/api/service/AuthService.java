package com.donation.api.service;

import com.donation.api.dto.*;
import com.donation.api.entity.User;
import com.donation.api.entity.enums.UserRole;
import com.donation.api.repository.UserRepository;
import com.donation.api.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setZipCode(request.getZipCode());
        user.setBio(request.getBio());

        // Adicionando a lógica para o campo 'role'
        try {
            // Verifica se o papel da requisição é válido e o define na entidade User
            user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            // Lança uma exceção se o papel enviado não existir na sua enumeração
            throw new RuntimeException("Papel de usuário inválido.");
        }
        // --- FIM DA ALTERAÇÃO CRUCIAL ---

        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    public JwtResponse login(LoginRequest request) {
        try {
            System.out.println("=== AuthService.login() ===");
            System.out.println("Attempting to authenticate user: " + request.getEmail());
            System.out.println("Password provided: " + request.getPassword());
            
            // Verificar se o usuário existe no banco e sua senha hash
            User userFromDb = userRepository.findByEmailAndEnabledTrue(request.getEmail()).orElse(null);
            if (userFromDb != null) {
                System.out.println("User found in database: " + userFromDb.getEmail());
                System.out.println("Password hash in database: " + userFromDb.getPassword());
                System.out.println("Password matches: " + passwordEncoder.matches(request.getPassword(), userFromDb.getPassword()));
            } else {
                System.out.println("User not found in database");
            }
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            System.out.println("Authentication successful");
            User user = (User) authentication.getPrincipal();
            System.out.println("User found: " + user.getEmail() + ", enabled: " + user.isEnabled());
            
            String token = jwtUtil.generateToken(user);
            UserResponse userResponse = convertToResponse(user);

            System.out.println("Token generated successfully");
            return new JwtResponse(token, userResponse);

        } catch (BadCredentialsException e) {
            System.out.println("BadCredentialsException: " + e.getMessage());
            throw new RuntimeException("Email ou senha inválidos");
        } catch (Exception e) {
            System.out.println("Unexpected error during login: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro interno durante o login");
        }
    }

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmailAndEnabledTrue(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return convertToResponse(user);
    }
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setAddress(user.getAddress());
        response.setCity(user.getCity());
        response.setState(user.getState());
        response.setZipCode(user.getZipCode());
        response.setBio(user.getBio());
        response.setProfileImageUrl(user.getProfileImageUrl());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}