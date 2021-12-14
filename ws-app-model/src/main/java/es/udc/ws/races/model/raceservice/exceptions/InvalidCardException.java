package es.udc.ws.races.model.raceservice.exceptions;

public class InvalidCardException extends Exception{

    public InvalidCardException(){
        super("Card is not valid or does not match with which you have made the payment");

    }
}
