# Architecture avec Contrôleur - Documentation

## 📋 Vue d'ensemble

Ce projet utilise une **architecture en couches avec contrôleur** pour découpler la logique métier de l'interface utilisateur. Cette approche améliore la testabilité, la maintenabilité et facilite l'intégration avec différents types d'interfaces.

## 🏗️ Structure de l'architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      MainApp (UI Layer)                      │
│                    Non testé - Interface console             │
│              (Gestion des entrées/sorties utilisateur)       │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│              BankingController (Controller Layer)            │
│        Testé à 80% - 26 tests - Interface métier            │
│    (Gestion de la session, coordination des opérations)     │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│            BankingService (Business Logic Layer)             │
│              Testé à 83% - Tests d'intégration              │
│         (Logique métier, gestion des comptes/users)         │
└────────────────────────┬────────────────────────────────────┘
                         │
           ┌─────────────┼─────────────┐
           ▼             ▼             ▼
    ┌───────────┐  ┌──────────┐  ┌──────────┐
    │ Strategy  │  │ Factory  │  │ Observer │
    │  Pattern  │  │  Pattern │  │  Pattern │
    │   100%    │  │   94%    │  │   87%    │
    └───────────┘  └──────────┘  └──────────┘
```

## 🎯 Rôle du BankingController

### Responsabilités

Le contrôleur **BankingController** agit comme une façade simplifiée qui :

1. **Gère la session utilisateur**
   - Authentification (login/logout)
   - Maintien de l'état de connexion
   - Récupération de l'utilisateur courant

2. **Simplifie les opérations métier**
   - Pas besoin de passer l'accountNumber à chaque fois
   - Le contrôleur le récupère automatiquement pour l'utilisateur connecté
   - Gestion des erreurs transparente (retourne true/false)

3. **Fournit des méthodes de haut niveau**
   - Statistiques agrégées (totalDeposits, totalWithdrawals, etc.)
   - Vérification d'existence d'utilisateurs
   - Historique des transactions

### Avantages de cette approche

✅ **Testabilité**
- Le contrôleur est entièrement testé avec Mockito (26 tests)
- Pas besoin de tester MainApp (simple interface console)
- Tests unitaires rapides et isolés

✅ **Découplage**
- MainApp ne connaît que le contrôleur
- Facile de remplacer MainApp par une interface web, mobile, etc.
- Le contrôleur peut être réutilisé dans différents contextes

✅ **Simplicité**
- API du contrôleur beaucoup plus simple que BankingService
- Gestion automatique du contexte utilisateur
- Moins d'erreurs de programmation

✅ **Maintenabilité**
- Changements dans MainApp n'affectent pas la logique métier
- Changements dans BankingService transparents pour MainApp
- Code plus facile à comprendre et modifier

## 📝 Exemples de code

### Avant (sans contrôleur)

```java
// Dans MainApp - code complexe et répétitif
System.out.print("Nom d'utilisateur: ");
String username = scanner.next();
System.out.print("Montant: ");
double amount = getDoubleInput();

Account account = bankingService.getAccountByUsername(username)
    .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

bankingService.deposit(account.getAccountNumber(), amount);
System.out.println("Dépôt effectué avec succès!");
```

### Après (avec contrôleur)

```java
// Dans MainApp - code simple et lisible
System.out.print("Montant à déposer: ");
double amount = getDoubleInput();

