package es.udc.ws.races.model.race;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Jdbc3CcSqlRaceDao extends AbstractSqlRaceDao{

    @Override
    public Race create(Connection connection, Race race) {

        // Create "queryString".
        String queryString = "INSERT INTO race"
                + " (city, description, dateRace, price, maxParticipants, nParticipants, creationDate)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, race.getCity().toLowerCase());
            preparedStatement.setString(i++, race.getDescription());
            preparedStatement.setTimestamp(i++,Timestamp.valueOf(race.getDateRace()));
            preparedStatement.setFloat(i++, race.getPrice());
            preparedStatement.setInt(i++, race.getMaxParticipants());
            preparedStatement.setInt(i++, race.getNParticipants());
            preparedStatement.setTimestamp(i++,Timestamp.valueOf(race.getCreationDate()));

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long raceId = resultSet.getLong(1);

            /* Return race. */
            return new Race(raceId, race.getCity(), race.getDescription(),
                    race.getDateRace(), race.getPrice(), race.getMaxParticipants(),race.getCreationDate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
