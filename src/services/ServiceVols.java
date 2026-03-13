package services;

import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import interfaces.IRechercheVol;
import modeles.Vol;

public class ServiceVols implements IRechercheVol {
    private List<Vol> listeVols;
    private static ServiceVols instance;
    
    private ServiceVols() {
        this.listeVols = new ArrayList<>();
        initialiserVols();
    }
    
    public static ServiceVols getInstance() {
        if (instance == null) {
            instance = new ServiceVols();
        }
        return instance;
    }
    
    private void initialiserVols() {
        LocalDateTime demain = LocalDateTime.now().plusDays(1);
        LocalDateTime dans3jours = LocalDateTime.now().plusDays(3);
        ServiceSieges serviceSieges = ServiceSieges.getInstance();
        
        Vol v1 = new Vol("AF1234", "Air France", "Paris CDG", "New York JFK", 
                        demain, demain.plusHours(8), 450.0, 150, "Economique");
        Vol v2 = new Vol("AF1235", "Air France", "Paris CDG", "New York JFK", 
                        dans3jours, dans3jours.plusHours(8), 520.0, 120, "Business");
        Vol v3 = new Vol("BA0289", "British Airways", "London LHR", "Tokyo HND", 
                        demain, demain.plusHours(12), 890.0, 200, "Economique");
        Vol v4 = new Vol("LH0456", "Lufthansa", "Frankfurt FRA", "Dubai DXB", 
                        dans3jours, dans3jours.plusHours(6), 380.0, 180, "Economique");
        Vol v5 = new Vol("EK0001", "Emirates", "Dubai DXB", "Paris CDG", 
                        LocalDateTime.now().plusDays(5), 
                        LocalDateTime.now().plusDays(5).plusHours(7), 620.0, 300, "Premiere");
        Vol v6 = new Vol("AF1001", "Air France", "Paris CDG", "Tokyo HND", 
                        LocalDateTime.now().plusDays(2), 
                        LocalDateTime.now().plusDays(2).plusHours(11), 780.0, 250, "Business");
        Vol v7 = new Vol("DL2002", "Delta Airlines", "New York JFK", "Paris CDG", 
                        LocalDateTime.now().plusDays(4), 
                        LocalDateTime.now().plusDays(4).plusHours(7), 580.0, 200, "Economique");
        
        listeVols.addAll(Arrays.asList(v1, v2, v3, v4, v5, v6, v7));
        
        // Initialiser les sièges pour chaque vol
        for (Vol v : listeVols) {
            serviceSieges.initialiserSiegesPourVol(v.getNumeroVol(), v.getPlacesDisponibles());
        }
    }
    
    @Override
    public List<Vol> rechercherParDestination(String destination) {
        return listeVols.stream()
                .filter(v -> v.getAeroportArrivee().toLowerCase().contains(destination.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Vol> rechercherParDepart(String depart) {
        return listeVols.stream()
                .filter(v -> v.getAeroportDepart().toLowerCase().contains(depart.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Vol> rechercherParDate(LocalDate date) {
        return listeVols.stream()
                .filter(v -> v.getDateDepart().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Vol> rechercherParPrix(double prixMax) {
        return listeVols.stream()
                .filter(v -> v.getPrix() <= prixMax)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Vol> rechercherParCompagnie(String compagnie) {
        return listeVols.stream()
                .filter(v -> v.getCompagnie().toLowerCase().contains(compagnie.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    // Recherche avancée combinée
    public List<Vol> rechercheAvancee(String depart, String arrivee, LocalDate date, 
                                      Double prixMax, String compagnie, String classe) {
        return listeVols.stream()
                .filter(v -> depart == null || v.getAeroportDepart().toLowerCase().contains(depart.toLowerCase()))
                .filter(v -> arrivee == null || v.getAeroportArrivee().toLowerCase().contains(arrivee.toLowerCase()))
                .filter(v -> date == null || v.getDateDepart().toLocalDate().equals(date))
                .filter(v -> prixMax == null || v.getPrix() <= prixMax)
                .filter(v -> compagnie == null || v.getCompagnie().toLowerCase().contains(compagnie.toLowerCase()))
                .filter(v -> classe == null || v.getClasse().equalsIgnoreCase(classe))
                .sorted(Comparator.comparingDouble(Vol::getPrix))
                .collect(Collectors.toList());
    }
    
    // Filtrer par durée maximale
    public List<Vol> filtrerParDureeMax(int heuresMax) {
        return listeVols.stream()
                .filter(v -> {
                    long heures = ChronoUnit.HOURS.between(v.getDateDepart(), v.getDateArrivee());
                    return heures <= heuresMax;
                })
                .collect(Collectors.toList());
    }
    
    // Recommandations de vols (prix les plus bas)
    public List<Vol> getRecommandationsPrixBas() {
        return listeVols.stream()
                .sorted(Comparator.comparingDouble(Vol::getPrix))
                .limit(5)
                .collect(Collectors.toList());
    }
    
    public void ajouterVol(Vol vol) {
        listeVols.add(vol);
        ServiceSieges.getInstance().initialiserSiegesPourVol(vol.getNumeroVol(), vol.getPlacesDisponibles());
    }
    
    public boolean modifierVol(Vol volModifie) {
        for (int i = 0; i < listeVols.size(); i++) {
            if (listeVols.get(i).getNumeroVol().equals(volModifie.getNumeroVol())) {
                listeVols.set(i, volModifie);
                return true;
            }
        }
        return false;
    }
    
    public boolean supprimerVol(String numeroVol) {
        return listeVols.removeIf(v -> v.getNumeroVol().equals(numeroVol));
    }
    
    public List<Vol> getTousLesVols() {
        return new ArrayList<>(listeVols);
    }
    
    public Vol trouverVolParNumero(String numero) {
        return listeVols.stream()
                .filter(v -> v.getNumeroVol().equals(numero))
                .findFirst()
                .orElse(null);
    }
    
    public List<String> getToutesCompagnies() {
        return listeVols.stream()
                .map(Vol::getCompagnie)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    public List<String> getToutesDestinations() {
        return listeVols.stream()
                .map(Vol::getAeroportArrivee)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}