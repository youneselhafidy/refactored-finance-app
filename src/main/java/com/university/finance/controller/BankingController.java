package com.university.finance.controller;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.service.BankingService;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur bancaire qui découple la logique métier de l'interface utilisateur.
 * Ce contrôleur sert d'intermédiaire entre MainApp et les services.
 * Il est conçu pour être facilement testable et réutilisable.
 */
public class BankingController {
    
    private final BankingService bankingService;
    private String currentUsername;
    
    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
        this.currentUsername = null;
    }
    
    /**
     * Crée un nouveau compte utilisateur
     * @return Le numéro de compte créé, ou null si échec
     */
    public String createAccount(String username, String password, double initialDeposit) {
        try {
            Account account = bankingService.createUser(username, password, initialDeposit);
            return account.getAccountNumber();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Authentifie un utilisateur
     * @return true si l'authentification réussit
     */
    public boolean login(String username, String password) {
        if (bankingService.authenticate(username, password)) {
            this.currentUsername = username;
            return true;
        }
        return false;
    }
    
    /**
     * Déconnecte l'utilisateur courant
     */
    public void logout() {
        this.currentUsername = null;
    }
    
    /**
     * Vérifie si un utilisateur est connecté
     */
    public boolean isLoggedIn() {
        return currentUsername != null;
    }
    
    /**
     * Récupère le nom d'utilisateur courant
     */
    public String getCurrentUsername() {
        return currentUsername;
    }
    
    /**
     * Récupère le compte de l'utilisateur courant
     */
    public Account getCurrentAccount() {
        if (!isLoggedIn()) {
            return null;
        }
        return bankingService.getAccountByUsername(currentUsername).orElse(null);
    }
    
    /**
     * Récupère le solde de l'utilisateur courant
     */
    public Double getCurrentBalance() {
        Account account = getCurrentAccount();
        return account != null ? account.getBalance() : null;
    }
    
    /**
     * Effectue un dépôt pour l'utilisateur courant
     * @return true si le dépôt réussit
     */
    public boolean deposit(double amount) {
        if (!isLoggedIn()) {
            return false;
        }
        
        Account account = getCurrentAccount();
        if (account == null) {
            return false;
        }
        
        try {
            bankingService.deposit(account.getAccountNumber(), amount);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Effectue un retrait pour l'utilisateur courant
     * @return true si le retrait réussit
     */
    public boolean withdraw(double amount) {
        if (!isLoggedIn()) {
            return false;
        }
        
        Account account = getCurrentAccount();
        if (account == null) {
            return false;
        }
        
        try {
            bankingService.withdraw(account.getAccountNumber(), amount);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Effectue un transfert depuis l'utilisateur courant vers un autre utilisateur
     * @return true si le transfert réussit
     */
    public boolean transfer(String recipientUsername, double amount) {
        if (!isLoggedIn()) {
            return false;
        }
        
        Account fromAccount = getCurrentAccount();
        if (fromAccount == null) {
            return false;
        }
        
        Optional<Account> toAccountOpt = bankingService.getAccountByUsername(recipientUsername);
        if (!toAccountOpt.isPresent()) {
            return false;
        }
        
        try {
            bankingService.transfer(fromAccount.getAccountNumber(), 
                                   toAccountOpt.get().getAccountNumber(), amount);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Récupère l'historique des transactions de l'utilisateur courant
     */
    public List<Transaction> getTransactionHistory() {
        Account account = getCurrentAccount();
        return account != null ? account.getTransactions() : null;
    }
    
    /**
     * Calcule le nombre total de transactions de l'utilisateur courant
     */
    public int getTotalTransactionCount() {
        List<Transaction> transactions = getTransactionHistory();
        return transactions != null ? transactions.size() : 0;
    }
    
    /**
     * Calcule le total des dépôts de l'utilisateur courant
     */
    public double getTotalDeposits() {
        List<Transaction> transactions = getTransactionHistory();
        if (transactions == null) {
            return 0.0;
        }
        
        return transactions.stream()
            .filter(t -> t.getType().equals("DEPOSIT"))
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
    
    /**
     * Calcule le total des retraits de l'utilisateur courant
     */
    public double getTotalWithdrawals() {
        List<Transaction> transactions = getTransactionHistory();
        if (transactions == null) {
            return 0.0;
        }
        
        return transactions.stream()
            .filter(t -> t.getType().equals("WITHDRAW"))
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
    
    /**
     * Calcule le total des transferts de l'utilisateur courant
     */
    public double getTotalTransfers() {
        List<Transaction> transactions = getTransactionHistory();
        if (transactions == null) {
            return 0.0;
        }
        
        return transactions.stream()
            .filter(t -> t.getType().equals("TRANSFER"))
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
    
    /**
     * Vérifie si un utilisateur existe
     */
    public boolean userExists(String username) {
        return bankingService.getAccountByUsername(username).isPresent();
    }
}
