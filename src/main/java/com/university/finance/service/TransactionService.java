package com.university.finance.service;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.pattern.observer.TransactionObserver;
import com.university.finance.pattern.strategy.TransactionStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * Service gérant l'exécution des transactions.
 * Utilise le pattern Strategy pour les différents types de transactions
 * et notifie les observateurs après chaque transaction.
 */
public class TransactionService {
    private final List<TransactionObserver> observers;
    
    public TransactionService() {
        this.observers = new ArrayList<>();
    }
    
    /**
     * Ajoute un observateur pour recevoir les notifications de transactions.
     */
    public void addObserver(TransactionObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * Retire un observateur.
     */
    public void removeObserver(TransactionObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Exécute une transaction en utilisant la stratégie fournie.
     * 
     * @param strategy La stratégie de transaction à utiliser
     * @param account Le compte source
     * @param amount Le montant
     * @param targetAccount Le compte cible (peut être null)
     * @return La transaction créée
     */
    public Transaction executeTransaction(TransactionStrategy strategy,
                                         Account account,
                                         double amount,
                                         Account targetAccount) {
        Transaction transaction = strategy.execute(account, amount, targetAccount);
        notifyObservers(transaction);
        
        // Si c'est un transfert, notifier aussi pour le compte cible
        if (targetAccount != null && !targetAccount.getTransactions().isEmpty()) {
            List<Transaction> targetTransactions = targetAccount.getTransactions();
            Transaction targetTransaction = targetTransactions.get(targetTransactions.size() - 1);
            notifyObservers(targetTransaction);
        }
        
        return transaction;
    }
    
    /**
     * Notifie tous les observateurs d'une nouvelle transaction.
     */
    private void notifyObservers(Transaction transaction) {
        for (TransactionObserver observer : observers) {
            observer.onTransaction(transaction);
        }
    }
}
