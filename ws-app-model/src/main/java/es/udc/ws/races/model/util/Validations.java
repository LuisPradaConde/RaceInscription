package es.udc.ws.races.model.util;

import es.udc.ws.races.model.race.Race;
import es.udc.ws.util.exceptions.InputValidationException;

import java.time.LocalDateTime;

public class Validations {

    public static void validateDate(String name, LocalDateTime value) throws InputValidationException{

        LocalDateTime actualDate = LocalDateTime.now();
        if ((value == null) || (value.isBefore(actualDate))){
            throw new InputValidationException("Invalid " + name + ",(" + value + ") cannot be a past date");
        }
    }

    public static void validatePrice(String name, Float value) throws InputValidationException {

        if ((value == null) || (value < 0)){
            throw new InputValidationException("Invalid " + name + ",(" + value + ") cannot be null or a negative");
        }
    }

    public static void validateRaceParticipants(String name, Integer value) throws InputValidationException {

        if ((value <= 0)){
            throw new InputValidationException("Invalid " + name + ",(" + value + ") cannot be 0 or a negative");
        }
    }

    public static void validateRaceId(String name, Long value) throws InputValidationException{
        if (( value <= 0 )){
            throw new InputValidationException(" Invalid " + name + " value cannot be null or negative");
        }
    }
}
