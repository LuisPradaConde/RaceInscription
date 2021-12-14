package es.udc.ws.races.test.model.raceservice;
import static es.udc.ws.races.model.util.ModelConstants.RACE_DATA_SOURCE;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;


import es.udc.ws.races.model.raceservice.exceptions.*;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import es.udc.ws.races.model.raceservice.*;
import es.udc.ws.races.model.race.*;
import es.udc.ws.races.model.inscription.*;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.exceptions.InputValidationException;

import javax.sql.DataSource;

public class RaceServiceTest {


    private final long NON_EXISTENT_PART_ID = -1;
    private final long NON_EXISTENT_RACE_ID = 5;
    private final long NON_EXISTENT_INSCRIPTION_ID = -1;

    private static RaceService raceService = null;
    private static SQLInscriptionDao inscriptionDao = null;
    private static SQLRaceDao raceDao = null;

    private final String VALID_EMAIL = "example@gmail.com";
    private final String INVALID_EMAIL = "examplegmail.com";
    private final String VALID_CARD = "1234567891011121";
    private final String INVALID_CARD = "4184719878174917284234";



    @BeforeAll
    public static void init() {

        /*
         * Create a simple data source and add it to "DataSourceLocator" (this
         * is needed to test "es.udc.ws.races.model.raceservice.RaceService"
         */
        DataSource dataSource = new SimpleDataSource();

        /* Add "dataSource" to "DataSourceLocator". */
        DataSourceLocator.addDataSource(RACE_DATA_SOURCE, dataSource);

        raceService = RaceServiceFactory.getService();
        raceDao = SqlRaceDaoFactory.getDao();
        inscriptionDao = SqlInscriptionDaoFactory.getDao();

    }

    private Race getValidRace(String city){return new Race(city,"La carrera tendrá premio para los 3 primeros clasificados", LocalDateTime.of(2022,12,31,10,30,00).withNano(0), (float)5.99,150);}

    private Race getAnotherValidRace(String city){return new Race(city,"Descripcion de Prueba", LocalDateTime.of(2024,1,1,10,30,00).withNano(0), (float)35,110);}

    private Race getValidRace() { return getValidRace("vigo");}

    private Race getInvalidRace() {
        return new Race("Vigo","Carrera de famosos", LocalDateTime.now().plusHours(15).withNano(0),(float) 3, 3000);
    }

    private Race getRaceMaxParticipants(){
        return new Race("Vigo","Descripcion",LocalDateTime.now().plusYears(2).withNano(0),(float) 5, 1);
    }

    private Inscription getValidInscription(Long raceId) {return new Inscription("example@gmail.com","1234567891011121",raceId);}

