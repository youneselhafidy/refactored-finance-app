package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import java.time.LocalDateTime;

/**
 * Strategy Pattern - Implémentation pour les opérations de transfert.
 * Gère les transferts entre deux comptes avec validation du solde.
 */
public class TransferStrategy implements TransactionStrategy {
    
    @Override
    public Transaction execute(Account account, double amount, Account targetAccount) {
        if (targetAccount == null) {
            throw new IllegalArgumentException("Compte cible requis pour un transfert");
        }
        
        if (!validate(account, amount)) {
            throw new IllegalArgumentException(
                "Transfert invalide: solde insuffisant ou montant négatif"
            );
        }
        
        double previousBalance = account.getBalance();
        double targetPreviousBalance = targetAccount.getBalance();
        
        // Débiter le compte source
        account.setBalance(previousBalance - amount);
        
        // Créditer le compte cible
        targetAccount.setBalance(targetPreviousBalance + amount);
        
        // Transaction pour le compte source
        Transaction sourceTransaction = new Transaction(
            account.getAccountNumber(),
            "TRANSFER_OUT",
            -amount,
            previousBalance,
            account.getBalance(),
            LocalDateTime.now(),
            "Transfert vers " + targetAccount.getAccountNumber()
        );
        
        // Transaction pour le compte cible
        Transaction targetTransaction = new Transaction(
            targetAccount.getAccountNumber(),
            "TRANSFER_IN",
            amount,
            targetPreviousBalance,
            targetAccount.getBalance(),
            LocalDateTime.now(),
            "Transfert depuis " + account.getAccountNumber()
        );
        
        account.addTransaction(sourceTransaction);
        targetAccount.addTransaction(targetTransaction);
        
        return sourceTransaction;
    }
    
    @Override
    public boolean validate(Account account, double amount) {
        return amount > 0 && account.getBalance() >= amount;
    }
}
