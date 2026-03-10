package com.donation.api.service;

import com.donation.api.dto.DonationRequest;
import com.donation.api.dto.DonationResponse;
import com.donation.api.dto.UserResponse;
import com.donation.api.entity.Donation;
import com.donation.api.entity.User;
import com.donation.api.entity.enums.FoodCategory;
import com.donation.api.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationService {
    
    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private UserService userService;
    
    @Transactional(readOnly = true)
    public Page<DonationResponse> getAllDonations(
            FoodCategory category, 
            String city, 
            String state, 
            Donation.DonationStatus status,
            String search,
            int page, 
            int size, 
            String sortBy, 
            String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Donation> donations = donationRepository.findWithFilters(
            category, city, state, status, search, pageable);
        
        return donations.map(this::convertToResponse);
    }
    
    public DonationResponse createDonation(String userEmail, DonationRequest request) {
        User donor = userService.findByEmail(userEmail);
        
        Donation donation = new Donation();
        donation.setTitle(request.getTitle());
        donation.setDescription(request.getDescription());
        donation.setCategory(request.getCategory());
        donation.setQuantity(request.getQuantity());
        donation.setExpirationDate(request.getExpirationDate());
        donation.setPerishable(request.getPerishable());
        donation.setStorageInstructions(request.getStorageInstructions());
        donation.setLocation(request.getLocation());
        donation.setCity(request.getCity());
        donation.setState(request.getState());
        donation.setZipCode(request.getZipCode());
        donation.setImageUrls(request.getImageUrls());
        donation.setPickupInstructions(request.getPickupInstructions());
        donation.setExpiresAt(request.getExpiresAt());
        donation.setDonor(donor);
        
        Donation savedDonation = donationRepository.save(donation);
        return convertToResponse(savedDonation);
    }
    
    @Transactional(readOnly = true)
    public DonationResponse getDonationById(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doação não encontrada"));
        
        return convertToResponse(donation);
    }
    
    public DonationResponse updateDonation(Long id, String userEmail, DonationRequest request) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doação não encontrada"));
        
        // Verificar se o usuário é o dono da doação
        if (!donation.getDonor().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para editar esta doação");
        }
        
        // Atualizar campos
        donation.setTitle(request.getTitle());
        donation.setDescription(request.getDescription());
        donation.setCategory(request.getCategory());
        donation.setQuantity(request.getQuantity());
        donation.setExpirationDate(request.getExpirationDate());
        donation.setPerishable(request.getPerishable());
        donation.setStorageInstructions(request.getStorageInstructions());
        donation.setLocation(request.getLocation());
        donation.setCity(request.getCity());
        donation.setState(request.getState());
        donation.setZipCode(request.getZipCode());
        donation.setImageUrls(request.getImageUrls());
        donation.setPickupInstructions(request.getPickupInstructions());
        donation.setExpiresAt(request.getExpiresAt());
        
        Donation updatedDonation = donationRepository.save(donation);
        return convertToResponse(updatedDonation);
    }
    
    public void deleteDonation(Long id, String userEmail) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doação não encontrada"));
        
        // Verificar se o usuário é o dono da doação
        if (!donation.getDonor().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para deletar esta doação");
        }
        
        // Verificar se não há matches pendentes ou aprovados
        if (donation.getMatches() != null && !donation.getMatches().isEmpty()) {
            boolean hasActiveMatches = donation.getMatches().stream()
                .anyMatch(match -> match.getStatus() == com.donation.api.entity.Match.MatchStatus.PENDING ||
                                 match.getStatus() == com.donation.api.entity.Match.MatchStatus.APPROVED ||
                                 match.getStatus() == com.donation.api.entity.Match.MatchStatus.IN_PROGRESS);
            
            if (hasActiveMatches) {
                throw new RuntimeException("Não é possível deletar doação com solicitações ativas");
            }
        }
        
        donationRepository.delete(donation);
    }
    
    @Transactional(readOnly = true)
    public List<DonationResponse> getUserDonations(String userEmail) {
        try {
            System.out.println("=== getUserDonations ===");
            System.out.println("User email: " + userEmail);
            
            User user = userService.findByEmail(userEmail);
            System.out.println("User found: " + user.getId() + " - " + user.getName());
            
            List<Donation> donations = donationRepository.findByDonorWithUser(user);
            System.out.println("Found " + donations.size() + " donations for user");
            
            if (!donations.isEmpty()) {
                System.out.println("First donation details:");
                Donation firstDonation = donations.get(0);
                System.out.println("- ID: " + firstDonation.getId());
                System.out.println("- Title: " + firstDonation.getTitle());
                System.out.println("- Donor: " + (firstDonation.getDonor() != null ? firstDonation.getDonor().getName() : "null"));
                System.out.println("- Matches count: " + (firstDonation.getMatches() != null ? firstDonation.getMatches().size() : "null"));
            }
            
            System.out.println("Starting conversion to DonationResponse...");
            List<DonationResponse> result = donations.stream()
                    .map(donation -> {
                        System.out.println("Converting donation: " + donation.getId());
                        return this.convertToResponse(donation);
                    })
                    .collect(Collectors.toList());
            
            System.out.println("Conversion completed successfully. Returning " + result.size() + " responses");
            return result;
            
        } catch (Exception e) {
            System.err.println("Error in getUserDonations: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar doações do usuário: " + e.getMessage(), e);
        }
    }
    
    public List<String> getCategories() {
        return Arrays.stream(FoodCategory.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
    
    public List<String> getCities() {
        return donationRepository.findDistinctCities();
    }
    
    private DonationResponse convertToResponse(Donation donation) {
        try {
            // Conversão manual para evitar problemas de referência circular
            DonationResponse response = new DonationResponse();
            
            // Campos básicos
            response.setId(donation.getId());
            response.setTitle(donation.getTitle());
            response.setDescription(donation.getDescription());
            response.setCategory(donation.getCategory());
            response.setQuantity(donation.getQuantity());
            response.setExpirationDate(donation.getExpirationDate());
            response.setPerishable(donation.getPerishable());
            response.setStorageInstructions(donation.getStorageInstructions());
            response.setLocation(donation.getLocation());
            response.setCity(donation.getCity());
            response.setState(donation.getState());
            response.setZipCode(donation.getZipCode());
            response.setStatus(donation.getStatus());
            response.setImageUrls(donation.getImageUrls());
            response.setPickupInstructions(donation.getPickupInstructions());
            response.setExpiresAt(donation.getExpiresAt());
            response.setCreatedAt(donation.getCreatedAt());
            response.setUpdatedAt(donation.getUpdatedAt());
            
            // Converter o donor manualmente (evita problemas de lazy loading)
            if (donation.getDonor() != null) {
                UserResponse donorResponse = new UserResponse();
                donorResponse.setId(donation.getDonor().getId());
                donorResponse.setName(donation.getDonor().getName());
                donorResponse.setEmail(donation.getDonor().getEmail());
                // Não incluir campos sensíveis ou lazy como address, password, etc.
                response.setDonor(donorResponse);
            }
            
            return response;
        } catch (Exception e) {
            // Log do erro para debug
            System.err.println("Erro na conversão Donation -> DonationResponse: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao converter doação para resposta: " + e.getMessage(), e);
        }
    }
}
