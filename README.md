# Trip Reservation System

Nom de l'équipe : X-ray  
Membres de l'équipe :  
Trung Nguyen, 20238006  
Dawson Zhang, 20268585  
Tien Tran, 20246669


Ce dépôt contient un programme Java simulant un système de réservation de voyages pour différents moyens de transport (avion, train et bateau).

Le système est structuré autour d’une application Java avec interface graphique (JavaFX), construite avec Maven, permettant de gérer les réservations, les utilisateurs et les différents types de trajets.


## Project Structure

```
3911_devoir3/
├── Code/
│   └── trip-portal/
│       ├── pom.xml                  # Configuration Maven
│       └── src/
│           ├── main/                # Code source principal
│           ├── test/                
│           └── Database/            # Données JSON (base de données locale)
│
├── Design/                          # Diagrammes et fichiers VPP (UML)
├── Rapport/                         # Rapport HTML du projet
├── .gitignore
└── README.md
```

## Prérequis

### Java / Maven

- **JDK 17 ou supérieur** — [Télécharger ici (JDK Temurin)](https://adoptium.net/)
- **Maven 3.8+** — [Télécharger ici](https://maven.apache.org/download.cgi)

## Étapes d'exécution

1. Ouvrez un terminal 
2. Naviguez vers le dossier du projet :

```bash
cd devoir3_3911/Code/trip-portal
```

3. Lancez l'application avec Maven :

```bash
mvn clean javafx:run
```
