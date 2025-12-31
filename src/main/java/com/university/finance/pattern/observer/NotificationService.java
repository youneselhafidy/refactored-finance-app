package com.university.finance.pattern.observer;

import com.university.finance.model.Transaction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Observer Pattern - Service de notification pour les transactions.
 * Envoie des notifications aux utilisateurs concernés.
 */
public class NotificationService implements TransactionObserver {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final List<String> notifications;
    
    public NotificationService() {
        this.notifications = new ArrayList<>();
    }
    
    @Override
    public void onTransaction(Transaction transaction) {
        String notification = createNotification(transaction);
        notifications.add(notification);
        // En production, on enverrait un email, SMS ou notification push
        logger.info("[NOTIFICATION] {}", notification);
    }
    
    /**
     * Crée un message de notification personnalisé selon le type de transaction.
     */
    private String createNotification(Transaction transaction) {
        String type = transaction.getType();
        double amount = Math.abs(transaction.getAmount());
        String accountNumber = transaction.getAccountNumber();
        
        switch (type) {
            case "DEPOSIT":
                return String.format("Dépôt de %.2f€ effectué sur le compte %s", 
                    amount, accountNumber);
            case "WITHDRAW":
                return String.format("Retrait de %.2f€ effectué sur le compte %s", 
                    amount, accountNumber);
            case "TRANSFER_OUT":
                return String.format("Transfert sortant de %.2f€ depuis le compte %s", 
                    amount, accountNumber);
            case "TRANSFER_IN":
                return String.format("Transfert entrant de %.2f€ vers le compte %s", 
                    amount, accountNumber);
            default:
                return String.format("Transaction de %.2f€ sur le compte %s", 
                    amount, accountNumber);
        }
    }
    
    /**
     * Récupère toutes les notifications.
     */
    public List<String> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }
    
    /**
     * Récupère les N dernières notifications.
     */
    public List<String> getRecentNotifications(int count) {
        int size = notifications.size();
        int fromIndex = Math.max(0, size - count);
        return Collections.unmodifiableList(notifications.subList(fromIndex, size));
    }
    
    /**
     * Efface les notifications (utile pour les tests).
     */
    public void clearNotifications() {
        notifications.clear();
    }
}
