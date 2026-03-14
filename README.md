✈️ SkyBooking - Système de Réservation de Vols

📋 Table des matières

Description

Fonctionnalités

Architecture

Technologies utilisées

Installation

Utilisation

Captures d'écran

Structure du projet

Diagrammes UML

Auteurs

📝 Description
SkyBooking est une application desktop Java complète de gestion de réservation de vols. Elle offre une interface utilisateur moderne et intuitive permettant aux clients de rechercher des vols, effectuer des réservations, choisir leurs sièges, effectuer des paiements et donner des avis. Une interface d'administration est également disponible pour la gestion complète du système.

L'application implémente une architecture robuste basée sur les principes de la programmation orientée objet et utilise les design patterns Singleton et MVC (Modèle-Vue-Contrôleur).

✨ Fonctionnalités
👤 Côté Client
Authentification complète

Connexion / Inscription

Mode invité (consultation sans compte)

Réinitialisation de mot de passe

Connexion administrateur

Recherche de vols

Recherche multi-critères (départ, destination, date, prix, compagnie, classe)

Filtres avancés

Tri des résultats (prix, durée, date)

Visualisation en temps réel des places disponibles

Réservation

Sélection interactive de sièges avec plan de l'avion

Gestion des passagers

Choix de la classe (Économique, Business, Première)

Calcul automatique du prix total

Paiement sécurisé

Plusieurs méthodes de paiement (Carte bancaire, PayPal, Virement, Cryptomonnaie)

Simulation de traitement de paiement

Génération de billet PDF

Gestion de compte

Modification des informations personnelles

Changement de mot de passe avec indicateur de force

Historique des réservations

Préférences de communication

Avis et évaluations

Notation des vols (1-5 étoiles)

Commentaires détaillés

Statistiques des avis

Visualisation des avis récents

🔧 Côté Administrateur
Tableau de bord

Statistiques en temps réel (nombre de vols, passagers, revenus)

Graphiques de performance

Gestion des vols

CRUD complet (Ajout, Modification, Suppression)

Gestion des places disponibles

Gestion des réservations

Vue d'ensemble de toutes les réservations

Possibilité d'annulation


Design Patterns utilisés
Pattern	Utilisation
Singleton	Services (ServiceVols, ServiceReservations, etc.) - une seule instance partagée
MVC	Séparation entre les données (modèles), l'interface (vues) et la logique (contrôleurs dans les panels)
Factory	Création des composants d'interface
Observer	Gestion des événements Swing
💻 Technologies utilisées
Technologie	Version	Utilisation
Java	17+	Langage principal
Swing	JDK 17	Interface graphique
Maven	-	Gestion de projet
JUnit	5	Tests unitaires (structure prévue)
Mermaid	-	Diagrammes UML
⚙️ Installation
Prérequis
Java JDK 17 ou supérieur

Git (optionnel)

Étapes d'installation
Cloner le dépôt

bash
git clone https://github.com/amgharz270-creator/vol.git
cd vol
Compiler le projet

