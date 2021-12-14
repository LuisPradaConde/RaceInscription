package es.udc.ws.races.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.races.client.service.dto.ClientInscriptionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class JsonToClientInscriptionDtoConversor {

    public static ClientInscriptionDto toClientInscriptionDto(InputStream jsonInscription) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscription);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientInscriptionDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }

    }

    public static List<ClientInscriptionDto> toClientInscriptionDtos(InputStream jsonInscription) throws ParsingException {

        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscription);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode inscriptionsArray = (ArrayNode) rootNode;
                List<ClientInscriptionDto> dtos = new ArrayList<>(inscriptionsArray.size());
                for (JsonNode inscriptionNode : inscriptionsArray) {
                    dtos.add(toClientInscriptionDto(inscriptionNode));
                }

                return dtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientInscriptionDto toClientInscriptionDto(JsonNode inscriptionNode) throws ParsingException{
        if (inscriptionNode.getNodeType() != JsonNodeType.OBJECT){
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode inscriptionObject = (ObjectNode) inscriptionNode;

            JsonNode inscriptionIdNode = inscriptionObject.get("inscriptionId");
            Long inscriptionId = (inscriptionIdNode != null) ? inscriptionIdNode.longValue() : null;

            if (inscriptionObject.get("raceId") != null) {
                String email = inscriptionObject.get("email").textValue().trim();
                String card = inscriptionObject.get("card").textValue().trim();
                Long raceId = inscriptionObject.get("raceId").longValue();
                Integer dorsal = inscriptionObject.get("dorsal").intValue();
                String dateInscription = inscriptionObject.get("dateInscription").textValue().trim();
                Boolean dorsalPicked = inscriptionObject.get("dorsalPicked").asBoolean();

                return new ClientInscriptionDto(inscriptionId,email,card,raceId,dorsal,LocalDateTime.parse(dateInscription),dorsalPicked);
            }
            return new ClientInscriptionDto(inscriptionId);
        }
    }
}