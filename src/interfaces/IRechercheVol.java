package interfaces;

import java.util.List;
import java.time.LocalDate;
import modeles.Vol;

public interface IRechercheVol {
    List<Vol> rechercherParDestination(String destination);
    List<Vol> rechercherParDate(LocalDate date);
    List<Vol> rechercherParPrix(double prixMax);
    List<Vol> rechercherParCompagnie(String compagnie);
}
