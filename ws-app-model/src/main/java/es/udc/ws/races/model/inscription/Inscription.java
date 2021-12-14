package es.udc.ws.races.model.inscription;

import java.time.*;
import java.util.Objects;


public class Inscription {
    private Long inscriptionId;
    private String email;
    private String card;
    private Long raceId;
    private Integer dorsal = 0;
    private LocalDateTime dateInscription;
    private Boolean dorsalPicked = false;

    //Constructor
    public Inscription(String email, String card, Long raceId) {
        this.email = email;
        this.card = card;
        this.raceId = raceId;
    }

    public Inscription(Long inscriptionId, String email, String card, Long raceId, Integer dorsal, LocalDateTime dateInscription) {
        this.email = email;
        this.card = card;
        this.raceId = raceId;
        this.inscriptionId = inscriptionId;
        this.dorsal = dorsal;
        this.dateInscription = dateInscription;
    }

    public Inscription(Long inscriptionId, String email, String card, Long raceId, Integer dorsal, LocalDateTime dateInscription, Boolean dorsalPicked) {
        this.inscriptionId = inscriptionId;
        this.email = email;
        this.card = card;
        this.raceId = raceId;
        this.dorsal = dorsal;
        this.dateInscription = dateInscription;
        this.dorsalPicked = dorsalPicked;
    }

    //Getter
    public String getEmail() { return email; }

    public String getCard() {
        return card;
    }

    public Long getRaceId() {
        return raceId;
    }

    public Long getInscriptionId() {
        return inscriptionId;
    }

    public Integer getDorsal() {
        return dorsal;
    }

    public LocalDateTime getDateInscription() { return dateInscription; }

    public Boolean getDorsalPicked() {
        return dorsalPicked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inscription that = (Inscription) o;
        return Objects.equals(inscriptionId, that.inscriptionId) &&
                Objects.equals(email, that.email) &&
                Objects.equals(card, that.card) &&
                Objects.equals(raceId, that.raceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inscriptionId, email, card, raceId);
    }
//Setter

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public void setRaceId(Long raceId) {
        this.raceId = raceId;
    }

    public void setInscriptionId(Long inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public void setDorsal(Integer dorsal) { this.dorsal = dorsal; }

    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }

    public void setDorsalPicked(Boolean dorsalPicked) {
        this.dorsalPicked = dorsalPicked;
    }
}
