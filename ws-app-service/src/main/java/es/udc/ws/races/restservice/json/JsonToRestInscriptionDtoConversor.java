package es.udc.ws.races.restservice.json;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.races.restservice.dto.RestInscriptionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;



public class JsonToRestInscriptionDtoConversor {

    public static ObjectNode toObjectNode(RestInscriptionDto i) {

        ObjectNode inscriptionObject = JsonNodeFactory.instance.objectNode();

        if (i.getInscriptionId() != null) {
            inscriptionObject.put("inscriptionId", i.getInscriptionId());
        }
        if (i.getRaceId() != null){
            inscriptionObject.put("email", i.getEmail()).
                    put("card", i.getCard()).
                    put("raceId", i.getRaceId()).
                    put("dorsal", i.getDorsal()).
                    put("dateInscription", i.getDateInscription()).
                    put("dorsalPicked", i.getDorsalPicked());
        }

        return inscriptionObject;
    }

    public static ArrayNode toArrayNode(List<RestInscriptionDto> i) {

        ArrayNode inscriptionNode = JsonNodeFactory.instance.arrayNode();
        for (int x = 0; x < i.size(); x++) {
            RestInscriptionDto inscriptionDto = i.get(x);
            ObjectNode inscriptionObject = toObjectNode(inscriptionDto);
            inscriptionNode.add(inscriptionObject);
        }

        return inscriptionNode;
    }

    public static RestInscriptionDto toServiceInscriptionDto(InputStream jsonInscription) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscription);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode inscriptionObject = (ObjectNode) rootNode;

                JsonNode inscriptionIdNode = inscriptionObject.get("inscriptionId");
                Long inscriptionId = (inscriptionIdNode != null) ? inscriptionIdNode.longValue() : null;

                String email = inscriptionObject.get("email").textValue().trim();
                String card = inscriptionObject.get("card").textValue().trim();
                Long raceId = inscriptionObject.get("raceId").longValue();
                Integer dorsal = inscriptionObject.get("dorsal").intValue();
                LocalDateTime dateInscription = LocalDateTime.parse(inscriptionObject.get("dateInscription").textValue().trim());
                Boolean dorsalPicked =  inscriptionObject.get("dorsalPicked").asBoolean();

                return new RestInscriptionDto(inscriptionId, email, card, raceId, dorsal, dateInscription.toString(), dorsalPicked);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}
