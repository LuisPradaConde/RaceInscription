package es.udc.ws.races.client.service.thrift;

import es.udc.ws.races.client.service.dto.ClientRaceDto;
import es.udc.ws.races.thrift.ThriftRaceDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientRaceDtoToThriftRaceDtoConversor {

    public static ThriftRaceDto toThriftRaceDto(ClientRaceDto clientRaceDto){
        Long raceId = clientRaceDto.getRaceId();
        return new ThriftRaceDto(
                raceId == null ? -1 : raceId.longValue(),
                clientRaceDto.getCity(),
                clientRaceDto.getDescription(),
                clientRaceDto.getDateRace().toString(),
                clientRaceDto.getPrice(),
                clientRaceDto.getMaxParticipants(),
                clientRaceDto.getNParticipants());
    }

    public static List<ClientRaceDto> toClientRaceDto (List<ThriftRaceDto> races){
        List<ClientRaceDto> clientRaceDtos = new ArrayList<>(races.size());

        for (ThriftRaceDto race : races){
            clientRaceDtos.add(toClientRaceDto(race));
        }
        return clientRaceDtos;
    }

    public  static ClientRaceDto toClientRaceDto (ThriftRaceDto r){
        return new ClientRaceDto(
                r.getRaceId(),
                r.getCity(),
                r.getDescription(),
                LocalDateTime.parse(r.getDateRace()),
                (float) r.getPrice(),
                r.getMaxParticipants(),
                r.getNParticipants());
    }
}
