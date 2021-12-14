package es.udc.ws.races.client.service.rest.json;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.races.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;


public class JsonToClientExceptionConversor {

    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException{

        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if(rootNode.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                switch (errorType){

                    case "InputValidation":
                        return toInputValidationException(rootNode);

                    case "AlreadyPickedUp":
                        return toAlreadyPickedUpException(rootNode);

                    case "InvalidCard":
                        return toInvalidCardException(rootNode);

                    case "AlreadyRegisteredException":
                        return toAlreadyRegisteredException(rootNode);

                    case "InscriptionDateException":
                        return toInscriptionDateException(rootNode);

                    case "MaxParticipationException":
                        return toMaxParticipationException(rootNode);

                    case "RaceException":
                        return toRaceException(rootNode);

                    case "InscriptionNotFoundException":
                        return toInscriptionNotFoundException(rootNode);

                    case "InstanceNotFound":
                        return toInstanceNotFoundException(rootNode);
                    default:
                        throw new ParsingException("Unrecognized error type: " + errorType);

                }
            }
        } catch (ParsingException e){
            throw e;
        } catch (Exception e){
            throw new ParsingException(e);
        }

    }

    private static AlreadyPickedUp toAlreadyPickedUpException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new AlreadyPickedUp(message);
    }

    private static InvalidCard toInvalidCardException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InvalidCard(message);
    }

    private static AlreadyRegisteredException toAlreadyRegisteredException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new AlreadyRegisteredException(message);
    }

    private static InscriptionDateException toInscriptionDateException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InscriptionDateException(message);
    }

    private static MaxParticipationException toMaxParticipationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new MaxParticipationException(message);
    }

    private static RaceException toRaceException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new RaceException(message);
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }
    private static InscriptionNotFoundException toInscriptionNotFoundException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InscriptionNotFoundException(message);
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId,instanceType);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else if  (errorType.equals("InscriptionNotFoundException")){
                    return toInscriptionNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }








}
