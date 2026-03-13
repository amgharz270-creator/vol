package interfaces;

import modeles.Siege;
import java.util.List;

public interface IGestionSieges {
    List<Siege> getSiegesDisponibles(String numeroVol);
    boolean reserverSiege(String numeroVol, String numeroSiege, int idPassager);
    boolean libererSiege(String numeroVol, String numeroSiege);
    Siege getSiegeByNumero(String numeroVol, String numeroSiege);
    String genererPlanSieges(String numeroVol);
}
