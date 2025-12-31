package com.university.finance;

import com.university.finance.controller.BankingController;
import com.university.finance.pattern.observer.AuditLogger;
import com.university.finance.pattern.observer.NotificationService;
import com.university.finance.service.BankingService;
import com.university.finance.service.TransactionService;
import java.util.Scanner;

/**
 * Application principale du système bancaire refactorisé.
 * Démontre l'utilisation des design patterns implémentés.
 * Utilise BankingController pour découpler la logique métier.
 */
public class MainApp {
    private final BankingController controller;
    private final AuditLogger auditLogger;
    private final Scanner scanner;
    
    public MainApp() {
        // Initialisation des services avec injection de dépendances
        TransactionService transactionService = new TransactionService();
        
        // Configuration des observateurs (Observer Pattern)
        this.auditLogger = new AuditLogger();
        NotificationService notificationService = new NotificationService();
        transactionService.addObserver(auditLogger);
        transactionService.addObserver(notificationService);
        
        BankingService bankingService = new BankingService(transactionService);
        this.controller = new BankingController(bankingService);
        this.scanner = new Scanner(System.in);
        
        // Initialisation des données de test
        initializeTestData(bankingService);
    }
    
    private void initializeTestData(BankingService bankingService) {
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
                        handleCreateAccount();
                        break;
                    case 2:
                        handleLogin();
                        break;
                    case 3:
                        handleViewBalance();
                        break;
                    case 4:
                        handleDeposit();
                        break;
                    case 5:
                        handleWithdraw();
                        break;
                    case 6:
                        handleTransfer();
                        break;
                    case 7:
                        handleViewHistory();
                        break;
                    case 8:
                        handleViewStatistics();
                        break;
                    case 9:
                        handleLogout();
                        break;
                    case 10:
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
        System.out.println("\n=== Menu Principal ===");
        if (!controller.isLoggedIn()) {
            System.out.println("1. Créer un compte");
            System.out.println("2. Se connecter");
        } else {
            System.out.println("Connecté: " + controller.getCurrentUsername());
            System.out.println("3. Afficher solde");
            System.out.println("4. Déposer argent");
            System.out.println("5. Retirer argent");
            System.out.println("6. Transfert");
            System.out.println("7. Historique des transactions");
            System.out.println("8. Statistiques");
            System.out.println("9. Se déconnecter");
        }
        System.out.println("10. Voir l'audit complet");
        System.out.println("0. Quitter");
        System.out.print("Votre choix: ");
    }
    
    private void handleCreateAccount() {
        System.out.print("\nNom d'utilisateur: ");
        String username = scanner.next();
        System.out.print("Mot de passe: ");
        String password = scanner.next();
        System.out.print("Dépôt initial: ");
        double initialDeposit = getDoubleInput();
        
        String accountNumber = controller.createAccount(username, password, initialDeposit);
        if (accountNumber != null) {
            System.out.println("✓ Compte créé avec succès! Numéro: " + accountNumber);
        } else {
            System.out.println("✗ Erreur lors de la création du compte.");
        }
    }
    
    private void handleLogin() {
        System.out.print("\nNom d'utilisateur: ");
        String username = scanner.next();
        System.out.print("Mot de passe: ");
        String password = scanner.next();
        
        if (controller.login(username, password)) {
            System.out.println("✓ Connexion réussie!");
        } else {
            System.out.println("✗ Identifiants invalides.");
        }
    }
    
    private void handleLogout() {
        controller.logout();
        System.out.println("✓ Déconnexion réussie!");
    }
    
    private void handleViewBalance() {
        if (!controller.isLoggedIn()) {
            System.out.println("✗ Vous devez être connecté.");
            return;
        }
        
        Double balance = controller.getCurrentBalance();
        if (balance != null) {
            System.out.printf("Solde actuel: %.2f€%n", balance);
        } else {
            System.out.println("✗ Impossible de récupérer le solde.");
        }
    }
    
    private void handleDeposit() {
        if (!controller.isLoggedIn()) {
            System.out.println("✗ Vous devez être connecté.");
            return;
        }
        
        System.out.print("Montant à déposer: ");
        double amount = getDoubleInput();
        
        if (controller.deposit(amount)) {
            System.out.println("✓ Dépôt effectué avec succès!");
            System.out.printf("Nouveau solde: %.2f€%n", controller.getCurrentBalance());
        } else {
            System.out.println("✗ Échec du dépôt.");
        }
    }
    
    private void handleWithdraw() {
        if (!controller.isLoggedIn()) {
            System.out.println("✗ Vous devez être connecté.");
            return;
        }
        
        System.out.print("Montant à retirer: ");
        double amount = getDoubleInput();
        
        if (controller.withdraw(amount)) {
            System.out.println("✓ Retrait effectué avec succès!");
            System.out.printf("Nouveau solde: %.2f€%n", controller.getCurrentBalance());
        } else {
            System.out.println("✗ Échec du retrait (solde insuffisant?).");
        }
    }
    
    private void handleTransfer() {
        if (!controller.isLoggedIn()) {
            System.out.println("✗ Vous devez être connecté.");
            return;
        }
        
        System.out.print("Vers l'utilisateur: ");
        String toUsername = scanner.next();
        
        if (!controller.userExists(toUsername)) {
            System.out.println("✗ Utilisateur destinataire introuvable.");
            return;
        }
        
        System.out.print("Montant: ");
        double amount = getDoubleInput();
        
        if (controller.transfer(toUsername, amount)) {
            System.out.println("✓ Transfert effectué avec succès!");
            System.out.printf("Nouveau solde: %.2f€%n", controller.getCurrentBalance());
        } else {
            System.out.println("✗ Échec du transfert (solde insuffisant?).");
        }
    }
    
    private void handleViewHistory() {
        if (!controller.isLoggedIn()) {
            System.out.println("✗ Vous devez être connecté.");
            return;
        }
        
        var transactions = controller.getTransactionHistory();
        if (transactions == null || transactions.isEmpty()) {
            System.out.println("Aucune transaction.");
        } else {
            System.out.println("\n=== Historique des transactions ===");
            transactions.forEach(System.out::println);
        }
    }
    
    private void handleViewStatistics() {
        if (!controller.isLoggedIn()) {
            System.out.println("✗ Vous devez être connecté.");
            return;
        }
        
        System.out.println("\n=== Statistiques ===");
        System.out.printf("Nombre de transactions: %d%n", controller.getTotalTransactionCount());
        System.out.printf("Total dépôts: %.2f€%n", controller.getTotalDeposits());
        System.out.printf("Total retraits: %.2f€%n", controller.getTotalWithdrawals());
        System.out.printf("Total transferts: %.2f€%n", controller.getTotalTransfers());
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
