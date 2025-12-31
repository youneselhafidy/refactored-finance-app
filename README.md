# Refactored Finance Application

## 📋 Description du Projet

Application bancaire refactorisée démontrant l'application des **design patterns** et des principes **SOLID** pour transformer un code "spaghetti" en une architecture maintenable et extensible.

### Contexte Pédagogique
Ce projet est réalisé dans le cadre du module **Ingénierie Logicielle** à l'ENSAM. Il illustre la transformation d'une application monolithique en une architecture modulaire utilisant les design patterns fondamentaux.

---

## 🎯 Objectifs du Projet

1. ✅ Refactoriser un code spaghetti en appliquant les principes SOLID
2. ✅ Implémenter 3 design patterns majeurs (Strategy, Factory, Observer)
3. ✅ Atteindre une couverture de tests > 80%
4. ✅ Mettre en place un pipeline CI/CD avec Jenkins
5. ✅ Intégrer l'analyse de qualité avec SonarQube

---

## 🏗️ Architecture et Design Patterns

### 1. **Strategy Pattern** 🎯
**Problème résolu**: Élimination des switch/case pour les différents types de transactions.

**Implémentation**:
- `TransactionStrategy` (interface)
- `DepositStrategy` - Gestion des dépôts
- `WithdrawStrategy` - Gestion des retraits avec validation du solde
- `TransferStrategy` - Gestion des transferts entre comptes

**Avantages**:
- Code extensible (ajout de nouveaux types de transactions)
- Respect du principe Open/Closed
- Logique métier isolée

### 2. **Factory Pattern** 🏭
**Problème résolu**: Centralisation de la logique de création d'objets complexes.

**Implémentation**:
- `AccountFactory` - Création de comptes avec numérotation automatique
- `UserFactory` - Création d'utilisateurs avec validation

**Avantages**:
- Encapsulation de la création d'objets
- Validation centralisée
- Facilite les tests unitaires

### 3. **Observer Pattern** 👁️
**Problème résolu**: Notification automatique des événements sans couplage fort.

**Implémentation**:
- `TransactionObserver` (interface)
- `AuditLogger` - Journalisation des transactions
- `NotificationService` - Envoi de notifications

**Avantages**:
- Découplage entre émetteurs et récepteurs
- Extensibilité (ajout de nouveaux observateurs)
- Respect du principe de responsabilité unique

---

## 📁 Structure du Projet

```
refactored-finance-app/
├── src/
│   ├── main/java/com/university/finance/
│   │   ├── pattern/
│   │   │   ├── strategy/
│   │   │   │   ├── TransactionStrategy.java
│   │   │   │   ├── DepositStrategy.java
│   │   │   │   ├── WithdrawStrategy.java
│   │   │   │   └── TransferStrategy.java
│   │   │   ├── factory/
│   │   │   │   ├── AccountFactory.java
│   │   │   │   └── UserFactory.java
│   │   │   └── observer/
│   │   │       ├── TransactionObserver.java
│   │   │       ├── AuditLogger.java
│   │   │       └── NotificationService.java
│   │   ├── model/
│   │   │   ├── Account.java
│   │   │   ├── User.java
│   │   │   └── Transaction.java
│   │   ├── service/
│   │   │   ├── BankingService.java
│   │   │   └── TransactionService.java
│   │   └── MainApp.java
│   └── test/java/com/university/finance/
│       ├── model/
│       │   ├── AccountTest.java
│       │   └── UserTest.java
│       ├── pattern/
│       │   ├── strategy/
│       │   │   ├── DepositStrategyTest.java
│       │   │   ├── WithdrawStrategyTest.java
│       │   │   └── TransferStrategyTest.java
│       │   ├── factory/
│       │   │   ├── AccountFactoryTest.java
│       │   │   └── UserFactoryTest.java
│       │   └── observer/
│       │       ├── AuditLoggerTest.java
│       │       └── NotificationServiceTest.java
│       └── service/
│           └── BankingServiceTest.java
├── pom.xml
├── Jenkinsfile
├── sonar-project.properties
└── README.md
```

