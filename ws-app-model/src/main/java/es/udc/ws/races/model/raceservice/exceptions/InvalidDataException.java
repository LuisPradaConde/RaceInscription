package es.udc.ws.races.model.raceservice.exceptions;

public class InvalidDataException extends Exception{


    public InvalidDataException(){
        super("Data not correspond to any registration");
    }
}
