package es.udc.ws.races.model.inscription;


import java.sql.*;
import java.time.LocalDateTime;

public class Jdbc3CcSqlInscriptionDao extends AbstractSqlInscriptionDao{

    @Override
    public Inscription create(Connection connection, Inscription inscription) {

        /* Create "queryString". */
        String queryString = "INSERT INTO inscription"
                + " (email, card, raceId,dorsal,dateInscription,dorsalPicked)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, inscription.getEmail());
            preparedStatement.setString(i++, inscription.getCard());
            preparedStatement.setLong(i++, inscription.getRaceId());
            preparedStatement.setInt(i++, inscription.getDorsal());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(inscription.getDateInscription()));
            preparedStatement.setBoolean(i++,inscription.getDorsalPicked());

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }

            Long inscriptionId = resultSet.getLong(1);

            /* Return inscription. */
            return new Inscription(inscriptionId, inscription.getEmail(), inscription.getCard(),
                    inscription.getRaceId(), inscription.getDorsal(), inscription.getDateInscription(), inscription.getDorsalPicked());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
