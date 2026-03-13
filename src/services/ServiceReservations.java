package services;

import java.util.*;
import java.util.stream.Collectors;
import modeles.Client;
import modeles.Vol;
import modeles.Reservation;
import modeles.Passager;
import java.time.LocalDate;

public class ServiceReservations {
    private List<Reservation> reservations;
    private static int compteurId = 1000;
    private static ServiceReservations instance;
    
    private ServiceReservations() {
        this.reservations = new ArrayList<>();
    }
    
    public static ServiceReservations getInstance() {
        if (instance == null) {
            instance = new ServiceReservations();
        }
        return instance;
    }
    
    public Reservation creerReservation(Client client, Vol vol) {
        Reservation nouvelle = new Reservation(++compteurId, client, vol);
        reservations.add(nouvelle);
        return nouvelle;
    }
    
    public boolean confirmerReservation(Reservation reservation) {
        return reservation.reserver();
    }
    
    public boolean annulerReservation(Reservation reservation) {
        return reservation.annuler();
    }
    
    /**
     * Récupère les réservations d'un client spécifique
     */
    public List<Reservation> getReservationsClient(Client client) {
        if (client == null) return new ArrayList<>();
        
        return reservations.stream()
                .filter(r -> r.getClient() != null && r.getClient().getId() == client.getId())
                .collect(Collectors.toList());
    }
    
    /**
     * Initialise des réservations de test pour un client
     */
    public void initialiserReservationsTest(Client client) {
        if (client == null) return;
        
        ServiceVols serviceVols = ServiceVols.getInstance();
        
        Vol vol1 = serviceVols.trouverVolParNumero("AF1234");
        Vol vol2 = serviceVols.trouverVolParNumero("BA0289");
        Vol vol3 = serviceVols.trouverVolParNumero("EK0001");
        
        if (vol1 != null) {
            Reservation r1 = new Reservation(++compteurId, client, vol1);
            r1.ajouterPassager(new Passager(client.getNom(), client.getPrenom(), 
                LocalDate.now(), "AB123456", "Economique"));
            r1.reserver();
            reservations.add(r1);
        }
        
        if (vol2 != null) {
            Reservation r2 = new Reservation(++compteurId, client, vol2);
            r2.ajouterPassager(new Passager(client.getNom(), client.getPrenom(), 
                LocalDate.now(), "CD789012", "Business"));
            r2.reserver();
            reservations.add(r2);
        }
        
        if (vol3 != null) {
            Reservation r3 = new Reservation(++compteurId, client, vol3);
            r3.ajouterPassager(new Passager(client.getNom(), client.getPrenom(), 
                LocalDate.now(), "EF345678", "Première"));
            r3.reserver();
            reservations.add(r3);
        }
    }
    
    public List<Reservation> getToutesLesReservations() {
        return new ArrayList<>(reservations);
    }
    
    public Reservation trouverReservationParId(int id) {
        return reservations.stream()
                .filter(r -> r.getIdReservation() == id)
                .findFirst()
                .orElse(null);
    }
    
    public int getNombreTotalPassagers() {
        return reservations.stream()
                .filter(r -> r.getStatut().equals("CONFIRMEE"))
                .mapToInt(r -> r.getPassagers().size())
                .sum();
    }
    
    public double getRevenusTotaux() {
        return reservations.stream()
                .filter(r -> r.getStatut().equals("CONFIRMEE"))
                .mapToDouble(Reservation::calculerMontantTotal)
                .sum();
    }
    
    public boolean aClientVoyage(int idClient, String numeroVol) {
        return reservations.stream()
                .anyMatch(r -> r.getVol().getNumeroVol().equals(numeroVol) 
                        && r.getStatut().equals("CONFIRMEE"));
    }
}
