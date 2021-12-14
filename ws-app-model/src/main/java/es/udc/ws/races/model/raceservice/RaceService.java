package es.udc.ws.races.model.raceservice;

import es.udc.ws.races.model.raceservice.exceptions.InvalidDataException;
import es.udc.ws.util.exceptions.*;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.raceservice.exceptions.*;
import es.udc.ws.races.model.inscription.Inscription;

import java.time.LocalDateTime;
import java.util.List;

public interface RaceService {

    Race addRace(Race race) throws InputValidationException;

    Race findRace (Long raceId) throws  InstanceNotFoundException;

    List<Race> findRaces(LocalDateTime date, String city) throws InputValidationException;

    Long addInscription(String email, String card, Long raceId)
            throws InputValidationException, RaceException, InscriptionDateException,
            MaxParticipationException, AlreadyRegisteredException;


    List<Inscription> findInscriptions (String email) throws InputValidationException;

    void pickUpDorsal (Long inscriptionId, String card) throws InscriptionNotFoundException,AlreadyPickedUpException, InvalidCardException;
}
