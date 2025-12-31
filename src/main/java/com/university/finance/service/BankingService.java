package com.university.finance.service;

import com.university.finance.model.Account;
import com.university.finance.model.User;
import com.university.finance.pattern.factory.AccountFactory;
import com.university.finance.pattern.factory.UserFactory;
import com.university.finance.pattern.strategy.DepositStrategy;
import com.university.finance.pattern.strategy.TransferStrategy;
import com.university.finance.pattern.strategy.WithdrawStrategy;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * Service principal gérant les opérations bancaires.
 * Suit le principe de responsabilité unique en déléguant les transactions.
 */
public class BankingService {
    private final Map<String, User> users;
    private final Map<String, Account> accounts;
    private final TransactionService transactionService;
    
    // Stratégies réutilisables
    private final DepositStrategy depositStrategy;
    private final WithdrawStrategy withdrawStrategy;
    private final TransferStrategy transferStrategy;
    
    public BankingService(TransactionService transactionService) {
        this.users = new HashMap<>();
        this.accounts = new HashMap<>();
        this.transactionService = transactionService;
        
        this.depositStrategy = new DepositStrategy();
        this.withdrawStrategy = new WithdrawStrategy();
        this.transferStrategy = new TransferStrategy();
    }
    
    /**
     * Crée un nouvel utilisateur avec un compte initial.
     */
    public Account createUser(String username, String password, double initialBalance) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException(Exceptions.USER_ALREADY_EXISTS.toString() + ": " + username);
        }
        
        User user = UserFactory.createUser(username, password);
        Account account = AccountFactory.createAccount(username, initialBalance);
        
        users.put(username, user);
        accounts.put(account.getAccountNumber(), account);
        
        return account;
    }
    
    /**
     * Authentifie un utilisateur.
     */
    public boolean authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.verifyPassword(password)) {
            user.updateLastLogin();
            return true;
        }
        return false;
    }
    
    /**
     * Récupère un compte par son numéro.
     */
    public Optional<Account> getAccount(String accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }
    
    /**
     * Récupère un compte par le nom d'utilisateur.
     */
    public Optional<Account> getAccountByUsername(String username) {
        return accounts.values().stream()
            .filter(account -> account.getOwnerUsername().equals(username))
            .findFirst();
    }
    
    /**
     * Effectue un dépôt sur un compte.
     */
    public void deposit(String accountNumber, double amount) {
        Account account = getAccount(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException(Exceptions.ACCOUNT_NOT_FOUND.toString()));
        
        transactionService.executeTransaction(depositStrategy, account, amount, null);
    }
    
    /**
     * Effectue un retrait sur un compte.
     */
    public void withdraw(String accountNumber, double amount) {
        Account account = getAccount(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException(Exceptions.ACCOUNT_NOT_FOUND.toString()));
        
        transactionService.executeTransaction(withdrawStrategy, account, amount, null);
    }
    
    /**
     * Effectue un transfert entre deux comptes.
     */
    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAccount = getAccount(fromAccountNumber)
            .orElseThrow(() -> new IllegalArgumentException(Exceptions.SOURCE_ACCOUNT_NOT_FOUND.toString()));
        Account toAccount = getAccount(toAccountNumber)
            .orElseThrow(() -> new IllegalArgumentException(Exceptions.TARGET_ACCOUNT_NOT_FOUND.toString()));
        
        transactionService.executeTransaction(transferStrategy, fromAccount, amount, toAccount);
    }
    
    /**
     * Récupère le solde d'un compte.
     */
    public double getBalance(String accountNumber) {
        return getAccount(accountNumber)
            .map(Account::getBalance)
            .orElseThrow(() -> new IllegalArgumentException(Exceptions.ACCOUNT_NOT_FOUND.toString()));
    }

    private enum Exceptions {
        ACCOUNT_NOT_FOUND("Compte non trouvé"),
        SOURCE_ACCOUNT_NOT_FOUND("Compte source non trouvé"),
        TARGET_ACCOUNT_NOT_FOUND("Compte cible non trouvé"),
        USER_ALREADY_EXISTS("L'utilisateur existe déjà"),
        INVALID_CREDENTIALS("Identifiants invalides");
        
        private final String message;
        
        Exceptions(String message) {
            this.message = message;
        }
        
        @Override
        public String toString() {
            return message;
        }
    }
}