---

## 🔧 Installation et Configuration

### Prérequis
- **Java JDK 11** ou supérieur
- **Maven 3.6+**
- **Git**
- **Jenkins** (optionnel, pour CI/CD)
- **SonarQube** (optionnel, pour analyse de qualité)

### Installation Locale

1. **Cloner le repository**
```bash
git clone https://github.com/ENSAM/finance-refactoring.git
cd refactored-finance-app
```

2. **Compiler le projet**
```bash
mvn clean compile
```

3. **Exécuter les tests**
```bash
mvn test
```

4. **Générer le rapport de couverture**
```bash
mvn jacoco:report
```
Le rapport sera disponible dans `target/site/jacoco/index.html`

5. **Créer le package JAR**
```bash
mvn package
```

6. **Exécuter l'application**
```bash
java -jar target/refactored-finance-app-1.0.0.jar
```

---

## 🧪 Tests Unitaires

### Couverture des Tests
- **Total de tests**: 30+ tests unitaires
- **Couverture cible**: > 80%
- **Framework**: JUnit 4.13.2

### Exécution des Tests
```bash
# Exécuter tous les tests
mvn test

# Exécuter un test spécifique
mvn test -Dtest=AccountTest

# Exécuter avec couverture
mvn clean test jacoco:report
```

### Organisation des Tests
- **Model Tests**: Validation des entités métier
- **Strategy Tests**: Tests des stratégies de transaction
- **Factory Tests**: Tests de création d'objets
- **Observer Tests**: Tests des notifications
- **Service Tests**: Tests d'intégration des services

---

## 🔄 Pipeline CI/CD

### Configuration Jenkins

Le projet inclut un `Jenkinsfile` complet avec les étapes suivantes:

1. **Checkout**: Récupération du code source
2. **Build**: Compilation avec Maven
3. **Unit Tests**: Exécution des tests avec génération de rapports
4. **Code Coverage**: Analyse de la couverture avec JaCoCo
5. **Code Quality**: Analyse SonarQube
6. **Quality Gate**: Validation des seuils de qualité
7. **Package**: Création de l'artifact JAR
8. **Archive**: Sauvegarde des artifacts

### Déclenchement
- **Automatique**: Push sur la branche `main`
- **Manuel**: Via l'interface Jenkins

---

## 📊 Analyse de Qualité avec SonarQube

### Métriques Surveillées

| Métrique | Objectif | Description |
|----------|----------|-------------|
| **Coverage** | > 80% | Couverture de code par les tests |
| **Code Smells** | < 10 | Problèmes de maintenabilité |
| **Technical Debt** | < 1h | Dette technique estimée |
| **Bugs** | 0 | Bugs détectés |
| **Vulnerabilities** | 0 | Vulnérabilités de sécurité |
| **Duplications** | < 3% | Code dupliqué |

### Lancer l'Analyse Locale
```bash
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=refactored-finance-app \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=YOUR_TOKEN
```

---

## 🎨 Diagrammes UML

### Diagramme de Classes (Simplifié)

```
┌─────────────────────┐
│   <<interface>>     │
│ TransactionStrategy │
├─────────────────────┤
│ + execute()         │
│ + validate()        │
└──────────┬──────────┘
           │
     ┌─────┴──────┬──────────────┐
     │            │              │
┌────▼────┐  ┌───▼─────┐  ┌────▼─────┐
│ Deposit │  │ Withdraw│  │ Transfer │
│Strategy │  │Strategy │  │ Strategy │
└─────────┘  └─────────┘  └──────────┘

┌──────────────┐
│  Account     │◄─────────┐
├──────────────┤          │
│ - balance    │          │ creates
│ + getBalance│          │
└──────────────┘          │
                    ┌─────┴────────┐
                    │AccountFactory│
                    ├──────────────┤
                    │+createAccount│
                    └──────────────┘
```

