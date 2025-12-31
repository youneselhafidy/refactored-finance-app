package com.university.finance;

import com.university.finance.model.Account;
import com.university.finance.pattern.observer.AuditLogger;
import com.university.finance.pattern.observer.NotificationService;
import com.university.finance.service.BankingService;
import com.university.finance.service.TransactionService;
import java.util.Scanner;

/**
 * Application principale du système bancaire refactorisé.
 * Démontre l'utilisation des design patterns implémentés.
 */
public class MainApp {
    private final BankingService bankingService;
    private final AuditLogger auditLogger;
    private final NotificationService notificationService;
    private final Scanner scanner;
    
    public MainApp() {
        // Initialisation des services avec injection de dépendances
        TransactionService transactionService = new TransactionService();
        
        // Configuration des observateurs (Observer Pattern)
        this.auditLogger = new AuditLogger();
        this.notificationService = new NotificationService();
        transactionService.addObserver(auditLogger);
        transactionService.addObserver(notificationService);
        
        this.bankingService = new BankingService(transactionService);
        this.scanner = new Scanner(System.in);
        
        // Initialisation des données de test
        initializeTestData();
    }
    
    private void initializeTestData() {
        bankingService.createUser("user1", "password1", 1000.0);
        bankingService.createUser("user2", "password2", 500.0);
    }
    
    public void run() {
        System.out.println("=== Système Bancaire Refactorisé ===");
        System.out.println("Design Patterns: Strategy, Factory, Observer");
        System.out.println("=======================================\n");
        
        boolean running = true;
        
        while (running) {
            displayMenu();
            int choice = getIntInput();
            
            try {
                switch (choice) {
                    case 1:
                        handleViewBalance();
                        break;
                    case 2:
                        handleDeposit();
                        break;
                    case 3:
                        handleWithdraw();
                        break;
                    case 4:
                        handleTransfer();
                        break;
                    case 5:
                        handleViewHistory();
                        break;
                    case 6:
                        handleAddUser();
                        break;
                    case 7:
                        handleViewAuditLog();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Au revoir!");
                        break;
                    default:
                        System.out.println("Choix invalide!");
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
            
            System.out.println();
        }
        
        scanner.close();
    }
    
    private void displayMenu() {
        System.out.println("=== Menu Principal ===");
        System.out.println("1. Afficher solde");
        System.out.println("2. Déposer argent");
        System.out.println("3. Retirer argent");
        System.out.println("4. Transfert");
        System.out.println("5. Historique des transactions");
        System.out.println("6. Ajouter utilisateur");
        System.out.println("7. Voir l'audit complet");
        System.out.println("0. Quitter");
        System.out.print("Votre choix: ");
    }
    
    private void handleViewBalance() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.next();
        
        Account account = bankingService.getAccountByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        
        System.out.printf("Solde du compte %s: %.2f€%n", 
            account.getAccountNumber(), account.getBalance());
    }
    
    private void handleDeposit() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.next();
        System.out.print("Montant: ");
        double amount = getDoubleInput();
        
        Account account = bankingService.getAccountByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        
        bankingService.deposit(account.getAccountNumber(), amount);
        System.out.println("Dépôt effectué avec succès!");
    }
    
    private void handleWithdraw() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.next();
        System.out.print("Montant: ");
        double amount = getDoubleInput();
        
        Account account = bankingService.getAccountByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        
        bankingService.withdraw(account.getAccountNumber(), amount);
        System.out.println("Retrait effectué avec succès!");
    }
    
    private void handleTransfer() {
        System.out.print("De l'utilisateur: ");
        String fromUsername = scanner.next();
        System.out.print("Vers l'utilisateur: ");
        String toUsername = scanner.next();
        System.out.print("Montant: ");
        double amount = getDoubleInput();
        
        Account fromAccount = bankingService.getAccountByUsername(fromUsername)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur source non trouvé"));
        Account toAccount = bankingService.getAccountByUsername(toUsername)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur cible non trouvé"));
        
        bankingService.transfer(fromAccount.getAccountNumber(), 
                               toAccount.getAccountNumber(), amount);
        System.out.println("Transfert effectué avec succès!");
    }
    
    private void handleViewHistory() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.next();
        
        Account account = bankingService.getAccountByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        
        System.out.println("\n=== Historique des transactions ===");
        account.getTransactions().forEach(System.out::println);
    }
    
    private void handleAddUser() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.next();
        System.out.print("Mot de passe: ");
        String password = scanner.next();
        System.out.print("Dépôt initial: ");
        double initialDeposit = getDoubleInput();
        
        Account account = bankingService.createUser(username, password, initialDeposit);
        System.out.printf("Utilisateur créé avec succès! Numéro de compte: %s%n", 
            account.getAccountNumber());
    }
    
    private void handleViewAuditLog() {
        System.out.println("\n=== Log d'Audit Complet ===");
        auditLogger.getAuditLog().forEach(System.out::println);
    }
    
    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Veuillez entrer un nombre: ");
        }
        return scanner.nextInt();
    }
    
    private double getDoubleInput() {
        while (!scanner.hasNextDouble()) {
            scanner.next();
            System.out.print("Veuillez entrer un montant valide: ");
        }
        return scanner.nextDouble();
    }
    
    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.run();
    }
}
