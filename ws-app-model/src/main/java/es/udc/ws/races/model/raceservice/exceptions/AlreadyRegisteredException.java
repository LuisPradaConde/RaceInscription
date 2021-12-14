package es.udc.ws.races.model.raceservice.exceptions;

public class AlreadyRegisteredException extends Exception{
    public AlreadyRegisteredException(String e) {
        super("Participant is already registered in this race " + e);
    }
}
