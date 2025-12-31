package com.university.finance.pattern.factory;

import com.university.finance.model.Account;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitaires pour AccountFactory.
 */
public class AccountFactoryTest {
    
    @Before
    public void setUp() {
        AccountFactory.resetCounter();
    }
    
    @Test
    public void testCreateAccount() {
        Account account = AccountFactory.createAccount("testuser", 1000.0);
        
        assertNotNull(account);
        assertEquals("testuser", account.getOwnerUsername());
        assertEquals(1000.0, account.getBalance(), 0.01);
        assertTrue(account.getAccountNumber().startsWith("ACC-"));
    }
    
    @Test
    public void testGenerateUniqueAccountNumbers() {
        Account account1 = AccountFactory.createAccount("user1", 1000.0);
        Account account2 = AccountFactory.createAccount("user2", 500.0);
        
        assertNotEquals(account1.getAccountNumber(), account2.getAccountNumber());
    }
    
    @Test
    public void testCreateAccountWithNumber() {
        Account account = AccountFactory.createAccountWithNumber("CUSTOM-001", "testuser", 1000.0);
        
        assertEquals("CUSTOM-001", account.getAccountNumber());
        assertEquals("testuser", account.getOwnerUsername());
    }
    
    @Test
    public void testResetCounter() {
        Account account1 = AccountFactory.createAccount("user1", 1000.0);
        String firstNumber = account1.getAccountNumber();
        
        AccountFactory.resetCounter();
        
        Account account2 = AccountFactory.createAccount("user2", 500.0);
        assertEquals(firstNumber, account2.getAccountNumber());
    }
}
