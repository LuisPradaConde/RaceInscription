package es.udc.ws.races.model.inscription;

import java.sql.Connection;

import es.udc.ws.races.model.raceservice.exceptions.InscriptionNotFoundException;
import es.udc.ws.util.exceptions.*;
import java.util.*;



public interface SQLInscriptionDao {
    
    Inscription create (Connection connection, Inscription inscription);

    List <Inscription> findByEmail (Connection connection, String email);

    Inscription find(Connection connection, Long inscriptionId) throws InscriptionNotFoundException;

    void update (Connection connection, Inscription inscription)
            throws InscriptionNotFoundException;

    public void remove (Connection connection, Long partId)
            throws InscriptionNotFoundException;

    boolean exist(Connection connection, String email, Long raceId);

}
