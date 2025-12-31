package com.university.finance.pattern.factory;

import com.university.finance.model.User;

/**
 * Factory Pattern - Fabrique pour créer des utilisateurs.
 * Centralise la logique de création et validation des utilisateurs.
 */
public class UserFactory {
    private UserFactory() {
        // Constructeur privé pour empêcher l'instanciation
    }
    /**
     * Crée un nouvel utilisateur après validation des données.
     * 
     * @param username Le nom d'utilisateur
     * @param password Le mot de passe
     * @return Un nouvel utilisateur
     * @throws IllegalArgumentException Si les données sont invalides
     */
    public static User createUser(String username, String password) {
        validateUsername(username);
        validatePassword(password);
        return new User(username, password);
    }
    
    /**
     * Crée un utilisateur de test avec validation minimale.
     * 
     * @param username Le nom d'utilisateur
     * @param password Le mot de passe
     * @return Un nouvel utilisateur
     */
    public static User createTestUser(String username, String password) {
        return new User(username, password);
    }
    
    /**
     * Valide le format du nom d'utilisateur.
     */
    private static void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être vide");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException(
                "Le nom d'utilisateur doit contenir au moins 3 caractères"
            );
        }
        if (!username.matches("^[\\w]+$")) {
            throw new IllegalArgumentException(
                "Le nom d'utilisateur ne peut contenir que des lettres, chiffres et underscores"
            );
        }
    }
    
    /**
     * Valide le format du mot de passe.
     */
    private static void validatePassword(String password) {
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException(
                "Le mot de passe doit contenir au moins 4 caractères"
            );
        }
    }
}
