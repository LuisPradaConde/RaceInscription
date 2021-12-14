package es.udc.ws.races.model.raceservice.exceptions;

public class MaxParticipationException extends Exception{

    public MaxParticipationException(){
        super("Race capacity is full");

    }
}
