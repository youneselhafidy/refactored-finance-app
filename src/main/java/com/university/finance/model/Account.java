package com.university.finance.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant un compte bancaire.
 * Encapsule les données et comportements d'un compte.
 */
public class Account {
    private final String accountNumber;
    private final String ownerUsername;
    private double balance;
    private final List<Transaction> transactions;
    
    public Account(String accountNumber, String ownerUsername, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Le solde initial ne peut pas être négatif");
        }
        this.accountNumber = accountNumber;
        this.ownerUsername = ownerUsername;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getOwnerUsername() {
        return ownerUsername;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
    
    /**
     * Retourne une copie non modifiable de l'historique des transactions.
     */
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
    
    @Override
    public String toString() {
        return String.format("Compte[%s] Propriétaire: %s, Solde: %.2f€", 
            accountNumber, ownerUsername, balance);
    }
}
