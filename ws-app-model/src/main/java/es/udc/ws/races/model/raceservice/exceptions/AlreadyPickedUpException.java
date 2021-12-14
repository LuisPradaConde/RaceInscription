package es.udc.ws.races.model.raceservice.exceptions;

public class AlreadyPickedUpException extends Exception{

    private Long inscriptionId;
    private String card;

    public AlreadyPickedUpException( Long inscriptionId, String card){
        super("The participation with ID= \"" + inscriptionId +
                "\" paid with the card **** **** **** \"" + card.substring(11,15) +
                "\" has already been collected");
        this.card=card;
        this.inscriptionId=inscriptionId;
    }

    public Long getinscriptionId() {
        return inscriptionId;
    }

    public String getCard() {
        return card;
    }

    public void setinscriptionId(Long partID) {
        this.inscriptionId = inscriptionId;
    }

    public void setCard(String card) {
        this.card = card;
    }
}

