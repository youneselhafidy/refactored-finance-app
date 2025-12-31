package com.university.finance.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitaires pour la classe Account.
 */
public class AccountTest {
    
    @Test
    public void testAccountCreation() {
        Account account = new Account("ACC-001", "testuser", 1000.0);
        
        assertEquals("ACC-001", account.getAccountNumber());
        assertEquals("testuser", account.getOwnerUsername());
        assertEquals(1000.0, account.getBalance(), 0.01);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAccountCreationWithNegativeBalance() {
        new Account("ACC-002", "testuser", -100.0);
    }
    
    @Test
    public void testSetBalance() {
        Account account = new Account("ACC-003", "testuser", 1000.0);
        account.setBalance(1500.0);
        
        assertEquals(1500.0, account.getBalance(), 0.01);
    }
    
    @Test
    public void testTransactionHistory() {
        Account account = new Account("ACC-004", "testuser", 1000.0);
        assertTrue(account.getTransactions().isEmpty());
    }
    
    @Test
    public void testToString() {
        Account account = new Account("ACC-005", "testuser", 1000.0);
        String result = account.toString();
        
        assertTrue(result.contains("ACC-005"));
        assertTrue(result.contains("testuser"));
        assertTrue(result.contains("1000"));
    }
}
