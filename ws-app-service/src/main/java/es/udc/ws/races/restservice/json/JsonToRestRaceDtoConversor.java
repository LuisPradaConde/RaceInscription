package es.udc.ws.races.restservice.json;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.races.restservice.dto.RestRaceDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;



public class JsonToRestRaceDtoConversor {

    public static ObjectNode toObjectNode(RestRaceDto r) {

        ObjectNode raceObject = JsonNodeFactory.instance.objectNode();

        if (r.getRaceId() != null) {
            raceObject.put("raceId", r.getRaceId());
        }
        raceObject.put("city", r.getCity()).
                    put("description", r.getDescription()).
                    put("dateRace", convertLocalDateTime(r.getDateRace())).
                    put("price", r.getPrice()).
                    put("maxParticipants", r.getMaxParticipants()).
                    put("nParticipants", r.getNParticipants());
        return raceObject;
    }

    private static String convertLocalDateTime(LocalDateTime ldt) {
        final String PATTERN = "dd-MM-yyyy HH:mm:ss";
        final DateTimeFormatter LDT_FOMATTER = DateTimeFormatter.ofPattern(PATTERN);
        return LDT_FOMATTER.format(ldt);
    }

    public static ArrayNode toArrayNode(List<RestRaceDto> r) {

        ArrayNode raceNode = JsonNodeFactory.instance.arrayNode();
        for (RestRaceDto raceDto : r) {
            ObjectNode raceObject = toObjectNode(raceDto);
            raceNode.add(raceObject);
        }

        return raceNode;
    }

    public static RestRaceDto toServiceRaceDto(InputStream jsonRace) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRace);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode raceObject = (ObjectNode) rootNode;

                JsonNode raceIdNode = raceObject.get("raceId");
                Long raceId = (raceIdNode != null) ? raceIdNode.longValue() : null;

                String city = raceObject.get("city").textValue().trim();
                String description = raceObject.get("description").textValue().trim();
                String dateRace = raceObject.get("dateRace").textValue();
                Float price = raceObject.get("price").floatValue();
                Integer maxParticipants = raceObject.get("maxParticipants").intValue();
                Integer nParticipants =  raceObject.get("nParticipants").intValue();

                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime raceDate = LocalDateTime.parse(dateRace, format);

                return new RestRaceDto(raceId, city, description, raceDate, price, maxParticipants, nParticipants);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}
