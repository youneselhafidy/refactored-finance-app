package com.university.finance.controller;

import com.university.finance.model.Account;
import com.university.finance.model.User;
import com.university.finance.service.BankingService;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour BankingController
 */
public class BankingControllerTest {
    
    private BankingService bankingService;
    private BankingController controller;
    
    @Before
    public void setUp() {
        bankingService = mock(BankingService.class);
        controller = new BankingController(bankingService);
    }
    
    // ========== Tests de création de compte ==========
    
    @Test
    public void testCreateAccount_Success() {
        User user = new User("john", "pass123");
        Account account = new Account("ACC001", user.getUsername(), 1000.0);
        when(bankingService.createUser("john", "pass123", 1000.0)).thenReturn(account);
        
        String accountNumber = controller.createAccount("john", "pass123", 1000.0);
        
        assertEquals("ACC001", accountNumber);
        verify(bankingService, times(1)).createUser("john", "pass123", 1000.0);
    }
    
    @Test
    public void testCreateAccount_Failure() {
        when(bankingService.createUser(anyString(), anyString(), anyDouble()))
            .thenThrow(new RuntimeException("Erreur"));
        
        String accountNumber = controller.createAccount("john", "pass123", 1000.0);
        
        assertNull(accountNumber);
    }
    
    // ========== Tests d'authentification ==========
    
    @Test
    public void testLogin_Success() {
        when(bankingService.authenticate("john", "pass123")).thenReturn(true);
        
        boolean result = controller.login("john", "pass123");
        
        assertTrue(result);
        assertTrue(controller.isLoggedIn());
        assertEquals("john", controller.getCurrentUsername());
    }
    
    @Test
    public void testLogin_Failure() {
        when(bankingService.authenticate("john", "wrongpass")).thenReturn(false);
        
        boolean result = controller.login("john", "wrongpass");
        
        assertFalse(result);
        assertFalse(controller.isLoggedIn());
        assertNull(controller.getCurrentUsername());
    }
    
    @Test
    public void testLogout() {
        when(bankingService.authenticate("john", "pass123")).thenReturn(true);
        controller.login("john", "pass123");
        
        controller.logout();
        
        assertFalse(controller.isLoggedIn());
        assertNull(controller.getCurrentUsername());
    }
    
    @Test
    public void testIsLoggedIn_InitiallyFalse() {
        assertFalse(controller.isLoggedIn());
    }
    
    // ========== Tests de récupération de compte ==========
    
    @Test
    public void testGetCurrentAccount_LoggedIn() {
        User user = new User("john", "pass123");
        Account account = new Account("ACC001", user.getUsername(), 1000.0);
        
        when(bankingService.authenticate("john", "pass123")).thenReturn(true);
        when(bankingService.getAccountByUsername("john")).thenReturn(Optional.of(account));
        
        controller.login("john", "pass123");
        Account result = controller.getCurrentAccount();
        
        assertNotNull(result);
        assertEquals("ACC001", result.getAccountNumber());
    }
    
    @Test
    public void testGetCurrentAccount_NotLoggedIn() {
        Account result = controller.getCurrentAccount();
        assertNull(result);
    }
    
    @Test
    public void testGetCurrentBalance_LoggedIn() {
        User user = new User("john", "pass123");
        Account account = new Account("ACC001", user.getUsername(), 1500.0);
        
        when(bankingService.authenticate("john", "pass123")).thenReturn(true);
        when(bankingService.getAccountByUsername("john")).thenReturn(Optional.of(account));
        
        controller.login("john", "pass123");
        Double balance = controller.getCurrentBalance();
        
        assertNotNull(balance);
        assertEquals(1500.0, balance, 0.01);
    }
    
    @Test
    public void testGetCurrentBalance_NotLoggedIn() {
        Double balance = controller.getCurrentBalance();
        assertNull(balance);
    }
    
    // ========== Tests de dépôt ==========
    
    @Test
    public void testDeposit_Success() {
        User user = new User("john", "pass123");
        Account account = new Account("ACC001", user.getUsername(), 1000.0);
        
        when(bankingService.authenticate("john", "pass123")).thenReturn(true);
        when(bankingService.getAccountByUsername("john")).thenReturn(Optional.of(account));
        
        controller.login("john", "pass123");
        boolean result = controller.deposit(500.0);
        
        assertTrue(result);
        verify(bankingService, times(1)).deposit("ACC001", 500.0);
    }
    
    @Test
    public void testDeposit_NotLoggedIn() {
        boolean result = controller.deposit(500.0);
        
        assertFalse(result);
        verify(bankingService, never()).deposit(anyString(), anyDouble());
    }
    
    @Test
    public void testDeposit_ServiceException() {
        User user = new User("john", "pass123");
        Account account = new Account("ACC001", user.getUsername(), 1000.0);
        
        when(bankingService.authenticate("john", "pass123")).thenReturn(true);
        when(bankingService.getAccountByUsername("john")).thenReturn(Optional.of(account));
        doThrow(new RuntimeException("Erreur")).when(bankingService).deposit("ACC001", 500.0);
        
        controller.login("john", "pass123");
        boolean result = controller.deposit(500.0);
        
        assertFalse(result);
    }
    
    // ========== Tests de retrait ==========
    
    @Test
    public void testWithdraw_Success() {
        User user = new User("john", "pass123");
        Account account = new Account("ACC001", user.getUsername(), 1000.0);
        
        when(bankingService.authenticate("john", "pass123")).thenReturn(true);
        when(bankingService.getAccountByUsername("john")).thenReturn(Optional.of(account));
        
        controller.login("john", "pass123");
        boolean result = controller.withdraw(300.0);
        
        assertTrue(result);
        verify(bankingService, times(1)).withdraw("ACC001", 300.0);
    }
    
