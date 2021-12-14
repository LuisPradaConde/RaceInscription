package es.udc.ws.races.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.races.model.raceservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import com.fasterxml.jackson.databind.JsonNode;
import es.udc.ws.util.exceptions.InputValidationException;

public class JsonToExceptionConversor {

    public static ObjectNode toInputValidationException(InputValidationException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InputValidation");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toInstanceNotFoundException(InstanceNotFoundException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InstanceNotFound");
        exceptionObject.put("instanceId", (ex.getInstanceId() != null) ?
                ex.getInstanceId().toString() : null);
        exceptionObject.put("instanceType",
                ex.getInstanceType().substring(ex.getInstanceType().lastIndexOf('.') + 1));

        return exceptionObject;
    }
    public static ObjectNode toAlreadyPickedUpException(AlreadyPickedUpException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "AlreadyPickedUp");
        exceptionObject.put("message", ex.getMessage());
        return exceptionObject;
    }
    public static ObjectNode toInvalidCardException(InvalidCardException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InvalidCard");
        exceptionObject.put("message", ex.getMessage());
        return exceptionObject;
    }

    public static ObjectNode toInscriptionDateException(InscriptionDateException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","InscriptionDateException");
        exceptionObject.put("message", ex.getMessage());
        return exceptionObject;
    }

    public static ObjectNode toMaxParticipation(MaxParticipationException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","MaxParticipationException");
        exceptionObject.put("message", ex.getMessage());
        return exceptionObject;
    }
    public static ObjectNode toAlreadyRegisteredException(AlreadyRegisteredException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","AlreadyRegisteredException");
        exceptionObject.put("message", ex.getMessage());
        return exceptionObject;
    }
    public static ObjectNode toRaceException(RaceException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","RaceException");
        exceptionObject.put("message", ex.getMessage());
        return exceptionObject;
    }

    public static ObjectNode toInscriptionNotFoundException(InscriptionNotFoundException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","InscriptionNotFoundException");
        exceptionObject.put("message", ex.getMessage());
        return exceptionObject;
    }

}
