package modeles;

public class Siege {
    private String numeroSiege;
    private String numeroVol;
    private String classe;
    private boolean occupe;
    private Integer idPassager;
    private double prixSupplement;
    
    public Siege(String numeroSiege, String numeroVol, String classe, double prixSupplement) {
        this.numeroSiege = numeroSiege;
        this.numeroVol = numeroVol;
        this.classe = classe;
        this.prixSupplement = prixSupplement;
        this.occupe = false;
        this.idPassager = null;
    }
    
    public String getNumeroSiege() { return numeroSiege; }
    public String getNumeroVol() { return numeroVol; }
    public String getClasse() { return classe; }
    public boolean isOccupe() { return occupe; }
    public void setOccupe(boolean occupe) { this.occupe = occupe; }
    public Integer getIdPassager() { return idPassager; }
    public void setIdPassager(Integer idPassager) { this.idPassager = idPassager; }
    public double getPrixSupplement() { return prixSupplement; }
    
    @Override
    public String toString() {
        return String.format("Siège %s (%s) %s", numeroSiege, classe, occupe ? "[X]" : "[ ]");
    }
}
