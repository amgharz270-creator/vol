package modeles;

import interfaces.IAuthentifiable;
import interfaces.IAdministrable;
import services.ServiceVols;
import services.ServiceReservations;
import java.util.List;

public class Admin implements IAuthentifiable, IAdministrable {
    private int idAdmin;
    private String nom;
    private String email;
    private String password;
    private boolean connecte;
    private ServiceVols serviceVols;
    private ServiceReservations serviceReservations;
    
    public Admin(int idAdmin, String nom, String email, String password) {
        this.idAdmin = idAdmin;
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.connecte = false;
        this.serviceVols = ServiceVols.getInstance();
        this.serviceReservations = ServiceReservations.getInstance();
    }
    
    @Override
    public boolean seConnecter(String email, String password) {
        if (this.email.equals(email) && this.password.equals(password)) {
            this.connecte = true;
            System.out.println("🔐 Admin connecté: " + this.nom);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean seDeconnecter() {
        this.connecte = false;
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
    
    @Override
    public boolean ajouterVol(Vol vol) {
        serviceVols.ajouterVol(vol);
        return true;
    }
    
    @Override
    public boolean modifierVol(Vol vol) {
        return serviceVols.modifierVol(vol);
    }
    
    @Override
    public boolean supprimerVol(String numeroVol) {
        return serviceVols.supprimerVol(numeroVol);
    }
    
    @Override
    public List<Vol> getTousLesVols() {
        return serviceVols.getTousLesVols();
    }
    
    @Override
    public int getNombreVols() {
        return serviceVols.getTousLesVols().size();
    }
    
    @Override
    public int getNombrePassagers() {
        return serviceReservations.getNombreTotalPassagers();
    }
    
    @Override
    public double getRevenusTotaux() {
        return serviceReservations.getRevenusTotaux();
    }
    
    @Override
    public List<Reservation> getToutesLesReservations() {
        return serviceReservations.getToutesLesReservations();
    }
    
    @Override
    public boolean annulerReservationAdmin(int idReservation) {
        Reservation r = serviceReservations.trouverReservationParId(idReservation);
        if (r != null) {
            return serviceReservations.annulerReservation(r);
        }
        return false;
    }
    
    public String getNom() { return nom; }
    public boolean isConnecte() { return connecte; }
}
