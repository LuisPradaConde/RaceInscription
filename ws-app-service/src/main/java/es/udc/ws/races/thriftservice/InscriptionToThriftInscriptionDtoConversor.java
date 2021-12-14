package es.udc.ws.races.thriftservice;

import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.thrift.ThriftInscriptionDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class InscriptionToThriftInscriptionDtoConversor {

    public static Inscription toInscription(ThriftInscriptionDto inscriptionDto) {
        return new Inscription(inscriptionDto.getInscriptionId(),  inscriptionDto.getEmail(),
                inscriptionDto.getCard(), inscriptionDto.getRaceId(),inscriptionDto.getDorsal(), LocalDateTime.parse(inscriptionDto.getDateInscription()),
                inscriptionDto.isDorsalPicked());
    }

    public static List<ThriftInscriptionDto> toThriftInscriptionDtos(List<Inscription> inscriptions) {

        List<ThriftInscriptionDto> dtos = new ArrayList<>(inscriptions.size());
        for (Inscription i : inscriptions) {
            dtos.add(toThriftInscriptionDto(i));
        }
        return dtos;
    }

    private static ThriftInscriptionDto toThriftInscriptionDto(Inscription i) {

        return new ThriftInscriptionDto(i.getInscriptionId(), i.getEmail(),
                i.getCard(), i.getRaceId(), i.getDorsal(), i.getDateInscription().toString(),
                i.getDorsalPicked());
    }
}
