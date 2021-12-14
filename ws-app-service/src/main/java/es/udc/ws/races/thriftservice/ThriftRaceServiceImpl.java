package es.udc.ws.races.thriftservice;

import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.raceservice.RaceServiceFactory;
import es.udc.ws.races.model.raceservice.exceptions.*;
;
import es.udc.ws.races.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class ThriftRaceServiceImpl implements ThriftRaceService.Iface{

    @Override
    public long addRace (ThriftRaceDto rDto) throws ThriftInputValidationException {
        Race r = RaceToThriftRaceDtoConversor.toRace(rDto);
        try {
            return RaceServiceFactory.getService().addRace(r).getRaceId();
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public List<ThriftRaceDto> findRaces (String date, String city) throws ThriftInputValidationException {

        if (city.trim().isEmpty()) {
            throw new ThriftInputValidationException("Parameter 'city' is mandatory");
        } else {
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                List<Race> races = RaceServiceFactory.getService().findRaces(LocalDateTime.parse(date,formatter),city);
                return RaceToThriftRaceDtoConversor.toThriftRaceDtos(races);

            } catch (InputValidationException e) {
                throw new ThriftInputValidationException(e.getMessage());
            }
        }
    }

    @Override
    public long addInscription(String email, String card, long raceId) throws ThriftInputValidationException, ThriftRaceException,
            ThriftInscriptionDateException, ThriftMaxParticipantionException, ThriftAlreadyRegistered {
        try {
            return RaceServiceFactory.getService().addInscription(email,card,raceId);
        } catch (InscriptionDateException e) {
            throw new ThriftInscriptionDateException(e.getMessage());
        } catch (RaceException e) {
            throw new ThriftRaceException(e.getMessage());
        } catch (MaxParticipationException e) {
            throw new ThriftMaxParticipantionException(e.getMessage());
        } catch (AlreadyRegisteredException e) {
            throw new ThriftAlreadyRegistered(e.getMessage());
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }


    @Override
    public List<ThriftInscriptionDto> findInscriptions (String email) throws ThriftInputValidationException {
        List<Inscription> i;

        try{
            i = RaceServiceFactory.getService().findInscriptions(email);
            return InscriptionToThriftInscriptionDtoConversor.toThriftInscriptionDtos(i);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public ThriftRaceDto findRace(long raceId) throws ThriftInstanceNotFoundException {

        try {
            Race r = RaceServiceFactory.getService().findRace(raceId);
            return RaceToThriftRaceDtoConversor.toThriftRaceDto(r);
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        }
    }

    @Override
    public void pickUpDorsal(long inscriptionId, String card) throws ThriftInscriptionNotFoundException, ThriftAlreadyPickedException, ThriftInvalidCardException {

        try {
            RaceServiceFactory.getService().pickUpDorsal(inscriptionId, card);
        } catch (InscriptionNotFoundException e) {
            throw new ThriftInscriptionNotFoundException(e.getMessage());
        } catch (AlreadyPickedUpException e) {
            throw new ThriftAlreadyPickedException(e.getMessage());
        } catch (InvalidCardException e) {
            throw new ThriftInvalidCardException(e.getMessage());
        }
    }



}
