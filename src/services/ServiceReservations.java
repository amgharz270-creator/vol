package services;

import java.util.*;
import java.util.stream.Collectors;
import modeles.Client;
import modeles.Vol;
import modeles.Reservation;

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
    
    public List<Reservation> getReservationsClient(Client client) {
        return reservations.stream()
                .filter(r -> r.getVol().equals(client))
                .collect(Collectors.toList());
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