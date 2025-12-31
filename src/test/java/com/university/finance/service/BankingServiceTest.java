package com.university.finance.service;

import com.university.finance.model.Account;
import com.university.finance.pattern.factory.AccountFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitaires pour BankingService.
 */
public class BankingServiceTest {
    
    private BankingService bankingService;
    private TransactionService transactionService;
    
    @Before
    public void setUp() {
        AccountFactory.resetCounter();
        transactionService = new TransactionService();
        bankingService = new BankingService(transactionService);
    }
    
    @Test
    public void testCreateUser() {
        Account account = bankingService.createUser("testuser", "password123", 1000.0);
        
        assertNotNull(account);
        assertEquals("testuser", account.getOwnerUsername());
        assertEquals(1000.0, account.getBalance(), 0.01);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateDuplicateUser() {
        bankingService.createUser("testuser", "password123", 1000.0);
        bankingService.createUser("testuser", "password456", 500.0);
    }
    
    @Test
    public void testAuthenticate() {
        bankingService.createUser("testuser", "password123", 1000.0);
        
        assertTrue(bankingService.authenticate("testuser", "password123"));
        assertFalse(bankingService.authenticate("testuser", "wrongpassword"));
        assertFalse(bankingService.authenticate("unknownuser", "password123"));
    }
    
    @Test
    public void testDeposit() {
        Account account = bankingService.createUser("testuser", "password123", 1000.0);
        
        bankingService.deposit(account.getAccountNumber(), 500.0);
        
        assertEquals(1500.0, account.getBalance(), 0.01);
    }
    
    @Test
    public void testWithdraw() {
        Account account = bankingService.createUser("testuser", "password123", 1000.0);
        
        bankingService.withdraw(account.getAccountNumber(), 300.0);
        
        assertEquals(700.0, account.getBalance(), 0.01);
    }
    
    @Test
    public void testTransfer() {
        Account account1 = bankingService.createUser("user1", "password1", 1000.0);
        Account account2 = bankingService.createUser("user2", "password2", 500.0);
        
        bankingService.transfer(account1.getAccountNumber(), account2.getAccountNumber(), 300.0);
        
        assertEquals(700.0, account1.getBalance(), 0.01);
        assertEquals(800.0, account2.getBalance(), 0.01);
    }
    
    @Test
    public void testGetBalance() {
        Account account = bankingService.createUser("testuser", "password123", 1000.0);
        
        double balance = bankingService.getBalance(account.getAccountNumber());
        
        assertEquals(1000.0, balance, 0.01);
    }
    
    @Test
    public void testGetAccountByUsername() {
        Account account = bankingService.createUser("testuser", "password123", 1000.0);
        
        Account foundAccount = bankingService.getAccountByUsername("testuser").orElse(null);
        
        assertNotNull(foundAccount);
        assertEquals(account.getAccountNumber(), foundAccount.getAccountNumber());
    }
}
