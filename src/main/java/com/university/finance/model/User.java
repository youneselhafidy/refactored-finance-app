package com.university.finance.model;

import java.time.LocalDateTime;

/**
 * Classe représentant un utilisateur du système bancaire.
 * Suit le principe d'encapsulation et de responsabilité unique.
 */
public class User {
    private final String username;
    private String password;
    private final LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    
    public User(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être vide");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 4 caractères");
        }
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.lastLogin = null;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }
    
    public void updatePassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 4) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 4 caractères");
        }
        this.password = newPassword;
    }
    
    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return String.format("User[%s] Créé le: %s", username, createdAt);
    }
}

