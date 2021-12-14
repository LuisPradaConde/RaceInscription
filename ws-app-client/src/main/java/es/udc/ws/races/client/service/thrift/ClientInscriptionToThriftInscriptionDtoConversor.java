package es.udc.ws.races.client.service.thrift;

import es.udc.ws.races.client.service.dto.ClientInscriptionDto;
import es.udc.ws.races.thrift.ThriftInscriptionDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientInscriptionToThriftInscriptionDtoConversor {

    public static ThriftInscriptionDto toThriftInscriptionDto(
            ClientInscriptionDto clientInscriptionDto) {

        Long inscriptionId = clientInscriptionDto.getRaceId();
        return new ThriftInscriptionDto(
                clientInscriptionDto.getInscriptionId() == null ? -1 : clientInscriptionDto.getInscriptionId().longValue(),
                clientInscriptionDto.getEmail(),
                clientInscriptionDto.getCard(),
                clientInscriptionDto.getRaceId(),
                clientInscriptionDto.getDorsal(),
                clientInscriptionDto.getDateInscription().toString(),
                clientInscriptionDto.getDorsalPicked());
    }

    public static List<ClientInscriptionDto> toClientInscriptionDto(List<ThriftInscriptionDto> inscriptions) {

        List<ClientInscriptionDto> clientInscriptionDtos = new ArrayList<>(inscriptions.size());
        for (ThriftInscriptionDto i : inscriptions) {
            clientInscriptionDtos.add(toClientInscriptionDto(i));
        }
        return clientInscriptionDtos;
    }

    public static ClientInscriptionDto toClientInscriptionDto(ThriftInscriptionDto inscription) {
        return new ClientInscriptionDto(
                inscription.getInscriptionId(),
                inscription.getEmail(),
                inscription.getCard(),
                inscription.getRaceId(),
                inscription.getDorsal(),
                LocalDateTime.parse(inscription.getDateInscription()),
                inscription.isDorsalPicked());
    }
}