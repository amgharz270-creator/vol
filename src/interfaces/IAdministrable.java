package interfaces;

import java.util.List;
import modeles.Vol;
import modeles.Reservation;

public interface IAdministrable {
    boolean ajouterVol(Vol vol);
    boolean modifierVol(Vol vol);
    boolean supprimerVol(String numeroVol);
    List<Vol> getTousLesVols();
    int getNombreVols();
    int getNombrePassagers();
    double getRevenusTotaux();
    List<Reservation> getToutesLesReservations();
    boolean annulerReservationAdmin(int idReservation);
}