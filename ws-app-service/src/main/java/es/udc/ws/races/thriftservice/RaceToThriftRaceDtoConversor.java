package es.udc.ws.races.thriftservice;

import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.thrift.ThriftRaceDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RaceToThriftRaceDtoConversor {

    public static Race toRace(ThriftRaceDto r) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return new Race(r.getRaceId(),r.getCity(), r.getDescription(),
                LocalDateTime.parse(r.getDateRace(), formatter), (float) r.getPrice(), r.getMaxParticipants(),
                r.getNParticipants());
    }

    public static List<ThriftRaceDto> toThriftRaceDtos(List<Race> races) {
        List<ThriftRaceDto> dtos = new ArrayList<>(races.size());
        for (Race race : races) {
            dtos.add(toThriftRaceDto(race));
        }
        return dtos;
    }

    public static ThriftRaceDto toThriftRaceDto(Race r) {
        return new ThriftRaceDto(r.getRaceId(), r.getCity(), r.getDescription(),
                r.getDateRace().toString(), r.getPrice(), r.getMaxParticipants(), r.getNParticipants());
    }



}
