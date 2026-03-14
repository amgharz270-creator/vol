✈️ SkyBooking - Système de Réservation de Vols
📋 Description
SkyBooking est une application desktop Java complète de gestion et réservation de vols aériens. Elle offre une interface graphique moderne et intuitive permettant aux clients de rechercher des vols, effectuer des réservations, gérer leurs profils et laisser des avis. Les administrateurs peuvent gérer les vols, consulter les statistiques et superviser les réservations.
🏗️ Architecture du Projet
Le projet suit une architecture MVC (Modèle-Vue-Contrôleur) avec les packages suivants :
plain
Copy
src/
├── interfaces/     # Interfaces fonctionnelles (contrats)
├── modeles/        # Classes métier (entités)
├── services/       # Services métier (logique applicative)
└── gui/            # Interface graphique (Swing)
📦 Structure des Classes
🔌 Interfaces (interfaces/)
Table
Interface	Description	Méthodes principales
IAuthentifiable	Gestion de l'authentification	seConnecter(), seDeconnecter(), changerMotDePasse()
IAdministrable	Fonctions d'administration	ajouterVol(), modifierVol(), supprimerVol(), getRevenusTotaux()
IReservable	Gestion des réservations	reserver(), annuler(), modifier(), verifierDisponibilite()
IPayable	Traitement des paiements	effectuerPaiement(), rembourser(), getStatutPaiement()
INotifiable	Système de notifications	envoyerConfirmation(), envoyerRappel(), envoyerAnnulation()
IRechercheVol	Recherche de vols	rechercherParDestination(), rechercherParDate(), rechercherParPrix()
IGestionSieges	Gestion des sièges	reserverSiege(), libererSiege(), genererPlanSieges()
IReviewable	Système d'avis	ajouterAvis(), getAvis(), getNoteMoyenne()
🎯 Modèles (modeles/)
Client
Rôle : Représente un utilisateur du système
Implémente : IAuthentifiable, INotifiable
Attributs principaux :
idClient, nom, prenom, email, telephone, password
historiqueReservations : Liste des réservations du client
connecte : État de connexion
Méthodes clés :
java
Copy
public boolean seConnecter(String email, String password)
public void ajouterReservation(Reservation r)
public void envoyerConfirmation(String message)
Admin
Rôle : Administrateur du système avec privilèges étendus
Implémente : IAuthentifiable, IAdministrable
Attributs : idAdmin, nom, email, password, connecte
Services injectés : ServiceVols, ServiceReservations
Méthodes clés :
java
Copy
public boolean ajouterVol(Vol vol)
public boolean supprimerVol(String numeroVol)
public double getRevenusTotaux()  // Agrégation via ServiceReservations
Vol
Rôle : Représente un vol aérien
Attributs :
numeroVol, compagnie, aeroportDepart, aeroportArrivee
dateDepart, dateArrivee : LocalDateTime
prix, placesDisponibles, classe
Reservation
Rôle : Gère une réservation de vol
Implémente : IReservable, IPayable
Attributs :
idReservation, client, vol, dateReservation
statut : "CONFIRMEE", "EN_ATTENTE", "ANNULEE"
montantPaye, paye, passagers (List<Passager>
Logique métier :
java
Copy
public boolean reserver() {
    if (vol.getPlacesDisponibles() >= passagers.size()) {
        vol.setPlacesDisponibles(vol.getPlacesDisponibles() - passagers.size());
        this.statut = "CONFIRMEE";
        client.envoyerConfirmation("Votre réservation est confirmée!");
        return true;
    }
    return false;
}
Passager
Rôle : Informations d'un passager
Attributs : nom, prenom, dateNaissance, numeroPasseport, classe
Siege
Rôle : Représentation d'un siège d'avion
Attributs : numeroSiege, numeroVol, classe, occupe, idPassager, prixSupplement
Avis
Rôle : Avis client sur un vol
Attributs : idAvis, idClient, nomClient, numeroVol, note (1-5), commentaire, dateAvis
⚙️ Services (services/)
Tous les services implémentent le pattern Singleton pour garantir une instance unique :
ServiceVols
Rôle : Gestion du catalogue de vols et recherche
Implémente : IRechercheVol
Données : List<Vol> listeVols
Méthodes principales :
java
Copy
public List<Vol> rechercheAvancee(String depart, String arrivee, 
                                  LocalDate date, Double prixMax, 
                                  String compagnie, String classe)
public void initialiserVols()  // Crée 7 vols de démonstration
public List<String> getToutesCompagnies()  // Destinations uniques
ServiceReservations
Rôle : Gestion du cycle de vie des réservations
Données : List<Reservation> reservations, compteurId
Méthodes clés :
java
Copy
public Reservation creerReservation(Client client, Vol vol)
public List<Reservation> getReservationsClient(Client client)
public void initialiserReservationsTest(Client client)  // Données de test
public boolean aClientVoyage(int idClient, String numeroVol)  // Pour les avis
ServiceSieges
Rôle : Gestion de l'occupation des sièges
Implémente : IGestionSieges
Données : Map<String, List<Siege>> siegesParVol
Logique d'initialisation :
java
Copy
public void initialiserSiegesPourVol(String numeroVol, int capacite) {
    // 60% Économique, 30% Business, 10% Première
    int ecoEnd = (int)(capacite * 0.6);
    int busEnd = (int)(capacite * 0.9);
    // Création des sièges avec prix supplémentaires
}
ServicePaiement
Rôle : Traitement des paiements simulé
Données : Map<Integer, String> statutPaiements, methodesPaiement
Enum : MethodePaiement { CARTE_BANCAIRE, PAYPAL, VIREMENT, CRYPTO }
Simulation : 95% de taux de succès avec Random
ServiceNotifications
Rôle : Envoi de notifications (console/simulation)
Données : List<String> historiqueNotifications
ServiceAvis
Rôle : Gestion des avis clients
Logique : Vérifie que le client a bien voyagé avant d'autoriser un avis
GenerateurPDF
Rôle : Génération de billets textuels (simulation PDF)
🖥️ Interface Graphique (gui/)
FenetrePrincipale
Type : JFrame principale
Layout : CardLayout pour la navigation entre panels
Architecture : Inner classes pour chaque panel fonctionnel
Panels internes :
Table
Panel	Fonction
PanelConnexion	Authentification avec validation email/password
PanelInscription	Création de compte client
PanelRecherche	Recherche de vols avec filtres avancés
PanelReservation	Formulaire de réservation + sélection sièges
PanelPaiement	Choix méthode de paiement
PanelConfirmation	Affichage du billet généré
PanelProfil	Gestion du profil (4 onglets)
PanelMesReservations	Historique des réservations du client
PanelAdmin	Tableau de bord administrateur
PanelAvis	Dépôt et consultation d'avis
Design System :
java
Copy
// Palette de couleurs professionnelle
private static final Color PRIMARY_DARK = new Color(15, 23, 42);    // Slate 900
private static final Color ACCENT = new Color(59, 130, 246);        // Blue 500
private static final Color SUCCESS = new Color(34, 197, 94);        // Green 500
private static final Color DANGER = new Color(239, 68, 68);         // Red 500
🔗 Relations entre Classes
Diagramme de Classes Simplifié
plain
Copy
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   Client    │◄──────│ Reservation │──────►│    Vol      │
├─────────────┤  1:n  ├─────────────┤  n:1  ├─────────────┤
│ -idClient   │       │ -idReserv   │       │ -numeroVol  │
│ -nom        │       │ -statut     │       │ -compagnie  │
│ -email      │       │ -montantPaye│       │ -prix       │
│ -password   │       │ -passagers  │◄──────│ -places     │
├─────────────┤       ├─────────────┤  n:1  ├─────────────┤
│+seConnecter()       │+reserver()  │       │+getPrix()   │
│+ajouterReserv()     │+annuler()   │       │+setPlaces() │
└──────┬──────┘       └──────┬──────┘       └──────┬──────┘
       │                     │                       │
       │                     │                       │
       ▼                     ▼                       ▼
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│ServiceReserv│       │   Passager  │       │ ServiceVols │
│  (Singleton)│       ├─────────────┤       │  (Singleton)│
├─────────────┤       │ -nom        │       ├─────────────┤
│ -reservations      │ -prenom     │       │ -listeVols  │
│ -compteurId │       │ -passeport  │       ├─────────────┤
├─────────────┤       ├─────────────┤       │+rechercher()│
│+creerReserv()│       │+getNom()    │       │+ajouterVol()│
│+annuler()   │       │+getPrenom() │       │+getCompagnie│
└─────────────┘       └─────────────┘       └─────────────┘
Flux de Données - Réservation
plain
Copy
1. Client sélectionne un vol (PanelRecherche)
   ↓
2. Clic sur "Réserver" → setVol() dans PanelReservation
   ↓
3. Saisie des informations passager + sélection siège
   ↓
4. Clic "Continuer" → creerReservation() dans ServiceReservations
   ↓
5. PanelPaiement : choix méthode → effectuerPaiement()
   ↓
6. Si succès : envoyerConfirmation() + afficher PanelConfirmation
   ↓
7. Génération du billet PDF (GenerateurPDF)
🚀 Fonctionnalités
Pour les Clients
✅ Recherche de vols multi-critères (destination, date, prix, compagnie, classe)
✅ Réservation avec sélection visuelle des sièges
✅ Paiement simulé (Carte, PayPal, Virement, Crypto)
✅ Gestion du profil (informations, sécurité, préférences)
✅ Historique des réservations avec statuts colorés
✅ Système d'avis et notation des vols
✅ Notifications email simulées
Pour les Administrateurs
✅ Tableau de bord avec statistiques en temps réel
✅ Gestion du catalogue de vols (CRUD)
✅ Supervision des réservations
✅ Calcul des revenus totaux
Fonctionnalités Techniques
🎨 Interface moderne avec thème sombre/clair
📱 Design responsive et adaptable
🔒 Validation des formulaires avec feedback visuel
📊 Tableaux de données avec tri et filtres
🎭 Mode invité (recherche sans connexion)
🛠️ Technologies Utilisées
Java 8+ : Langage principal
Swing : Framework GUI
java.time : Gestion des dates (API modernisée)
Stream API : Traitement des collections
Pattern Singleton : Services
Pattern Observer : Événements Swing
GridBagLayout/CardLayout : Layouts avancés
📊 Données de Test
Le système est initialisé avec :
7 vols : Air France, British Airways, Lufthansa, Emirates, Delta
Destinations : Paris, New York, Londres, Tokyo, Francfort, Dubai
3 réservations de test pour le client connecté
Sièges générés automatiquement par vol (60% Eco, 30% Business, 10% Première)
🔐 Identifiants de Test
plain
Copy
Client:
  Email: zineb.amghar@email.com (simulé)
  Password: password123

Admin:
  Email: admin@skybooking.com
  Password: admin123
🎯 Points d'Extension
Le système est conçu pour être facilement extensible :
Persistance : Remplacer les ArrayList par une base de données (JDBC/JPA)
API Externe : Connecter des API de vols réels (Amadeus, Skyscanner)
Vrai Paiement : Intégrer Stripe, PayPal SDK
Email : Remplacer System.out par JavaMail API
PDF Réel : Intégrer iText ou Apache PDFBox
Internationalisation : Ajouter ResourceBundles pour i18n
📝 Notes de Développement
Le code utilise des inner classes pour encapsuler les panels
Les services sont thread-safe (Singleton avec initialisation lazy)
La validation des emails utilise regex
Le générateur de PDF produit du texte ASCII art (simulation)
Les couleurs suivent la palette Tailwind CSS (Slate/Blue/Green)
Projet académique Java - Architecture MVC, Patterns de conception, POO avancée
