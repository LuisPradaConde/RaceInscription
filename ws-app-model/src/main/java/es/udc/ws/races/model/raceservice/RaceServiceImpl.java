package es.udc.ws.races.model.raceservice;

import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.model.inscription.SQLInscriptionDao;
import es.udc.ws.races.model.inscription.SqlInscriptionDaoFactory;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.race.SQLRaceDao;
import es.udc.ws.races.model.race.SqlRaceDaoFactory;
import es.udc.ws.races.model.raceservice.exceptions.*;
import es.udc.ws.races.model.util.Validations;
import es.udc.ws.util.exceptions.*;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.*;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static es.udc.ws.races.model.util.ModelConstants.*;


public class RaceServiceImpl implements RaceService {

    private final DataSource dataSource;
    private SQLRaceDao raceDao = null;
    private SQLInscriptionDao inscriptionDao = null;

    public RaceServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(RACE_DATA_SOURCE);
        raceDao = SqlRaceDaoFactory.getDao();
        inscriptionDao = SqlInscriptionDaoFactory.getDao();
    }

    private void raceValidationCheck(Race r) throws InputValidationException {

        PropertyValidator.validateMandatoryString("city",r.getCity());
        PropertyValidator.validateMandatoryString("description",r.getDescription());
        Validations.validateDate("date",r.getDateRace());
        Validations.validatePrice("price",r.getPrice());
        Validations.validateRaceParticipants("participants",r.getMaxParticipants());
    }


    @Override
    public Race addRace(Race r) throws InputValidationException{

        //Validamos la carrera
        raceValidationCheck(r);

        //Le añadimos el valor al campo de fecha de creación con el LocalDateTime actual
        r.setCreationDate(LocalDateTime.now().withNano(0));

        try (Connection connection = dataSource.getConnection()){
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                Race createdRace = raceDao.create(connection, r);
                connection.commit();
                return createdRace;
            }catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            }catch (RuntimeException|Error e){
                connection.rollback();
                throw e;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Race findRace(Long raceID) throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {
            return raceDao.find(connection, raceID);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Race> findRaces(LocalDateTime date, String city) throws InputValidationException {

        Validations.validateDate("date",date);
        try (Connection connection = dataSource.getConnection()){
            return raceDao.findRaces(connection,date,city);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }


    public void inscriptionValidationCheck(String email, String card, Long raceId) throws InputValidationException {

        PropertyValidator.validateCreditCard(card);
        Validations.validateRaceId("raceId", raceId);
       if (!isValidEmail(email)) {
           throw new InputValidationException("Invalid email format");
       }

    }

    private boolean AlreadyRegistered(String email, Long raceId, Connection connection){
        return inscriptionDao.exist(connection,email,raceId);
    }


    @Override
    public Long addInscription(String email, String card, Long raceId) throws InputValidationException, RaceException, InscriptionDateException, MaxParticipationException, AlreadyRegisteredException {

        Inscription inscription = new Inscription(email, card, raceId);
        inscription.setDateInscription(LocalDateTime.now().withNano(0));
        inscriptionValidationCheck(email, card, raceId);
        try (Connection connection = dataSource.getConnection()) {

            try {
                /* Prepare connection. */

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                if (AlreadyRegistered(email, raceId, connection)) {
                    connection.rollback();
                    throw new AlreadyRegisteredException(raceId.toString());
                }
                Race race = raceDao.find(connection, raceId);

                if (inscription.getDateInscription().isAfter(race.getDateRace().minusHours(24))) {
                    connection.rollback();
                    throw new InscriptionDateException();
                }

                if (race.getNParticipants() == race.getMaxParticipants()) {
                    connection.rollback();
                    throw new MaxParticipationException();
                }

                Integer numParticipants = race.getNParticipants();
                race.setNumParticipants(numParticipants + 1);
                inscription.setDorsal(race.getNParticipants());
                raceDao.update(connection, race);
                Inscription created = inscriptionDao.create(connection, inscription);


                /* Commit. */
                connection.commit();

                return created.getInscriptionId();
            } catch (InstanceNotFoundException e) {
                connection.rollback();
                throw new RaceException("This race does not exist");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Inscription> findInscriptions (String email) throws InputValidationException {

        if (!isValidEmail(email)) {
            throw new InputValidationException("Invalid email format");
        } else {

            List<Inscription> found;

            try (Connection connection = dataSource.getConnection()) {
                found = inscriptionDao.findByEmail(connection, email);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return found;
        }

    }


    @Override
    public void pickUpDorsal (Long inscriptionId, String card) throws InscriptionNotFoundException, AlreadyPickedUpException, InvalidCardException {


        try (Connection connection = dataSource.getConnection()){
            try {
                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                //isValidCard(card);
                Inscription i = inscriptionDao.find(connection, inscriptionId);


                if (!card.equals(i.getCard())) {
                    connection.rollback();
                    throw new InvalidCardException();
                } else if (i.getDorsalPicked()) {
                    connection.rollback();
                    throw new AlreadyPickedUpException(inscriptionId, card);
                }
                i.setDorsalPicked(true);
                inscriptionDao.update(connection,i);

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);

            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
}
