package modeles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import interfaces.IReservable;
import interfaces.IPayable;

public class Reservation implements IReservable, IPayable {
    private int idReservation;
    private Client client;
    private Vol vol;
    private LocalDateTime dateReservation;
    private String statut; // CONFIRMEE, EN_ATTENTE, ANNULEE
    private double montantPaye;
    private boolean paye;
    private List<Passager> passagers;
    
    public Reservation(int id, Client client, Vol vol) {
        this.idReservation = id;
        this.client = client;
        this.vol = vol;
        this.dateReservation = LocalDateTime.now();
        this.statut = "EN_ATTENTE";
        this.montantPaye = 0;
        this.paye = false;
        this.passagers = new ArrayList<>();
    }
    
    // Implémentation IReservable
    @Override
    public boolean reserver() {
        if (vol.getPlacesDisponibles() >= passagers.size()) {
            vol.setPlacesDisponibles(vol.getPlacesDisponibles() - passagers.size());
            this.statut = "CONFIRMEE";
            client.ajouterReservation(this);
            client.envoyerConfirmation("Votre réservation " + idReservation + " est confirmée!");
            return true;
        }
        return false;
    }
    
    @Override
    public boolean annuler() {
        if (this.statut.equals("CONFIRMEE")) {
            vol.setPlacesDisponibles(vol.getPlacesDisponibles() + passagers.size());
            this.statut = "ANNULEE";
            if (paye) {
                rembourser(montantPaye);
            }
            client.envoyerAnnulation("Votre réservation a été annulée sur demande");
            return true;
        }
        return false;
    }
    
    @Override
    public boolean modifier() {
        // Logique de modification (changement de date, vol, etc.)
        if (this.statut.equals("CONFIRMEE")) {
            client.envoyerConfirmation("Votre réservation " + idReservation + " a été modifiée");
            return true;
        }
        return false;
    }
    
    @Override
    public boolean verifierDisponibilite() {
        return vol.getPlacesDisponibles() >= passagers.size();
    }
    
    // Implémentation IPayable
    @Override
    public boolean effectuerPaiement(double montant) {
        // Simulation de paiement
        System.out.println("Paiement de " + montant + "€ effectué par carte bancaire");
        this.montantPaye = montant;
        this.paye = true;
        return true;
    }
    
    @Override
    public boolean rembourser(double montant) {
        System.out.println("Remboursement de " + montant + "€ effectué sur le compte du client");
        this.montantPaye = 0;
        this.paye = false;
        return true;
    }
    
    @Override
    public String getStatutPaiement() {
        return paye ? "PAYE" : "EN_ATTENTE";
    }
    
    // Méthodes supplémentaires
    public void ajouterPassager(Passager p) {
        passagers.add(p);
    }
    
    public double calculerMontantTotal() {
        return vol.getPrix() * passagers.size();
    }
    public Client getClient() {
    return client;
}
    // Getters
    public int getIdReservation() { return idReservation; }
    public String getStatut() { return statut; }
    public Vol getVol() { return vol; }
    public List<Passager> getPassagers() { return passagers; }
}

/**
 * Classe représentant un Passager
 */
