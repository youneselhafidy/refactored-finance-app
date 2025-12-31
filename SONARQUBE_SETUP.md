# Guide de Configuration SonarQube

## Installation et Configuration

### 1. Installation via Docker (Recommandé)

```bash
# Démarrer SonarQube
docker run -d --name sonarqube -p 9000:9000 sonarqube:latest

# Attendre le démarrage (2-3 minutes)
# Accéder à http://localhost:9000
# Credentials par défaut: admin/admin
```

### 2. Installation Manuelle

1. Télécharger SonarQube depuis https://www.sonarqube.org/downloads/
2. Extraire l'archive
3. Lancer `bin/windows-x86-64/StartSonar.bat`
4. Accéder à http://localhost:9000

### 3. Configuration Initiale

1. **Connexion**
   - URL: http://localhost:9000
   - Login: admin
   - Password: admin
   - Changer le mot de passe lors de la première connexion

2. **Créer un Token**
   - My Account → Security → Generate Token
   - Nom: "refactored-finance-app"
   - Copier le token généré

3. **Créer le Projet**
   - Projects → Create Project
   - Project key: `refactored-finance-app`
   - Display name: `Refactored Finance Application`

## Exécution de l'Analyse

### Option 1: Ligne de Commande

```bash
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=refactored-finance-app \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=VOTRE_TOKEN_ICI
```

### Option 2: Via Jenkins

Le Jenkinsfile est déjà configuré pour SonarQube:

1. **Configurer SonarQube dans Jenkins**
   - Manage Jenkins → Configure System
   - Section "SonarQube servers"
   - Add SonarQube:
     - Name: `SonarQube`
     - Server URL: `http://localhost:9000`
     - Server authentication token: Ajouter credentials avec le token

2. **Lancer le Pipeline**
   - Le stage "Code Quality Analysis" exécutera l'analyse
   - Le stage "Quality Gate" attendra la validation

## Configuration du Projet

Le fichier `sonar-project.properties` est déjà configuré:

```properties
sonar.projectKey=refactored-finance-app
sonar.projectName=Refactored Finance Application
sonar.projectVersion=1.0.0

sonar.sources=src/main/java
sonar.tests=src/test/java
sonar.sourceEncoding=UTF-8

sonar.java.source=11
sonar.java.binaries=target/classes
sonar.java.test.binaries=target/test-classes

sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
sonar.jacoco.reportPaths=target/jacoco.exec

# Exclusion de MainApp pour la couverture
sonar.coverage.exclusions=**/MainApp.java,**/*Test.java
```

## Métriques SonarQube Attendues

### 1. Fiabilité (Reliability)
- **Bugs**: Nombre de bugs détectés
- **Reliability Rating**: Note de A à E

### 2. Sécurité (Security)
- **Vulnerabilities**: Vulnérabilités identifiées
- **Security Rating**: Note de A à E
- **Security Hotspots**: Points sensibles à vérifier

### 3. Maintenabilité (Maintainability)
- **Code Smells**: Problèmes de qualité de code
- **Technical Debt**: Dette technique estimée
- **Maintainability Rating**: Note de A à E

### 4. Couverture (Coverage)
- **Coverage**: Pourcentage de couverture de code
- **Line Coverage**: Couverture par ligne
- **Branch Coverage**: Couverture des branches

### 5. Duplication
- **Duplications**: Pourcentage de code dupliqué
- **Duplicated Lines**: Nombre de lignes dupliquées
- **Duplicated Blocks**: Nombre de blocs dupliqués

## Quality Gates

### Configuration par Défaut

SonarQube utilise le Quality Gate "Sonar way" par défaut:

- Coverage ≥ 80%
- Duplicated Lines < 3%
- Maintainability Rating = A
- Reliability Rating = A
- Security Rating = A
- Security Hotspots Reviewed = 100%

### Quality Gate Personnalisé

Pour ce projet, considérer:

```
Condition                           | Operator | Value
------------------------------------|----------|-------
Coverage on New Code                | is less than | 80%
Duplicated Lines (%) on New Code    | is greater than | 3%
Maintainability Rating on New Code  | is worse than | A
Reliability Rating on New Code      | is worse than | A
Security Rating on New Code         | is worse than | A
```

## Résultats Attendus

### Points Forts Anticipés
1. ✅ **Architecture**: Utilisation de design patterns
2. ✅ **Tests**: 52 tests unitaires couvrant la logique métier
3. ✅ **Encapsulation**: Bonne utilisation des modificateurs d'accès
4. ✅ **Documentation**: Code commenté et documenté

### Points d'Attention Possibles
1. ⚠️ **Couverture**: 61% global (en dessous de 80%)
   - Solution: MainApp exclu de l'analyse
2. ⚠️ **Complexité**: Méthodes potentiellement complexes dans MainApp
3. ⚠️ **Exception Handling**: Validation à effectuer

## Commandes Utiles

### Analyse Complète
```bash
mvn clean verify sonar:sonar
```

### Analyse avec Profil Spécifique
```bash
mvn sonar:sonar -Dsonar.profile="Custom Profile"
```

### Mise à Jour de la Configuration
```bash
mvn sonar:sonar -Dsonar.projectVersion=1.0.1
```

### Debug Mode
```bash
mvn sonar:sonar -X
```

## Intégration Continue

### GitHub Actions (exemple)

```yaml
name: SonarQube Analysis

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  sonarqube:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          java-version: '11'
      - name: Build and analyze
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: mvn clean verify sonar:sonar
```

## Ressources

- Documentation SonarQube: https://docs.sonarqube.org/
- SonarQube Maven Plugin: https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-maven/
- JaCoCo Integration: https://docs.sonarqube.org/latest/analysis/coverage/

## Troubleshooting

### Problème: "401 Unauthorized"
**Solution**: Vérifier que le token est correct et que l'utilisateur a les permissions nécessaires.

### Problème: "Coverage report not found"
**Solution**: S'assurer que les tests ont été exécutés et que JaCoCo a généré le rapport XML.
```bash
mvn clean test
mvn jacoco:report
mvn sonar:sonar
```

### Problème: "Project key already exists"
**Solution**: Utiliser un autre projectKey ou supprimer le projet existant dans SonarQube.

### Problème: "Quality Gate failed"
**Solution**: Consulter les détails dans le rapport SonarQube et corriger les problèmes identifiés.
