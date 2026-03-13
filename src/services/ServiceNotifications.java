package services;

import modeles.Client;
import modeles.Reservation;
import java.util.*;

public class ServiceNotifications {
    private static ServiceNotifications instance;
    private List<String> historiqueNotifications;
    
    private ServiceNotifications() {
        this.historiqueNotifications = new ArrayList<>();
    }
    
    public static ServiceNotifications getInstance() {
        if (instance == null) {
            instance = new ServiceNotifications();
        }
        return instance;
    }
    
    public void envoyerConfirmationReservation(Client client, Reservation reservation) {
        String message = String.format(
            "✈️ Confirmation de réservation\n" +
            "Cher %s,\n" +
            "Votre réservation #%d est confirmée!\n" +
            "Vol: %s | %s → %s\n" +
            "Date: %s\n" +
            "Montant: %.2f €\n" +
            "Merci de voyager avec nous!",
            client.getPrenom(),
            reservation.getIdReservation(),
            reservation.getVol().getNumeroVol(),
            reservation.getVol().getAeroportDepart(),
            reservation.getVol().getAeroportArrivee(),
            reservation.getVol().getDateDepart().toLocalDate(),
            reservation.calculerMontantTotal()
        );
        
        System.out.println("\n📧 EMAIL ENVOYÉ À: " + client.getEmail());
        System.out.println("═══════════════════════════════════════");
        System.out.println(message);
        System.out.println("═══════════════════════════════════════\n");
        
        historiqueNotifications.add("[EMAIL] " + client.getEmail() + " - Confirmation #" + reservation.getIdReservation());
    }
    
    public void envoyerRappelVol(Client client, Reservation reservation) {
        String message = String.format(
            "⏰ Rappel: Votre vol %s décolle demain à %s!",
            reservation.getVol().getNumeroVol(),
            reservation.getVol().getDateDepart().toLocalTime()
        );
        System.out.println("\n📧 RAPPEL: " + client.getEmail());
        System.out.println(message);
    }
    
    public List<String> getHistoriqueNotifications() {
        return new ArrayList<>(historiqueNotifications);
    }
}
