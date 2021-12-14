package es.udc.ws.races.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.races.client.service.dto.ClientRaceDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientRaceDtoConversor {

    public static ObjectNode toObjectNode(ClientRaceDto r) throws IOException{

        ObjectNode raceObject = JsonNodeFactory.instance.objectNode();

        if (r.getRaceId() != null) {
            raceObject.put("raceId", r.getRaceId());
        }
        raceObject.put("city", r.getCity()).
                put("description", r.getDescription()).
                put("dateRace", convertLocalDateTime(r.getDateRace())).
                put("price", r.getPrice()).
                put("maxParticipants", r.getMaxParticipants()).
                put("nParticipants",r.getNParticipants());
        return raceObject;
    }

    private static String convertLocalDateTime(LocalDateTime ldt) {
        final String PATTERN = "dd-MM-yyyy HH:mm:ss";
        final DateTimeFormatter LDT_FOMATTER = DateTimeFormatter.ofPattern(PATTERN);
        return LDT_FOMATTER.format(ldt);
    }

    public static ClientRaceDto toClientRaceDto(InputStream jsonRace) throws ParsingException{
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRace);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientRaceDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }

    }

    public static List<ClientRaceDto> toClientRaceDtos(InputStream jsonRaces) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRaces);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode racesArray = (ArrayNode) rootNode;
                List<ClientRaceDto> dtos = new ArrayList<>(racesArray.size());
                for (JsonNode raceNode : racesArray) {
                    dtos.add(toClientRaceDto(raceNode));
                }
                return dtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientRaceDto toClientRaceDto(JsonNode raceNode) throws ParsingException {
        if (raceNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode raceObject = (ObjectNode) raceNode;

            JsonNode raceIdNode = raceObject.get("raceId");
            Long raceId = (raceIdNode != null) ? raceIdNode.longValue() : null;

            String city = raceObject.get("city").textValue().trim();
            String description = raceObject.get("description").textValue().trim();
            String dateRace = raceObject.get("dateRace").textValue().trim();
            Float price = raceObject.get("price").floatValue();
            Integer maxParticipants = raceObject.get("maxParticipants").intValue();
            Integer NParticipants = raceObject.get("nParticipants").intValue();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime raceDateLocalDateTime = LocalDateTime.parse(dateRace, formatter);

            return new ClientRaceDto(raceId, city, description, raceDateLocalDateTime, price, maxParticipants,
                    NParticipants);
        }
    }







}



