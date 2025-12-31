package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitaires pour DepositStrategy.
 */
public class DepositStrategyTest {
    
    private DepositStrategy strategy;
    private Account account;
    
    @Before
    public void setUp() {
        strategy = new DepositStrategy();
        account = new Account("ACC-001", "testuser", 1000.0);
    }
    
    @Test
    public void testDepositValid() {
        Transaction transaction = strategy.execute(account, 500.0, null);
        
        assertEquals(1500.0, account.getBalance(), 0.01);
        assertEquals("DEPOSIT", transaction.getType());
        assertEquals(500.0, transaction.getAmount(), 0.01);
        assertEquals(1000.0, transaction.getBalanceBefore(), 0.01);
        assertEquals(1500.0, transaction.getBalanceAfter(), 0.01);
    }
    
    @Test
    public void testValidatePositiveAmount() {
        assertTrue(strategy.validate(account, 100.0));
        assertTrue(strategy.validate(account, 0.01));
    }
    
    @Test
    public void testValidateNegativeAmount() {
        assertFalse(strategy.validate(account, -100.0));
        assertFalse(strategy.validate(account, 0.0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDepositNegativeAmount() {
        strategy.execute(account, -100.0, null);
    }
}
