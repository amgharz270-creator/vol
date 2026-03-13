package interfaces;
public interface IPayable {
    boolean effectuerPaiement(double montant);
    boolean rembourser(double montant);
    String getStatutPaiement();
}