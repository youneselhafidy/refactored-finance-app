package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import java.time.LocalDateTime;

/**
 * Strategy Pattern - Implémentation pour les opérations de dépôt.
 * Gère la logique métier spécifique aux dépôts d'argent.
 */
public class DepositStrategy implements TransactionStrategy {
    
    @Override
    public Transaction execute(Account account, double amount, Account targetAccount) {
        if (!validate(account, amount)) {
            throw new IllegalArgumentException("Dépôt invalide: montant doit être positif");
        }
        
        double previousBalance = account.getBalance();
        account.setBalance(previousBalance + amount);
        
        Transaction transaction = new Transaction(
            account.getAccountNumber(),
            "DEPOSIT",
            amount,
            previousBalance,
            account.getBalance(),
            LocalDateTime.now()
        );
        
        account.addTransaction(transaction);
        return transaction;
    }
    
    @Override
    public boolean validate(Account account, double amount) {
        return amount > 0;
    }
}
