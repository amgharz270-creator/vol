package modeles;

import java.time.LocalDate;

public class Passager {
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String numeroPasseport;
    private String classe;
    
    public Passager(String nom, String prenom, LocalDate dateNaissance, 
                   String numeroPasseport, String classe) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.numeroPasseport = numeroPasseport;
        this.classe = classe;
    }
    
    // Getters
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getNumeroPasseport() { return numeroPasseport; }
}