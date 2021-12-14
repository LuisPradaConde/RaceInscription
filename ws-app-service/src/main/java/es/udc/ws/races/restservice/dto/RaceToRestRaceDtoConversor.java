package es.udc.ws.races.restservice.dto;

import java.util.ArrayList;
import java.util.List;
import es.udc.ws.races.model.race.Race;

public class RaceToRestRaceDtoConversor {

    public static List<RestRaceDto> toRestRaceDtos (List<Race> r){
        List <RestRaceDto> dtos = new ArrayList<>(r.size());
        for (Race race : r){
            dtos.add(toRestRaceDto(race));
        }
        return dtos;
    }

    public static RestRaceDto toRestRaceDto (Race r){
        return new RestRaceDto(r.getRaceId(),r.getCity(), r.getDescription(), r.getDateRace(), r.getPrice(), r.getMaxParticipants(), r.getNParticipants());
    }

    public static Race toRace (RestRaceDto r){
        return new Race(r.getRaceId(),r.getCity(), r.getDescription(), r.getDateRace(), r.getPrice(), r.getMaxParticipants(), r.getNParticipants());
    }

}
