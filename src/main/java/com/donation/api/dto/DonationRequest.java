package com.donation.api.dto;

import com.donation.api.entity.enums.FoodCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DonationRequest {
    
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 5, max = 200, message = "Título deve ter entre 5 e 200 caracteres")
    private String title;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, message = "Descrição deve ter no mínimo 10 caracteres")
    private String description;
    
    @NotNull(message = "Categoria é obrigatória")
    private FoodCategory category;
    
    @Positive(message = "Quantidade deve ser positiva")
    private Integer quantity = 1;
    
    private LocalDate expirationDate;
    private Boolean perishable = false;
    private String storageInstructions;
    private String location;
    private String city;
    private String state;
    private String zipCode;
    private String imageUrls;
    private String pickupInstructions;
    private LocalDateTime expiresAt;
    
    public DonationRequest() {}
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public FoodCategory getCategory() {
        return category;
    }
    
    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getPerishable() {
        return perishable;
    }

    public void setPerishable(Boolean perishable) {
        this.perishable = perishable;
    }

    public String getStorageInstructions() {
        return storageInstructions;
    }

    public void setStorageInstructions(String storageInstructions) {
        this.storageInstructions = storageInstructions;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public String getImageUrls() {
        return imageUrls;
    }
    
    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }
    
    public String getPickupInstructions() {
        return pickupInstructions;
    }
    
    public void setPickupInstructions(String pickupInstructions) {
        this.pickupInstructions = pickupInstructions;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
