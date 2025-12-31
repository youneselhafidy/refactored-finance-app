package com.university.finance.pattern.observer;

import com.university.finance.model.Transaction;

/**
 * Observer Pattern - Interface pour les observateurs de transactions.
 * Permet de notifier plusieurs composants lors d'une transaction.
 */
public interface TransactionObserver {
    /**
     * Méthode appelée lors d'une nouvelle transaction.
     * 
     * @param transaction La transaction effectuée
     */
    void onTransaction(Transaction transaction);
}