### Diagramme de Séquence - Transfert

```
User -> BankingService: transfer(from, to, amount)
BankingService -> TransactionService: executeTransaction()
TransactionService -> TransferStrategy: execute()
TransferStrategy -> Account: debit(from)
TransferStrategy -> Account: credit(to)
TransferStrategy -> TransactionService: return Transaction
TransactionService -> AuditLogger: onTransaction()
TransactionService -> NotificationService: onTransaction()
```

---

## 📈 Comparaison Avant/Après

### Code Spaghetti (Avant)
❌ Une seule classe monolithique  
❌ Méthode `main()` de 200+ lignes  
❌ Code dupliqué (journalisation répétée)  
❌ Aucune séparation des responsabilités  
❌ Impossible à tester unitairement  
❌ Violations des principes SOLID  

### Code Refactorisé (Après)
✅ 15+ classes bien organisées  
✅ Responsabilités clairement définies  
✅ 3 design patterns implémentés  
✅ 30+ tests unitaires (>80% coverage)  
✅ Code maintenable et extensible  
✅ Respect des principes SOLID  

---

## 🚀 Utilisation de l'Application

### Menu Principal
```
=== Système Bancaire Refactorisé ===
1. Afficher solde
2. Déposer argent
3. Retirer argent
4. Transfert
5. Historique des transactions
6. Ajouter utilisateur
7. Voir l'audit complet
0. Quitter
```

### Exemples d'Utilisation

**Dépôt d'argent:**
```
Choix: 2
Nom d'utilisateur: user1
Montant: 500
✓ Dépôt effectué avec succès!
```

**Transfert:**
```
Choix: 4
De l'utilisateur: user1
Vers l'utilisateur: user2
Montant: 300
✓ Transfert effectué avec succès!
```

---

## 🛠️ Technologies Utilisées

- **Java 11**: Langage de programmation
- **Maven**: Gestion de dépendances et build
- **JUnit 4**: Framework de tests unitaires
- **JaCoCo**: Analyse de couverture de code
- **SonarQube**: Analyse de qualité de code
- **Jenkins**: Intégration continue
- **Git**: Gestion de version

---

## 📝 Principes SOLID Appliqués

1. **Single Responsibility Principle (SRP)**
   - Chaque classe a une seule responsabilité
   - `BankingService`, `TransactionService`, `AuditLogger`

2. **Open/Closed Principle (OCP)**
   - Extensible via interfaces (Strategy, Observer)
   - Nouveau type de transaction sans modifier le code existant

3. **Liskov Substitution Principle (LSP)**
   - Toutes les stratégies sont interchangeables
   - Tous les observateurs respectent le contrat

4. **Interface Segregation Principle (ISP)**
   - Interfaces ciblées et spécifiques
   - Pas d'interface "fourre-tout"

5. **Dependency Inversion Principle (DIP)**
   - Dépendance sur des abstractions (interfaces)
   - Injection de dépendances dans les services

---

## 🎓 Auteur

**Projet réalisé par**: [Votre Nom]  
**Module**: Ingénierie Logicielle  
**Institution**: École Nationale des Sciences Appliquées de Marrakech (ENSAM)  
**Encadrant**: BOUARIFI Walid  
**Année**: 2025

---

## 📄 Licence

Ce projet est réalisé à des fins pédagogiques dans le cadre du cursus ENSAM.

---

## 📞 Contact

Pour toute question concernant ce projet:
- **Email**: [votre.email@etu.uca.ma]
- **GitHub**: [Votre profil GitHub]

---

## 🔗 Ressources Complémentaires

- [Design Patterns - Gang of Four](https://refactoring.guru/design-patterns)
- [Principes SOLID](https://www.digitalocean.com/community/conceptual_articles/s-o-l-i-d-the-first-five-principles-of-object-oriented-design)
- [Clean Code - Robert C. Martin](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)
- [Refactoring - Martin Fowler](https://refactoring.com/)

---

*Dernière mise à jour: Décembre 2025*
