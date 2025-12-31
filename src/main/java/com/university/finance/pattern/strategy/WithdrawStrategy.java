package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import java.time.LocalDateTime;

/**
 * Strategy Pattern - Implémentation pour les opérations de retrait.
 * Vérifie le solde disponible avant d'autoriser le retrait.
 */
public class WithdrawStrategy implements TransactionStrategy {
    
    @Override
    public Transaction execute(Account account, double amount, Account targetAccount) {
        if (!validate(account, amount)) {
            throw new IllegalArgumentException(
                "Retrait invalide: solde insuffisant ou montant négatif"
            );
        }
        
        double previousBalance = account.getBalance();
        account.setBalance(previousBalance - amount);
        
        Transaction transaction = new Transaction(
            account.getAccountNumber(),
            "WITHDRAW",
            -amount,
            previousBalance,
            account.getBalance(),
            LocalDateTime.now()
        );
        
        account.addTransaction(transaction);
        return transaction;
    }
    
    @Override
    public boolean validate(Account account, double amount) {
        return amount > 0 && account.getBalance() >= amount;
    }
}