    private void removeRace(Long raceId){
        DataSource dataSource = DataSourceLocator.getDataSource(RACE_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {
                // Prepare connection.
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                // Do work.
                raceDao.remove(connection,raceId);

                // Commit.
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void updateRace(Race r){
        DataSource dataSource = DataSourceLocator.getDataSource(RACE_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                // Prepare connection.
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                //Do work.
                raceDao.update(connection,r);

                // Commit.
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Inscription createInscription(Inscription inscription) {

        DataSource dataSource = DataSourceLocator.getDataSource(RACE_DATA_SOURCE);
        inscription.setDateInscription(LocalDateTime.now().withNano(0));

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* DO work. */
                Inscription createdInscription = inscriptionDao.create(connection, inscription);

                /* Commit */
                connection.commit();

                return createdInscription;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private Inscription findInscription(Long inscriptionId) throws InscriptionNotFoundException {

        DataSource dataSource = DataSourceLocator.getDataSource(RACE_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            return inscriptionDao.find(connection, inscriptionId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeInscription (Long partId){
        DataSource dataSource = DataSourceLocator.getDataSource(RACE_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                // Prepare connection.
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                // Do work.
                inscriptionDao.remove(connection, partId);

                // Commit.
                connection.commit();

            } catch (InscriptionNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateInscription(Inscription i){

        DataSource dataSource = DataSourceLocator.getDataSource(RACE_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                // Prepare connection.
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                // Do work.
                inscriptionDao.update(connection, i);

                // Commit.
                connection.commit();

            } catch (InscriptionNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddRaceAndFindRace() throws InputValidationException,InstanceNotFoundException{

        Race race = getValidRace("redon");
        Race addedRace = null;

        try{
            //Create Race
            LocalDateTime before = LocalDateTime.now().withNano(0);
            addedRace = raceService.addRace(race);
            LocalDateTime after = LocalDateTime.now().withNano(0);

            //Find race
            Race foundRace = raceService.findRace(addedRace.getRaceId());
            assertEquals(addedRace,foundRace);
            assertEquals(foundRace.getRaceId(),addedRace.getRaceId());
            assertEquals(foundRace.getCity(),race.getCity());
            assertEquals(foundRace.getDescription(),race.getDescription());
            assertEquals(foundRace.getDateRace(),race.getDateRace());
            assertEquals(foundRace.getPrice(),race.getPrice());
            assertEquals(foundRace.getMaxParticipants(),race.getMaxParticipants());
            assertTrue((foundRace.getCreationDate().compareTo(before)>=0) && (foundRace.getCreationDate().compareTo(after)<=0));

        }finally{
            //Clear Database
            if(addedRace != null){
                removeRace(addedRace.getRaceId());
            }
        }
    }

    @Test
    public void testAddInvalidRace(){
        //Check race city not null
        assertThrows(InputValidationException.class, () -> {
            Race race = getValidRace();
            race.setCity(null);
            Race addedRace = raceService.addRace(race);
            removeRace(addedRace.getRaceId());
        });

        //Check race city not empty
        assertThrows(InputValidationException.class, () -> {
            Race race = getValidRace();
            race.setCity("");
            Race addedRace = raceService.addRace(race);
            removeRace(addedRace.getRaceId());
        });


        // Check race description not null
        assertThrows(InputValidationException.class, () -> {
            Race race = getValidRace();
            race.setDescription(null);
            Race addedRace = raceService.addRace(race);
            removeRace(addedRace.getRaceId());
        });

        // Check race description not empty
        assertThrows(InputValidationException.class, () -> {
            Race race = getValidRace();
            race.setDescription("");
            Race addedRace = raceService.addRace(race);
            removeRace(addedRace.getRaceId());
        });

        //Check race date not null
        assertThrows(InputValidationException.class, () -> {
            Race race = getValidRace();
            race.setDateRace(null);
            Race addedRace = raceService.addRace(race);
            removeRace(addedRace.getRaceId());
        });

        //Check race date > actual date
        assertThrows(InputValidationException.class, () -> {
            Race race = getValidRace();
            race.setDateRace(LocalDateTime.of(2020,07,04,20,00,05));
            Race addedRace = raceService.addRace(race);
            removeRace(addedRace.getRaceId());
        });

        //Check price is not null
        assertThrows(InputValidationException.class, () ->{
            Race race = getValidRace();
            race.setPrice(null);
            Race addedRace = raceService.addRace(race);
            removeRace(addedRace.getRaceId());
        });

        //Check race price > 0
        assertThrows(InputValidationException.class, () ->{
            Race race = getValidRace();
            race.setPrice((float)-5);
            Race addedRace = raceService.addRace(race);
            removeRace(addedRace.getRaceId());
        });
    }


    @Test
    public void testFindNonExistentRace() {

        assertThrows(InstanceNotFoundException.class, () -> raceService.findRace(NON_EXISTENT_RACE_ID));

    }

    @Test
    public void testUpdateRace() throws InstanceNotFoundException,InputValidationException{

        Race race = raceService.addRace(getValidRace());

        try{
            Race raceToUpdate = new Race(race.getRaceId(), "vigo","Carrera Gastronomica", LocalDateTime.of(2021,07,04,20,00,05),(float) 30, 200, race.getNParticipants());

            updateRace(raceToUpdate);

            Race updatedRace = raceService.findRace(race.getRaceId());

            raceToUpdate.setCreationDate(race.getCreationDate());
            assertEquals("Carrera Gastronomica",updatedRace.getDescription());

        }finally{
            //Clear Database
            removeRace(race.getRaceId());
        }
    }


    @Test
    public void testRemoveRace() throws InstanceNotFoundException, InputValidationException{

        Race race = raceService.addRace(getValidRace("chapela"));

        removeRace(race.getRaceId());

        assertThrows(InstanceNotFoundException.class, () -> raceService.findRace(race.getRaceId()));
    }


    @Test
    public void testFindRaces()throws  InputValidationException{
        //Add races
        List<Race> races= new LinkedList<Race>();
        Race race1=raceService.addRace(getValidRace("redondela"));
        races.add(race1);
        Race race2=raceService.addRace(getValidRace("ourense"));
        races.add(race2);
        Race race3 = raceService.addRace(getAnotherValidRace("Santiago"));


        try {
            //Las carreras que añadimos a la lista son las que debe encontrar
            List<Race> foundRaces = raceService.findRaces(LocalDateTime.of(2022,12,31,23,30),null);
            assertEquals(races, foundRaces);

            //Añadimos una carrera en 2024, aumentamos la fecha de búsqueda y comprobamos que tambien la encuentra
            races.add(race3);
            List<Race> foundRaces2 = raceService.findRaces(LocalDateTime.of(2024,10,23,23,30),null);
            assertEquals(races, foundRaces2);

            //No encuentra carreras en por Ciudad (no hay ninguna en Pontevedra)
            foundRaces = raceService.findRaces(LocalDateTime.of(2024,12,27,14,00),"pontevedra");
            assertEquals(0, foundRaces.size());

            //Encuentra una carrera en Santiago
            foundRaces = raceService.findRaces(LocalDateTime.of(2024,12,27,14,00),"Santiago");
            assertEquals(1, foundRaces.size());

        }
        finally {
            // Clear Database
            for (Race race : races) {
                removeRace(race.getRaceId());
            }
        }
    }

    @Test
    public void testFindInvalidRaces() throws InputValidationException{

        //Test race with null date
        assertThrows(InputValidationException.class, () -> {
            List<Race> foundRaces = raceService.findRaces(null,null);
        });

        //Test race with past date
        assertThrows(InputValidationException.class, () -> {
            List<Race> foundRaces = raceService.findRaces(LocalDateTime.now().withNano(0).minusDays(2),null);
        });
    }

    @Test
    public void testAddInscriptionAndFindInscription () throws InputValidationException, InstanceNotFoundException, InscriptionNotFoundException,InscriptionDateException,
            RaceException, MaxParticipationException, AlreadyRegisteredException {

        // Add a Inscription
        Race r = raceService.addRace(getValidRace());
        Inscription i = getValidInscription(r.getRaceId());
        try {


            Long inscriptionId = raceService.addInscription( i.getEmail(),i.getCard(),r.getRaceId()
                    );

            i.setInscriptionId(inscriptionId);
            Inscription foundInscription = findInscription(inscriptionId);
            Race foundRace = raceService.findRace(r.getRaceId());

            assertEquals(foundRace.getNParticipants(), 1);
            assertEquals(VALID_CARD, foundInscription.getCard());
            assertEquals(inscriptionId, foundInscription.getInscriptionId());
            assertEquals(r.getRaceId(), foundInscription.getRaceId());
            assertFalse(foundInscription.getDorsalPicked());
            assertEquals(foundInscription.getDorsal(), foundRace.getNParticipants());
            assertEquals(foundInscription.getRaceId(), r.getRaceId());
            assertTrue(foundInscription.getDateInscription().compareTo(r.getDateRace()) <= 0);

        } finally {
            removeRace(r.getRaceId());
        }
    }


    @Test
    public void testAddInscriptionWithNoExistentRACEID(){

        Inscription inscription = getValidInscription((long) 500);

        assertThrows(RaceException.class, () -> raceService.addInscription(inscription.getEmail(),
                inscription.getCard(),

                inscription.getRaceId()));

        assertThrows(RaceException.class, () -> {
            inscription.setInscriptionId(null);
            raceService.addInscription(inscription.getEmail(), inscription.getCard(),
                    inscription.getRaceId());
        });

        assertThrows(InputValidationException.class, () -> raceService.addInscription(inscription.getEmail(),
                inscription.getCard(),
                (long) -1));


    }

    @Test
    public void testInscriptionWithInvalidDate() throws InputValidationException{
        Race r = raceService.addRace(getInvalidRace());
        Inscription i = getValidInscription(r.getRaceId());

        assertThrows(InscriptionDateException.class, () -> raceService.addInscription(i.getEmail(),
                i.getCard(),i.getRaceId()));

        removeRace(r.getRaceId());
    }


    @Test
    public void testInvalidInscription() throws InputValidationException{
        Race r = raceService.addRace(getValidRace());
        Inscription i = getValidInscription(r.getRaceId());

        assertThrows(InputValidationException.class, () -> {
           i.setCard(null);raceService.addInscription(i.getEmail(),i.getCard(),r.getRaceId());
        });
        assertThrows(InputValidationException.class, () -> {
            i.setEmail(null);raceService.addInscription(i.getEmail(),i.getCard(),r.getRaceId());
        });
        assertThrows(InputValidationException.class, () -> {
            i.setCard("094");
            i.setEmail(null);raceService.addInscription(i.getEmail(),i.getCard(),r.getRaceId());
        });

        removeRace(r.getRaceId());

    }

    @Test
    public void testAlreadyRegisteredRaceInscription() throws InputValidationException, InscriptionDateException, AlreadyRegisteredException, MaxParticipationException, RaceException {
        Race r = raceService.addRace(getValidRace());
        Inscription i = getValidInscription(r.getRaceId());

        raceService.addInscription(i.getEmail(),i.getCard(),r.getRaceId());

        assertThrows(AlreadyRegisteredException.class, () -> {
            raceService.addInscription(i.getEmail(),i.getCard(),r.getRaceId());
        });


        removeRace(r.getRaceId());
    }


    @Test
    public void testFindInscription() throws InputValidationException,RaceException,InscriptionDateException,
            MaxParticipationException,AlreadyRegisteredException{

        Race race1 = raceService.addRace(getValidRace("a coruña"));
        Race race2 = raceService.addRace(getValidRace("lugo"));
        Race race3 = raceService.addRace(getValidRace("santiago"));
        Race race4 = raceService.addRace(getValidRace("ourense"));

        raceService.addInscription(VALID_EMAIL,VALID_CARD,race1.getRaceId());
        raceService.addInscription(VALID_EMAIL,VALID_CARD,race2.getRaceId());
        raceService.addInscription(VALID_EMAIL,VALID_CARD,race3.getRaceId());

        try {

            List<Inscription> foundInscriptions = raceService.findInscriptions(VALID_EMAIL);
            assertEquals(3,foundInscriptions.size());

            int i;
            for (i=0; i < foundInscriptions.size();i++){
                if (foundInscriptions.get(i).getRaceId() == race2.getRaceId()){
                    assertEquals(foundInscriptions.get(i).getCard(),VALID_CARD);
                    assertEquals(foundInscriptions.get(i).getEmail(),VALID_EMAIL);
                    assertEquals(foundInscriptions.get(i).getDorsalPicked(),false);
                }
            }
        } finally {
            removeRace(race1.getRaceId());
            removeRace(race2.getRaceId());
            removeRace(race3.getRaceId());
            removeRace(race4.getRaceId());
        }
    }

    @Test
    public void testFindInscriptionWithNotExistingUser() throws InputValidationException {
        Race r = raceService.addRace(getValidRace());
        createInscription(getValidInscription(r.getRaceId()));
        try{
            assertTrue(raceService.findInscriptions("user@gmail.com").isEmpty());

        } finally {
            removeRace(r.getRaceId());
        }
    }


    @Test
    public void testPickDorsalWithInvalidCard() throws InputValidationException,InscriptionNotFoundException, RaceException, InscriptionDateException, MaxParticipationException, AlreadyRegisteredException{

        Race r = raceService.addRace(getValidRace("Madrid"));
        Inscription i = getValidInscription(r.getRaceId());

        try{
            Long inscriptionId = raceService.addInscription(i.getEmail(),i.getCard(),r.getRaceId());
            Inscription foundInscription = findInscription(inscriptionId);

            assertThrows(InvalidCardException.class, () ->
                    raceService.pickUpDorsal(foundInscription.getInscriptionId(),INVALID_CARD));


        }finally{
            removeRace(r.getRaceId());
        }
    }

    @Test
    public void testPickDorsalWithNoExistentPartId(){

        assertThrows(InscriptionNotFoundException.class ,() ->
            raceService.pickUpDorsal(NON_EXISTENT_PART_ID,VALID_CARD));
    }


    @Test
    public void testPickDorsalAlreadyPicked() throws InputValidationException,InscriptionNotFoundException, RaceException, InscriptionDateException, MaxParticipationException, AlreadyRegisteredException{

        Race r = raceService.addRace(getValidRace("Madrid"));
        Inscription i = getValidInscription(r.getRaceId());



        try{
            Long inscriptionId = raceService.addInscription(i.getEmail(),i.getCard(),r.getRaceId());
            Inscription foundInscription = findInscription(inscriptionId);
            foundInscription.setDorsalPicked(true);
            updateInscription(foundInscription);

            assertThrows(AlreadyPickedUpException.class, () -> raceService.pickUpDorsal(foundInscription.getInscriptionId(),foundInscription.getCard()));

        }finally{
            removeRace(r.getRaceId());
        }
    }

    @Test
    public void testPickDorsal() throws InscriptionNotFoundException,AlreadyPickedUpException, InvalidCardException,  InputValidationException, RaceException, InscriptionDateException, MaxParticipationException,  AlreadyRegisteredException{

        Race r = raceService.addRace(getValidRace("viso"));
        Inscription i = getValidInscription(r.getRaceId());

        try{

            Long inscriptionId = raceService.addInscription(i.getEmail(),i.getCard(),r.getRaceId());
            Inscription foundInscription = findInscription(inscriptionId);

            assertFalse(foundInscription.getDorsalPicked());

            raceService.pickUpDorsal(foundInscription.getInscriptionId(),foundInscription.getCard());
            Inscription inscriptionUpdate = findInscription(foundInscription.getInscriptionId());

            assertTrue(inscriptionUpdate.getDorsalPicked());

        }finally{
            removeRace(r.getRaceId());
        }
    }

}