    @Test
    public void testWithdraw_NotLoggedIn() {
        boolean result = controller.withdraw(300.0);
        
        assertFalse(result);
        verify(bankingService, never()).withdraw(anyString(), anyDouble());
    }
    
    @Test
    public void testWithdraw_InsufficientFunds() {
        User user = new User("john", "pass123");
        Account account = new Account("ACC001", user.getUsername(), 100.0);
        
        when(bankingService.authenticate("john", "pass123")).thenReturn(true);
        when(bankingService.getAccountByUsername("john")).thenReturn(Optional.of(account));
        doThrow(new IllegalStateException("Solde insuffisant"))
            .when(bankingService).withdraw("ACC001", 500.0);
        
        controller.login("john", "pass123");
        boolean result = controller.withdraw(500.0);
        
        assertFalse(result);
    }
    
    // ========== Tests de transfert ==========
    
    @Test
    public void testTransfer_Success() {
        User user1 = new User("john", "pass123");
        User user2 = new User("jane", "pass456");
        Account account1 = new Account("ACC001", user1.getUsername(), 1000.0);
        Account account2 = new Account("ACC002", user2.getUsername(), 500.0);
        
        when(bankingService.authenticate("john", "pass123")).thenReturn(true);
        when(bankingService.getAccountByUsername("john")).thenReturn(Optional.of(account1));
        when(bankingService.getAccountByUsername("jane")).thenReturn(Optional.of(account2));
        
        controller.login("john", "pass123");
        boolean result = controller.transfer("jane", 200.0);
        
        assertTrue(result);
        verify(bankingService, times(1)).transfer("ACC001", "ACC002", 200.0);
    }
    
    @Test
    public void testTransfer_NotLoggedIn() {
        boolean result = controller.transfer("jane", 200.0);
        
        assertFalse(result);
        verify(bankingService, never()).transfer(anyString(), anyString(), anyDouble());
    }
    
    @Test
    public void testTransfer_RecipientNotFound() {
        User user = new User("john", "pass123");
        Account account = new Account("ACC001", user.getUsername(), 1000.0);
        
        when(bankingService.authenticate("john", "pass123")).thenReturn(true);
        when(bankingService.getAccountByUsername("john")).thenReturn(Optional.of(account));
        when(bankingService.getAccountByUsername("ghost")).thenReturn(Optional.empty());
        
        controller.login("john", "pass123");
        boolean result = controller.transfer("ghost", 200.0);
        
        assertFalse(result);
        verify(bankingService, never()).transfer(anyString(), anyString(), anyDouble());
    }
    
    @Test
    public void testTransfer_ServiceException() {
        User user1 = new User("john", "pass123");
        User user2 = new User("jane", "pass456");
        Account account1 = new Account("ACC001", user1.getUsername(), 100.0);
        Account account2 = new Account("ACC002", user2.getUsername(), 500.0);
        
        when(bankingService.authenticate("john", "pass123")).thenReturn(true);
        when(bankingService.getAccountByUsername("john")).thenReturn(Optional.of(account1));
        when(bankingService.getAccountByUsername("jane")).thenReturn(Optional.of(account2));
        doThrow(new IllegalStateException("Solde insuffisant"))
            .when(bankingService).transfer("ACC001", "ACC002", 200.0);
        
        controller.login("john", "pass123");
        boolean result = controller.transfer("jane", 200.0);
        
        assertFalse(result);
    }
    
    // ========== Tests d'historique de transactions ==========
    
    @Test
    public void testGetTransactionHistory_LoggedIn() {
        User user = new User("john", "pass123");
        Account account = new Account("ACC001", user.getUsername(), 1000.0);
        
        when(bankingService.authenticate("john", "pass123")).thenReturn(true);
        when(bankingService.getAccountByUsername("john")).thenReturn(Optional.of(account));
        
        controller.login("john", "pass123");
        var history = controller.getTransactionHistory();
        
        assertNotNull(history);
        assertEquals(0, history.size()); // Nouveau compte sans transactions
    }
    
    @Test
    public void testGetTransactionHistory_NotLoggedIn() {
        var history = controller.getTransactionHistory();
        assertNull(history);
    }
    
    // ========== Tests de statistiques ==========
    
    @Test
    public void testGetTotalTransactionCount_NotLoggedIn() {
        assertEquals(0, controller.getTotalTransactionCount());
    }
    
    @Test
    public void testGetStatistics_NotLoggedIn() {
        assertEquals(0, controller.getTotalTransactionCount());
        assertEquals(0.0, controller.getTotalDeposits(), 0.01);
        assertEquals(0.0, controller.getTotalWithdrawals(), 0.01);
        assertEquals(0.0, controller.getTotalTransfers(), 0.01);
    }
    
    // ========== Tests de vérification d'utilisateur ==========
    
    @Test
    public void testUserExists_True() {
        User user = new User("john", "pass123");
        Account account = new Account("ACC001", user.getUsername(), 1000.0);
        
        when(bankingService.getAccountByUsername("john")).thenReturn(Optional.of(account));
        
        boolean exists = controller.userExists("john");
        assertTrue(exists);
    }
    
    @Test
    public void testUserExists_False() {
        when(bankingService.getAccountByUsername("ghost")).thenReturn(Optional.empty());
        
        boolean exists = controller.userExists("ghost");
        assertFalse(exists);
    }
}
