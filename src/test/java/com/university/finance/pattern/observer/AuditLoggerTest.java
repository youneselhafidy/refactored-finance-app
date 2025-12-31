package com.university.finance.pattern.observer;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

/**
 * Tests unitaires pour AuditLogger.
 */
public class AuditLoggerTest {
    
    private AuditLogger auditLogger;
    private Account account;
    
    @Before
    public void setUp() {
        auditLogger = new AuditLogger();
        account = new Account("ACC-001", "testuser", 1000.0);
    }
    
    @Test
    public void testOnTransaction() {
        Transaction transaction = new Transaction(
            account.getAccountNumber(), "DEPOSIT", 500.0, 1000.0, 1500.0, LocalDateTime.now()
        );
        
        auditLogger.onTransaction(transaction);
        
        assertEquals(1, auditLogger.getAuditLog().size());
    }
    
    @Test
    public void testGetAuditLog() {
        Transaction transaction1 = new Transaction(
            account.getAccountNumber(), "DEPOSIT", 500.0, 1000.0, 1500.0, LocalDateTime.now()
        );
        Transaction transaction2 = new Transaction(
            account.getAccountNumber(), "WITHDRAW", -200.0, 1500.0, 1300.0, LocalDateTime.now()
        );
        
        auditLogger.onTransaction(transaction1);
        auditLogger.onTransaction(transaction2);
        
        assertEquals(2, auditLogger.getAuditLog().size());
    }
    
    @Test
    public void testGetRecentAuditLog() {
        for (int i = 0; i < 5; i++) {
            Transaction transaction = new Transaction(
                account.getAccountNumber(), "DEPOSIT", 100.0, 1000.0, 1100.0, LocalDateTime.now()
            );
            auditLogger.onTransaction(transaction);
        }
        
        assertEquals(3, auditLogger.getRecentAuditLog(3).size());
        assertEquals(5, auditLogger.getRecentAuditLog(10).size());
    }
    
    @Test
    public void testClearLog() {
        Transaction transaction = new Transaction(
            account.getAccountNumber(), "DEPOSIT", 500.0, 1000.0, 1500.0, LocalDateTime.now()
        );
        
        auditLogger.onTransaction(transaction);
        assertEquals(1, auditLogger.getAuditLog().size());
        
        auditLogger.clearLog();
        assertEquals(0, auditLogger.getAuditLog().size());
    }
}
