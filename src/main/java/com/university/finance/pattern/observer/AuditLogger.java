package com.university.finance.pattern.observer;

import com.university.finance.model.Transaction;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Observer Pattern - Observateur qui enregistre un audit de toutes les transactions.
 * Suit le principe SRP en se concentrant uniquement sur l'audit.
 */
public class AuditLogger implements TransactionObserver {
    private final List<String> auditLog;
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public AuditLogger() {
        this.auditLog = new ArrayList<>();
    }
    
    @Override
    public void onTransaction(Transaction transaction) {
        String logEntry = formatLogEntry(transaction);
        auditLog.add(logEntry);
        // En production, on écrirait dans un fichier ou une base de données
        System.out.println("[AUDIT] " + logEntry);
    }
    
    /**
     * Formate une entrée de log pour l'audit.
     */
    private String formatLogEntry(Transaction transaction) {
        return String.format("[%s] Compte: %s | Type: %s | Montant: %.2f€ | Solde: %.2f€ → %.2f€",
            transaction.getTimestamp().format(FORMATTER),
            transaction.getAccountNumber(),
            transaction.getType(),
            transaction.getAmount(),
            transaction.getBalanceBefore(),
            transaction.getBalanceAfter()
        );
    }
    
    /**
     * Récupère l'historique complet de l'audit.
     */
    public List<String> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
    }
    
    /**
     * Récupère les N dernières entrées d'audit.
     */
    public List<String> getRecentAuditLog(int count) {
        int size = auditLog.size();
        int fromIndex = Math.max(0, size - count);
        return Collections.unmodifiableList(auditLog.subList(fromIndex, size));
    }
    
    /**
     * Efface l'historique d'audit (utile pour les tests).
     */
    public void clearLog() {
        auditLog.clear();
    }
}
