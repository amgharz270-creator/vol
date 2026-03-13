package services;

import modeles.Reservation;
import java.util.*;

public class ServicePaiement {
    private static ServicePaiement instance;
    private Map<Integer, String> statutPaiements;
    private Map<Integer, String> methodesPaiement;
    private Random random;
    
    private ServicePaiement() {
        this.statutPaiements = new HashMap<>();
        this.methodesPaiement = new HashMap<>();
        this.random = new Random();
    }
    
    public static ServicePaiement getInstance() {
        if (instance == null) {
            instance = new ServicePaiement();
        }
        return instance;
    }
    
    public enum MethodePaiement {
        CARTE_BANCAIRE("💳 Carte Bancaire"),
        PAYPAL("🅿️ PayPal"),
        VIREMENT("🏦 Virement Bancaire"),
        CRYPTO("₿ Cryptomonnaie");
        
        private final String label;
        MethodePaiement(String label) { this.label = label; }
        public String getLabel() { return label; }
    }
    
    public boolean effectuerPaiement(Reservation reservation, MethodePaiement methode) {
        System.out.println("\n💳 Traitement du paiement...");
        System.out.println("Méthode: " + methode.getLabel());
        System.out.println("Montant: " + String.format("%.2f €", reservation.calculerMontantTotal()));
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        boolean succes = random.nextDouble() < 0.95;
        
        if (succes) {
            statutPaiements.put(reservation.getIdReservation(), "PAYÉ");
            methodesPaiement.put(reservation.getIdReservation(), methode.getLabel());
            reservation.effectuerPaiement(reservation.calculerMontantTotal());
            System.out.println("✅ Paiement accepté!");
            return true;
        } else {
            statutPaiements.put(reservation.getIdReservation(), "REFUSÉ");
            System.out.println("❌ Paiement refusé.");
            return false;
        }
    }
    
    public String getStatutPaiement(int idReservation) {
        return statutPaiements.getOrDefault(idReservation, "EN_ATTENTE");
    }
    
    public String getMethodePaiement(int idReservation) {
        return methodesPaiement.getOrDefault(idReservation, "Non spécifié");
    }
}
