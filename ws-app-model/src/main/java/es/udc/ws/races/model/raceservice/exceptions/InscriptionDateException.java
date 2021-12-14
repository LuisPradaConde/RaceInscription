package es.udc.ws.races.model.raceservice.exceptions;

public class InscriptionDateException extends Exception {

    public InscriptionDateException(){
        super("Registration period has already closed");

    }
}
