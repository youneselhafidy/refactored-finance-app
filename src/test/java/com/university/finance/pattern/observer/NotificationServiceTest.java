package com.university.finance.pattern.observer;

import com.university.finance.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

/**
 * Tests unitaires pour NotificationService.
 */
public class NotificationServiceTest {
    
    private NotificationService notificationService;
    
    @Before
    public void setUp() {
        notificationService = new NotificationService();
    }
    
    @Test
    public void testOnTransactionDeposit() {
        Transaction transaction = new Transaction(
            "ACC-001", "DEPOSIT", 500.0, 1000.0, 1500.0, LocalDateTime.now()
        );
        
        notificationService.onTransaction(transaction);
        
        assertEquals(1, notificationService.getNotifications().size());
        assertTrue(notificationService.getNotifications().get(0).contains("Dépôt"));
    }
    
    @Test
    public void testOnTransactionWithdraw() {
        Transaction transaction = new Transaction(
            "ACC-001", "WITHDRAW", -200.0, 1000.0, 800.0, LocalDateTime.now()
        );
        
        notificationService.onTransaction(transaction);
        
        assertTrue(notificationService.getNotifications().get(0).contains("Retrait"));
    }
    
    @Test
    public void testOnTransactionTransfer() {
        Transaction transaction = new Transaction(
            "ACC-001", "TRANSFER_OUT", -300.0, 1000.0, 700.0, 
            LocalDateTime.now(), "Transfert vers ACC-002"
        );
        
        notificationService.onTransaction(transaction);
        
        assertTrue(notificationService.getNotifications().get(0).contains("Transfert sortant"));
    }
    
    @Test
    public void testGetRecentNotifications() {
        for (int i = 0; i < 5; i++) {
            Transaction transaction = new Transaction(
                "ACC-001", "DEPOSIT", 100.0, 1000.0, 1100.0, LocalDateTime.now()
            );
            notificationService.onTransaction(transaction);
        }
        
        assertEquals(3, notificationService.getRecentNotifications(3).size());
    }
    
    @Test
    public void testClearNotifications() {
        Transaction transaction = new Transaction(
            "ACC-001", "DEPOSIT", 500.0, 1000.0, 1500.0, LocalDateTime.now()
        );
        
        notificationService.onTransaction(transaction);
        assertEquals(1, notificationService.getNotifications().size());
        
        notificationService.clearNotifications();
        assertEquals(0, notificationService.getNotifications().size());
    }
}
