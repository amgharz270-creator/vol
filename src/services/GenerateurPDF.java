package services;

import modeles.Reservation;
import modeles.Passager;
import java.time.format.DateTimeFormatter;

public class GenerateurPDF {
    
    public static String genererBilletPDF(Reservation reservation, Passager passager, String siege) {
        StringBuilder billet = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        billet.append("╔════════════════════════════════════════════════════════════════╗\n");
        billet.append("║                    ✈️  SKYBOOKING AIRLINES                    ║\n");
        billet.append("║                     CARTE D'EMBARQUEMENT                       ║\n");
        billet.append("╠════════════════════════════════════════════════════════════════╣\n");
        billet.append("║                                                                ║\n");
        billet.append(String.format("║  PASSAGER: %-25s CLASSE: %-12s ║\n", 
            passager.getPrenom() + " " + passager.getNom(), 
            passager.getNumeroPasseport().startsWith("B") ? "BUSINESS" : "ECONOMY"));
        billet.append("║                                                                ║\n");
        billet.append(String.format("║  VOL: %-10s %-15s → %-15s      ║\n",
            reservation.getVol().getNumeroVol(),
            reservation.getVol().getAeroportDepart(),
            reservation.getVol().getAeroportArrivee()));
        billet.append("║                                                                ║\n");
        billet.append(String.format("║  DATE: %-15s DÉPART: %-10s ARRIVÉE: %-10s ║\n",
            reservation.getVol().getDateDepart().format(dateFormatter),
            reservation.getVol().getDateDepart().format(timeFormatter),
            reservation.getVol().getDateArrivee().format(timeFormatter)));
        billet.append("║                                                                ║\n");
        billet.append(String.format("║  SIÈGE: %-10s PORTE: %-8s PRIX: %-15s ║\n",
            siege != null ? siege : "Non assigné",
            "A" + (reservation.getIdReservation() % 20 + 1),
            String.format("%.2f €", reservation.getVol().getPrix())));
        billet.append("║                                                                ║\n");
        billet.append("║  ┌─────────────────┐                                           ║\n");
        billet.append(String.format("║  │ ██████████████ │  RÉSERVATION: #%d\n", reservation.getIdReservation()));
        billet.append("║  │ ██          ██ │                                           ║\n");
        billet.append("║  │ ██████████████ │                                           ║\n");
        billet.append("║  │ ██  ████  ████ │                                           ║\n");
        billet.append("║  │ ██████████████ │                                           ║\n");
        billet.append("║  │ ██          ██ │                                           ║\n");
        billet.append("║  │ ██████████████ │                                           ║\n");
        billet.append("║  └─────────────────┘                                           ║\n");
        billet.append("║                                                                ║\n");
        billet.append("╠════════════════════════════════════════════════════════════════╣\n");
        billet.append("║  PRÉSENTEZ CE BILLET À L'ENREGISTREMENT                        ║\n");
        billet.append("╚════════════════════════════════════════════════════════════════╝\n");
        
        return billet.toString();
    }
}
