package modeles;

import java.time.LocalDateTime;

public class Vol {
    private String numeroVol;
    private String compagnie;
    private String aeroportDepart;
    private String aeroportArrivee;
    private LocalDateTime dateDepart;
    private LocalDateTime dateArrivee;  // AJOUTE CECI
    private double prix;
    private int placesDisponibles;
    private String classe;  // AJOUTE CECI

    public Vol(String numeroVol, String compagnie, String aeroportDepart, 
               String aeroportArrivee, LocalDateTime dateDepart, 
               LocalDateTime dateArrivee, double prix, int placesDisponibles, String classe) {
        this.numeroVol = numeroVol;
        this.compagnie = compagnie;
        this.aeroportDepart = aeroportDepart;
        this.aeroportArrivee = aeroportArrivee;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;  // AJOUTE CECI
        this.prix = prix;
        this.placesDisponibles = placesDisponibles;
        this.classe = classe;  // AJOUTE CECI
    }

    // Getters
    public String getNumeroVol() { return numeroVol; }
    public String getCompagnie() { return compagnie; }
    public String getAeroportDepart() { return aeroportDepart; }
    public String getAeroportArrivee() { return aeroportArrivee; }
    public LocalDateTime getDateDepart() { return dateDepart; }
    
    // AJOUTE CES DEUX GETTERS MANQUANTS :
    public LocalDateTime getDateArrivee() { return dateArrivee; }
    public String getClasse() { return classe; }
    
    public double getPrix() { return prix; }
    public int getPlacesDisponibles() { return placesDisponibles; }
    
    public void setPlacesDisponibles(int places) { this.placesDisponibles = places; }
    
    @Override
    public String toString() {
        return String.format("%s: %s → %s (%s)", numeroVol, aeroportDepart, aeroportArrivee, compagnie);
    }
}