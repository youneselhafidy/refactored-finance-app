package com.university.finance.model;

import java.time.LocalDateTime;

/**
 * Classe représentant une transaction bancaire.
 * Suit le principe SRP (Single Responsibility Principle).
 */
public class Transaction {
    private final String accountNumber;
    private final String type;
    private final double amount;
    private final double balanceBefore;
    private final double balanceAfter;
    private final LocalDateTime timestamp;
    private final String description;
    
    public Transaction(String accountNumber, String type, double amount, 
                      double balanceBefore, double balanceAfter, LocalDateTime timestamp) {
        this(accountNumber, type, amount, balanceBefore, balanceAfter, timestamp, "");
    }
    
    public Transaction(String accountNumber, String type, double amount, 
                      double balanceBefore, double balanceAfter, LocalDateTime timestamp, String description) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.timestamp = timestamp;
        this.description = description != null ? description : "";
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getType() {
        return type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public double getBalanceBefore() {
        return balanceBefore;
    }
    
    public double getBalanceAfter() {
        return balanceAfter;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %.2f€ (Solde: %.2f€ → %.2f€) %s",
            timestamp.toString(), type, amount, balanceBefore, balanceAfter,
            description.isEmpty() ? "" : "- " + description);
    }
}
