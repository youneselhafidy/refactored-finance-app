package com.university.finance.pattern.factory;

import com.university.finance.model.Account;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Factory Pattern - Fabrique pour créer des comptes bancaires.
 * Centralise la logique de création et génère des numéros de compte uniques.
 */
public class AccountFactory {
    private static final AtomicLong accountCounter = new AtomicLong(1000);
    private AccountFactory() {
        // Constructeur privé pour empêcher l'instanciation
    }
    /**
     * Crée un nouveau compte avec un numéro de compte auto-généré.
     * 
     * @param ownerUsername Le nom d'utilisateur du propriétaire
     * @param initialBalance Le solde initial
     * @return Un nouveau compte
     */
    public static Account createAccount(String ownerUsername, double initialBalance) {
        String accountNumber = generateAccountNumber();
        return new Account(accountNumber, ownerUsername, initialBalance);
    }
    
    /**
     * Crée un compte de test avec un numéro spécifique.
     * Utilisé principalement pour les tests unitaires.
     * 
     * @param accountNumber Numéro de compte spécifique
     * @param ownerUsername Le nom d'utilisateur du propriétaire
     * @param initialBalance Le solde initial
     * @return Un nouveau compte
     */
    public static Account createAccountWithNumber(String accountNumber, 
                                                  String ownerUsername, 
                                                  double initialBalance) {
        return new Account(accountNumber, ownerUsername, initialBalance);
    }
    
    /**
     * Génère un numéro de compte unique au format ACC-XXXX.
     */
    private static String generateAccountNumber() {
        long number = accountCounter.getAndIncrement();
        return String.format("ACC-%04d", number);
    }
    
    /**
     * Réinitialise le compteur (utile pour les tests).
     */
    public static void resetCounter() {
        accountCounter.set(1000);
    }
}
