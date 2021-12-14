package es.udc.ws.races.client.service;

import es.udc.ws.races.client.service.dto.ClientInscriptionDto;
import es.udc.ws.races.client.service.dto.ClientRaceDto;
import es.udc.ws.races.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;


public interface ClientRaceService {

    Long addRace(ClientRaceDto r) throws InputValidationException;

    ClientRaceDto findRace(Long raceId) throws InstanceNotFoundException;

    List<ClientRaceDto> findRaces(String date, String city);

    Long addInscription(String email, String card, Long raceId) throws InputValidationException,InstanceNotFoundException,RaceException, InscriptionDateException,
            MaxParticipationException, AlreadyRegisteredException;

    List<ClientInscriptionDto> findInscriptions (String email) throws InputValidationException;

    void pickUpDorsal(Long inscriptionId, String card) throws InstanceNotFoundException, AlreadyPickedUp, InvalidCard;
}
