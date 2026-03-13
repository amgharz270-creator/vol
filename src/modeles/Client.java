package modeles;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import interfaces.IAuthentifiable;
import interfaces.INotifiable;

public class Client implements IAuthentifiable, INotifiable {
    private int idClient;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String password;
    private boolean connecte;
    private List<Reservation> historiqueReservations;
    
    // Constructeur avec mot de passe
    public Client(int idClient, String nom, String prenom, String email, String telephone, String password) {
        this.idClient = idClient;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
        this.historiqueReservations = new ArrayList<>();
        this.connecte = false;
    }
    
    // Constructeur sans mot de passe (pour mode invité)
    public Client(int idClient, String nom, String prenom, String email, String telephone) {
        this(idClient, nom, prenom, email, telephone, "");
    }
    
    // Implémentation IAuthentifiable
    @Override
    public boolean seConnecter(String email, String password) {
        if (this.email.equals(email) && this.password.equals(password)) {
            this.connecte = true;
            System.out.println("Connexion réussie pour " + this.prenom + " " + this.nom);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean seDeconnecter() {
        this.connecte = false;
        System.out.println("Déconnexion réussie");
        return true;
    }
    
    @Override
    public boolean changerMotDePasse(String ancien, String nouveau) {
        if (this.password.equals(ancien)) {
            this.password = nouveau;
            return true;
        }
        return false;
    }
    
    // Implémentation INotifiable
    @Override
    public void envoyerConfirmation(String message) {
        System.out.println("Email envoyé à " + this.email + ": " + message);
    }
    
    @Override
    public void envoyerRappel() {
        System.out.println("Rappel envoyé à " + this.email + " pour votre vol demain!");
    }
    
    @Override
    public void envoyerAnnulation(String raison) {
        System.out.println("Email d'annulation envoyé à " + this.email + ": " + raison);
    }
    
    // Méthodes métier
    public void ajouterReservation(Reservation r) {
        historiqueReservations.add(r);
    }
    
    public List<Reservation> getHistorique() {
        return historiqueReservations;
    }
    
    // Getters
    public int getId() { 
        return idClient; 
    }
    
    public String getNom() { 
        return nom; 
    }
    
    public String getPrenom() { 
        return prenom; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public String getTelephone() { 
        return telephone; 
    }
    
    public boolean isConnecte() { 
        return connecte; 
    }
    
    // Setters
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Client client = (Client) obj;
        return idClient == client.idClient;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idClient);
    }
}
