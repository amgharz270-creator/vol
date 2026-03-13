package interfaces;

import modeles.Avis;
import java.util.List;

public interface IReviewable {
    void ajouterAvis(Avis avis);
    List<Avis> getAvis();
    double getNoteMoyenne();
    boolean peutLaisserAvis(int idClient);
}
