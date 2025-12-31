package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitaires pour WithdrawStrategy.
 */
public class WithdrawStrategyTest {
    
    private WithdrawStrategy strategy;
    private Account account;
    
    @Before
    public void setUp() {
        strategy = new WithdrawStrategy();
        account = new Account("ACC-001", "testuser", 1000.0);
    }
    
    @Test
    public void testWithdrawValid() {
        Transaction transaction = strategy.execute(account, 300.0, null);
        
        assertEquals(700.0, account.getBalance(), 0.01);
        assertEquals("WITHDRAW", transaction.getType());
        assertEquals(-300.0, transaction.getAmount(), 0.01);
        assertEquals(1000.0, transaction.getBalanceBefore(), 0.01);
        assertEquals(700.0, transaction.getBalanceAfter(), 0.01);
    }
    
    @Test
    public void testValidateSufficientBalance() {
        assertTrue(strategy.validate(account, 500.0));
        assertTrue(strategy.validate(account, 1000.0));
    }
    
    @Test
    public void testValidateInsufficientBalance() {
        assertFalse(strategy.validate(account, 1500.0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawInsufficientBalance() {
        strategy.execute(account, 1500.0, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawNegativeAmount() {
        strategy.execute(account, -100.0, null);
    }
}
