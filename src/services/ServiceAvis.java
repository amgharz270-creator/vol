package services;

import modeles.Avis;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceAvis {
    private List<Avis> avisList;
    private static int compteurId = 1;
    private static ServiceAvis instance;
    private ServiceReservations serviceReservations;
    
    private ServiceAvis() {
        this.avisList = new ArrayList<>();
        this.serviceReservations = ServiceReservations.getInstance();
    }
    
    public static ServiceAvis getInstance() {
        if (instance == null) {
            instance = new ServiceAvis();
        }
        return instance;
    }
    
    public void ajouterAvis(int idClient, String nomClient, String numeroVol, int note, String commentaire) {
        if (peutLaisserAvis(idClient, numeroVol)) {
            Avis avis = new Avis(compteurId++, idClient, nomClient, numeroVol, note, commentaire);
            avisList.add(avis);
        }
    }
    
    public boolean peutLaisserAvis(int idClient, String numeroVol) {
        return serviceReservations.aClientVoyage(idClient, numeroVol);
    }
    
    public List<Avis> getAvisParVol(String numeroVol) {
        return avisList.stream()
                .filter(a -> a.getNumeroVol().equals(numeroVol))
                .sorted((a1, a2) -> a2.getDateAvis().compareTo(a1.getDateAvis()))
                .collect(Collectors.toList());
    }
    
    public double getNoteMoyenne(String numeroVol) {
        List<Avis> avisVol = getAvisParVol(numeroVol);
        if (avisVol.isEmpty()) return 0.0;
        return avisVol.stream().mapToInt(Avis::getNote).average().orElse(0.0);
    }
    
    public List<Avis> getTousLesAvis() {
        return new ArrayList<>(avisList);
    }
}