if (controller.deposit(amount)) {
    System.out.println("✓ Dépôt effectué avec succès!");
    System.out.printf("Nouveau solde: %.2f€%n", controller.getCurrentBalance());
} else {
    System.out.println("✗ Échec du dépôt.");
}
```

## 🧪 Tests du contrôleur

Le contrôleur est testé avec **26 tests unitaires** couvrant :

### Tests d'authentification (4 tests)
- ✅ Login réussi
- ✅ Login échoué
- ✅ Logout
- ✅ Vérification état initial

### Tests de création de compte (2 tests)
- ✅ Création réussie
- ✅ Gestion des erreurs

### Tests d'opérations (12 tests)
- ✅ Dépôt (succès, non connecté, erreur service)
- ✅ Retrait (succès, non connecté, solde insuffisant)
- ✅ Transfert (succès, non connecté, destinataire inexistant, erreur service)

### Tests de consultation (6 tests)
- ✅ Récupération compte/solde (connecté/non connecté)
- ✅ Historique des transactions
- ✅ Statistiques

### Tests utilitaires (2 tests)
- ✅ Vérification existence utilisateur
- ✅ Vérification non-existence

## 📊 Couverture de code

```
Package                Coverage    Tests
─────────────────────────────────────────────
controller             80%         26 tests ← Nouveau!
service                83%         8 tests
strategy               100%        14 tests
factory                94%         9 tests
observer               87%         9 tests
model                  74%         12 tests
─────────────────────────────────────────────
TOTAL                  84%         78 tests
```

**MainApp** est **exclu** de la couverture (pas testé) car il ne contient que de l'interface utilisateur.

## 🚀 Utilisation

### Pour MainApp (Interface console)

```java
// Initialisation
BankingController controller = new BankingController(bankingService);

// Création de compte
String accountNumber = controller.createAccount("john", "pass123", 1000.0);

// Authentification
if (controller.login("john", "pass123")) {
    // Opérations
    controller.deposit(500.0);
    controller.withdraw(200.0);
    controller.transfer("jane", 100.0);
    
    // Consultation
    Double balance = controller.getCurrentBalance();
    List<Transaction> history = controller.getTransactionHistory();
    
    // Statistiques
    int count = controller.getTotalTransactionCount();
    double totalDeposits = controller.getTotalDeposits();
    
    // Déconnexion
    controller.logout();
}
```

### Pour une future interface web/mobile

Le même contrôleur peut être utilisé avec n'importe quelle interface :

```java
@RestController
@RequestMapping("/api/banking")
public class BankingRestController {
    
    private final BankingController controller;
    
    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit(@RequestBody DepositRequest request) {
        if (controller.deposit(request.getAmount())) {
            return ResponseEntity.ok(new DepositResponse(
                controller.getCurrentBalance(),
                "Dépôt réussi"
            ));
        }
        return ResponseEntity.badRequest().body(/* ... */);
    }
    
    // Autres endpoints...
}
```

## ✨ Bonnes pratiques implémentées

1. **Séparation des responsabilités** (SRP)
   - MainApp : Interface utilisateur
   - Controller : Gestion de session et coordination
   - Service : Logique métier
   - Patterns : Comportements réutilisables

2. **Dependency Injection**
   - Le contrôleur reçoit BankingService via constructeur
   - Facilite les tests avec Mockito
   - Permet différentes configurations

3. **Gestion d'erreurs robuste**
   - Méthodes du contrôleur retournent true/false
   - Pas de propagation d'exceptions vers MainApp
   - Logs et messages clairs

4. **API cohérente**
   - Toutes les méthodes suivent le même pattern
   - Vérification automatique de la connexion
   - Noms de méthodes explicites

## 📈 Avantages pour le CI/CD

- ✅ **Tests rapides** : 78 tests s'exécutent en < 3 secondes
- ✅ **Couverture élevée** : 84% de code couvert
- ✅ **Qualité SonarQube** : Pas de code smells majeurs
- ✅ **Maintenabilité** : Score A dans SonarQube
- ✅ **Intégration continue** : Pipeline Jenkins fonctionnel

## 🎓 Conclusion

L'ajout du **BankingController** transforme notre application :

**Avant** : MainApp couplé à BankingService → difficile à tester et maintenir
**Après** : MainApp → Controller → Service → Patterns → testable et extensible

Cette architecture permet :
- De **tester 84% du code** sans tester l'interface console
- D'avoir **78 tests automatisés** qui s'exécutent rapidement
- De **réutiliser facilement** la logique métier dans d'autres contextes
- De **maintenir et faire évoluer** le code plus facilement

---

**Auteur** : Équipe de développement
**Date** : 31/12/2025
**Version** : 1.0.0
