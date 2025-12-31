package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitaires pour TransferStrategy.
 */
public class TransferStrategyTest {
    
    private TransferStrategy strategy;
    private Account fromAccount;
    private Account toAccount;
    
    @Before
    public void setUp() {
        strategy = new TransferStrategy();
        fromAccount = new Account("ACC-001", "user1", 1000.0);
        toAccount = new Account("ACC-002", "user2", 500.0);
    }
    
    @Test
    public void testTransferValid() {
        Transaction transaction = strategy.execute(fromAccount, 300.0, toAccount);
        
        assertEquals(700.0, fromAccount.getBalance(), 0.01);
        assertEquals(800.0, toAccount.getBalance(), 0.01);
        assertEquals("TRANSFER_OUT", transaction.getType());
        assertEquals(-300.0, transaction.getAmount(), 0.01);
    }
    
    @Test
    public void testTransferCreatesTransactionForBothAccounts() {
        strategy.execute(fromAccount, 300.0, toAccount);
        
        assertEquals(1, fromAccount.getTransactions().size());
        assertEquals(1, toAccount.getTransactions().size());
        
        Transaction fromTransaction = fromAccount.getTransactions().get(0);
        Transaction toTransaction = toAccount.getTransactions().get(0);
        
        assertEquals("TRANSFER_OUT", fromTransaction.getType());
        assertEquals("TRANSFER_IN", toTransaction.getType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTransferWithoutTargetAccount() {
        strategy.execute(fromAccount, 300.0, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTransferInsufficientBalance() {
        strategy.execute(fromAccount, 1500.0, toAccount);
    }
    
    @Test
    public void testValidateTransfer() {
        assertTrue(strategy.validate(fromAccount, 500.0));
        assertFalse(strategy.validate(fromAccount, 1500.0));
    }
}
