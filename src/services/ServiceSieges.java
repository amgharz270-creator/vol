package services;

import modeles.Siege;
import interfaces.IGestionSieges;
import java.util.*;

public class ServiceSieges implements IGestionSieges {
    private Map<String, List<Siege>> siegesParVol;
    private static ServiceSieges instance;
    
    private ServiceSieges() {
        this.siegesParVol = new HashMap<>();
    }
    
    public static ServiceSieges getInstance() {
        if (instance == null) {
            instance = new ServiceSieges();
        }
        return instance;
    }
    
    public void initialiserSiegesPourVol(String numeroVol, int capacite) {
        List<Siege> sieges = new ArrayList<>();
        int ecoEnd = (int)(capacite * 0.6);
        int busEnd = (int)(capacite * 0.9);
        
        for (int i = 1; i <= ecoEnd; i++) {
            sieges.add(new Siege("E" + i, numeroVol, "Economique", 0.0));
        }
        for (int i = ecoEnd + 1; i <= busEnd; i++) {
            sieges.add(new Siege("B" + (i - ecoEnd), numeroVol, "Business", 150.0));
        }
        for (int i = busEnd + 1; i <= capacite; i++) {
            sieges.add(new Siege("P" + (i - busEnd), numeroVol, "Premiere", 500.0));
        }
        
        siegesParVol.put(numeroVol, sieges);
    }
    
    @Override
    public List<Siege> getSiegesDisponibles(String numeroVol) {
        List<Siege> disponibles = new ArrayList<>();
        for (Siege s : siegesParVol.getOrDefault(numeroVol, new ArrayList<>())) {
            if (!s.isOccupe()) disponibles.add(s);
        }
        return disponibles;
    }
    
    @Override
    public boolean reserverSiege(String numeroVol, String numeroSiege, int idPassager) {
        Siege siege = getSiegeByNumero(numeroVol, numeroSiege);
        if (siege != null && !siege.isOccupe()) {
            siege.setOccupe(true);
            siege.setIdPassager(idPassager);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean libererSiege(String numeroVol, String numeroSiege) {
        Siege siege = getSiegeByNumero(numeroVol, numeroSiege);
        if (siege != null) {
            siege.setOccupe(false);
            siege.setIdPassager(null);
            return true;
        }
        return false;
    }
    
    @Override
    public Siege getSiegeByNumero(String numeroVol, String numeroSiege) {
        for (Siege s : siegesParVol.getOrDefault(numeroVol, new ArrayList<>())) {
            if (s.getNumeroSiege().equals(numeroSiege)) return s;
        }
        return null;
    }
    
    @Override
    public String genererPlanSieges(String numeroVol) {
        List<Siege> sieges = siegesParVol.getOrDefault(numeroVol, new ArrayList<>());
        StringBuilder plan = new StringBuilder();
        plan.append("╔══════════════════════════════════════╗\n");
        plan.append("║         PLAN DES SIÈGES              ║\n");
        plan.append("╠══════════════════════════════════════╣\n");
        
        String currentClass = "";
        for (Siege s : sieges) {
            if (!s.getClasse().equals(currentClass)) {
                currentClass = s.getClasse();
                plan.append("║ ").append(String.format("%-12s", currentClass.toUpperCase())).append("           ║\n");
            }
            String status = s.isOccupe() ? "[X]" : "[ ]";
            plan.append("║ ").append(String.format("%-4s %s", s.getNumeroSiege(), status));
            if (sieges.indexOf(s) % 4 == 3) plan.append(" ║\n");
        }
        plan.append("╚══════════════════════════════════════╝");
        return plan.toString();
    }
    
    public List<Siege> getSiegesParClasse(String numeroVol, String classe) {
        List<Siege> result = new ArrayList<>();
        for (Siege s : siegesParVol.getOrDefault(numeroVol, new ArrayList<>())) {
            if (s.getClasse().equals(classe)) result.add(s);
        }
        return result;
    }
}
