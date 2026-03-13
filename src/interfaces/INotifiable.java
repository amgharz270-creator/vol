package interfaces;
public interface INotifiable {
    void envoyerConfirmation(String message);
    void envoyerRappel();
    void envoyerAnnulation(String raison);
}