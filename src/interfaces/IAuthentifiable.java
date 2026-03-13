package interfaces;
public interface IAuthentifiable {
    boolean seConnecter(String email, String password);
    boolean seDeconnecter();
    boolean changerMotDePasse(String ancien, String nouveau);
}