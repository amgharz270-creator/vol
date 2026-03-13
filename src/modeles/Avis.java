package modeles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Avis {
    private int idAvis;
    private int idClient;
    private String nomClient;
    private String numeroVol;
    private int note;
    private String commentaire;
    private LocalDateTime dateAvis;
    
    public Avis(int idAvis, int idClient, String nomClient, String numeroVol, 
                int note, String commentaire) {
        this.idAvis = idAvis;
        this.idClient = idClient;
        this.nomClient = nomClient;
        this.numeroVol = numeroVol;
        this.note = note;
        this.commentaire = commentaire;
        this.dateAvis = LocalDateTime.now();
    }
    
    public int getIdAvis() { return idAvis; }
    public int getIdClient() { return idClient; }
    public String getNomClient() { return nomClient; }
    public String getNumeroVol() { return numeroVol; }
    public int getNote() { return note; }
    public String getCommentaire() { return commentaire; }
    public LocalDateTime getDateAvis() { return dateAvis; }
    
    public String getStars() {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < note; i++) stars.append("★");
        for (int i = note; i < 5; i++) stars.append("☆");
        return stars.toString();
    }
    
    public String getDateFormatee() {
        return dateAvis.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
