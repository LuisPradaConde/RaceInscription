package es.udc.ws.races.model.race;

import es.udc.ws.util.exceptions.*;
import java.sql.*;
import java.time.*;
import java.util.*;


public abstract class AbstractSqlRaceDao implements SQLRaceDao {

    protected AbstractSqlRaceDao() {}

    @Override
    public Race find(Connection connection, Long raceId) throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT city , description, dateRace, price, maxParticipants, nParticipants," +
                "creationDate FROM race WHERE raceId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i=1;
            preparedStatement.setLong(i++, raceId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(raceId, Race.class.getName());
            }

            /* Get results. */
            i = 1;
            String city  = resultSet.getString(i++);
            String description = resultSet.getString(i++);
            Timestamp DateAsTimestamp =
                    resultSet.getTimestamp(i++);
            LocalDateTime dateRace =
                    DateAsTimestamp.toLocalDateTime();
            float price = resultSet.getFloat(i++);
            Integer maxParticipants = resultSet.getInt(i++);
            Integer nParticipants = resultSet.getInt(i++);
            Timestamp creationDateAsTimestamp =
                    resultSet.getTimestamp(i++);
            LocalDateTime creationDate =
                    creationDateAsTimestamp.toLocalDateTime();

            /* Return Race. */
            return new Race(raceId, city , description, dateRace,
                    price, maxParticipants, nParticipants, creationDate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Race> findRaces(Connection connection, LocalDateTime dateRace, String city) throws InputValidationException {

        /* Create "queryString". */
        String queryString = "SELECT raceId, city , description, dateRace, price, maxParticipants, nParticipants ,creationDate FROM race";

        if ((dateRace != null) && (dateRace.isAfter(LocalDateTime.now()))) {
            queryString += " WHERE dateRace < ?";
            if (city != null) {
                queryString += " AND city=LOWER(?)";
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

                //Añadimos el valor de la fecha
                if (dateRace != null){preparedStatement.setTimestamp(1,Timestamp.valueOf(dateRace));}

                //Añadimos el valor de la ciudad
                if (city != null){preparedStatement.setString(2,city);}

                /* Execute query. */
                ResultSet resultSet = preparedStatement.executeQuery();

                /* Read races. */
                List<Race> races = new ArrayList<Race>();

                while (resultSet.next()) {
                    int i = 1;
                    Long raceId = Long.valueOf(resultSet.getLong(i++));
                    String resultCity = resultSet.getString(i++);
                    String resultDesc = resultSet.getString(i++);
                    Timestamp raceDateAsTimestamp = resultSet.getTimestamp(i++);
                    LocalDateTime resultDate = raceDateAsTimestamp.toLocalDateTime();
                    float resultPrice = resultSet.getFloat(i++);
                    Integer resultMaxParticipants = resultSet.getInt(i++);
                    Integer resultNParticipants = resultSet.getInt(i++);
                    Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
                    LocalDateTime resultCreationDate = creationDateAsTimestamp.toLocalDateTime();
                    if (LocalDateTime.now().isBefore(resultDate)){
                        races.add(new Race(raceId, resultCity, resultDesc, resultDate, resultPrice, resultMaxParticipants, resultNParticipants,resultCreationDate));
                    }
                }
                /* Return race. */
                return races;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new InputValidationException("Date cannot be null or past");
        }
    }


    @Override
    public void update(Connection connection, Race race) throws InstanceNotFoundException{

            /* Create "queryString". */
            String queryString = "UPDATE race"
                    + " SET city  = ?, description = ?, dateRace = ?, "
                    + "price = ?, maxParticipants = ?, nParticipants = ?"
                    + " WHERE raceId = ?";

            try (PreparedStatement preparedStatement =
                         connection.prepareStatement(queryString)) {

                /* Fill "preparedStatement". */
                int i = 1;
                preparedStatement.setString(i++, race.getCity().toLowerCase());
                preparedStatement.setString(i++, race.getDescription());
                Timestamp dateRace = Timestamp.valueOf(race.getDateRace());
                preparedStatement.setTimestamp(i++, dateRace);
                preparedStatement.setDouble(i++, race.getPrice());
                preparedStatement.setInt(i++, race.getMaxParticipants());
                preparedStatement.setInt(i++, race.getNParticipants());
                preparedStatement.setLong(i++,race.getRaceId());

                /* Execute query. */
                int updatedRows = preparedStatement.executeUpdate();

                if (updatedRows == 0) {
                    throw new InstanceNotFoundException(race.getRaceId(),Race.class.getName());
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



    @Override
    public void remove (Connection connection, Long raceId) throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM race WHERE" +
                " raceId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, raceId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(raceId,Race.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}





