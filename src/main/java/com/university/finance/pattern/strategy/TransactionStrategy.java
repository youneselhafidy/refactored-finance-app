package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;

/**
 * Strategy Pattern - Interface définissant le contrat pour toutes les stratégies de transaction.
 * Permet de définir différentes stratégies de transaction de manière polymorphique.
 */
public interface TransactionStrategy {
    /**
     * Exécute une transaction selon la stratégie implémentée.
     * 
     * @param account Le compte source de la transaction
     * @param amount Le montant de la transaction
     * @param targetAccount Le compte cible (peut être null pour dépôt/retrait)
     * @return L'objet Transaction créé
     * @throws IllegalArgumentException Si la transaction n'est pas valide
     */
    Transaction execute(Account account, double amount, Account targetAccount);
    
    /**
     * Valide si la transaction peut être exécutée.
     * 
     * @param account Le compte source
     * @param amount Le montant
     * @return true si la transaction est valide
     */
    boolean validate(Account account, double amount);
}