bash
javac -encoding UTF-8 -d bin src/interfaces/*.java src/modeles/*.java src/services/*.java src/gui/*.java
Exécuter l'application

bash
java -cp bin gui.FenetrePrincipale
Ou avec Maven :

bash
mvn clean compile exec:java -Dexec.mainClass="gui.FenetrePrincipale"
🎮 Utilisation
Identifiants de test
Rôle	Email	Mot de passe
Client test	zinebamghar1949@gmail.com	zineeeb12
Admin	admin@skybooking.com	admin123
Guide rapide
Connexion : Utilisez les identifiants ci-dessus ou créez un compte

Recherche : Utilisez les filtres pour trouver un vol

Réservation : Cliquez sur "Réserver" pour un vol, sélectionnez un siège

Paiement : Choisissez une méthode de paiement et validez

Billet : Téléchargez votre billet PDF

Avis : Donnez votre avis après le voyage

📁 Structure du projet
text
skybooking/
├── src/
│   ├── gui/
│   │   └── FenetrePrincipale.java
│   ├── interfaces/
│   │   ├── IAuthentifiable.java
│   │   ├── IReservable.java
│   │   ├── IPayable.java
│   │   ├── INotifiable.java
│   │   ├── IAdministrable.java
│   │   ├── IRechercheVol.java
│   │   ├── IGestionSieges.java
│   │   └── IReviewable.java
│   ├── modeles/
│   │   ├── Client.java
│   │   ├── Admin.java
│   │   ├── Vol.java
│   │   ├── Reservation.java
│   │   ├── Passager.java
│   │   ├── Siege.java
│   │   └── Avis.java
│   └── services/
│       ├── ServiceVols.java
│       ├── ServiceReservations.java
│       ├── ServiceSieges.java
│       ├── ServicePaiement.java
│       ├── ServiceAvis.java
│       ├── ServiceNotifications.java
│       └── GenerateurPDF.java
├── .vscode/
│   └── settings.json
├── README.md
└── pom.xml (optionnel)

🚀 Améliorations futures
Base de données persistante (MySQL/PostgreSQL)

API REST pour version web

Application mobile (Android/iOS)

Intégration avec de vraies passerelles de paiement

Système de fidélité avec miles

Notifications par email réelles (JavaMail)

Internationalisation (i18n) - Support multilingue

Thèmes personnalisables

Export des rapports (Excel/PDF)

Tests unitaires complets

👥 Auteurs
Zineb Amghar - Développement principal - GitHub

📄 Licence
Ce projet est sous licence MIT - voir le fichier LICENSE pour plus de détails.

text
MIT License

Copyright (c) 2026 Zineb Amghar

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files...
🙏 Remerciements
Merci à tous les contributeurs et testeurs

Inspiré par les systèmes de réservation des grandes compagnies aériennes

Développé dans le cadre d'un projet d'études en IHM( Interface-Homme-Machine)


Dernière mise à jour : Mars 2026


















Diagramme de Classes (simplifié)
text
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Client    │────▶│ Reservation │────▶│     Vol     │
├─────────────┤     ├─────────────┤     ├─────────────┤
│-idClient    │     │-idReservation│     │-numeroVol   │
│-nom         │     │-statut      │     │-compagnie   │
│-email       │     │-montantPaye │     │-destination │
└─────────────┘     └─────────────┘     │-prix        │
                                          └─────────────┘
        │                                      │
        ▼                                      ▼
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Avis      │     │  Passager   │     │   Siege     │
├─────────────┤     ├─────────────┤     ├─────────────┤
│-note        │     │-nom         │     │-numeroSiege │
│-commentaire │     │-passeport   │     │-occupe      │
└─────────────┘     └─────────────┘     └─────────────┘
Pour les diagrammes complets, consultez la documentation UML

🧪 Tests
bash
# Exécuter les tests
mvn test
🚀 Améliorations futures
Base de données persistante (MySQL/PostgreSQL)

API REST pour version web

Application mobile (Android/iOS)

Intégration avec de vraies passerelles de paiement

Système de fidélité avec miles

Notifications par email réelles (JavaMail)

Internationalisation (i18n) - Support multilingue

Thèmes personnalisables

Export des rapports (Excel/PDF)

Tests unitaires complets

👥 Auteurs
Zineb Amghar - Développement principal - GitHub

📄 Licence
Ce projet est sous licence MIT - voir le fichier LICENSE pour plus de détails.

text
MIT License

Copyright (c) 2026 Zineb Amghar

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files...
🙏 Remerciements
Merci à tous les contributeurs et testeurs

Inspiré par les systèmes de réservation des grandes compagnies aériennes

Développé dans le cadre d'un projet d'études en génie logiciel

⭐ N'oubliez pas de mettre une étoile si ce projet vous a été utile !

Dernière mise à jour : Mars 2026
