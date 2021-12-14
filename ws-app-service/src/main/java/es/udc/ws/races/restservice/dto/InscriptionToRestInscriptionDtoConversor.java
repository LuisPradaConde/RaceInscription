package es.udc.ws.races.restservice.dto;

import es.udc.ws.races.model.inscription.Inscription;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InscriptionToRestInscriptionDtoConversor {

    public static List<RestInscriptionDto> toRaceInscriptionDtos (List<Inscription> i){
        List <RestInscriptionDto> dtos = new ArrayList<>(i.size());
        for (Inscription inscription : i){
            dtos.add(toRestInscriptionDto(inscription));
        }
        return dtos;
    }

    public static RestInscriptionDto toRestInscriptionDto (Inscription i){
        return new RestInscriptionDto(i.getInscriptionId(),i.getEmail(), i.getCard(), i.getRaceId(), i.getDorsal(), i.getDateInscription().toString(), i.getDorsalPicked());
    }

    public static Inscription toInscription (RestInscriptionDto i){
        return new Inscription(i.getInscriptionId(),i.getEmail(), i.getCard(), i.getRaceId(), i.getDorsal(), LocalDateTime.parse(i.getDateInscription()), i.getDorsalPicked());
    }
}
