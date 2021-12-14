package es.udc.ws.races.model.inscription;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.raceservice.exceptions.InscriptionNotFoundException;
import es.udc.ws.util.exceptions.*;


import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public abstract class AbstractSqlInscriptionDao implements SQLInscriptionDao {

    protected AbstractSqlInscriptionDao() {}



    @Override
    public Inscription find(Connection connection, Long inscriptionId) throws InscriptionNotFoundException{

        /* Create "queryString". */
        String queryString = "SELECT email , card, " +
                "raceId, dorsal, dateInscription, dorsalPicked FROM inscription" +
                " WHERE inscriptionId = ?";

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, inscriptionId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InscriptionNotFoundException("Inscription not found");
            }

            /* Get results. */
            i = 1;
            String email  = resultSet.getString(i++);
            String card = resultSet.getString(i++);
            Long raceId = resultSet.getLong(i++);
            int dorsal = resultSet.getInt(i++);
            Timestamp creationDateAsTimestamp =
                    resultSet.getTimestamp(i++);
            LocalDateTime dateInscription =
                    creationDateAsTimestamp.toLocalDateTime();
            boolean dorsalPicked = resultSet.getBoolean(i++);

            /* Return movie. */
            return new Inscription(inscriptionId, email, card , raceId, dorsal,
                    dateInscription, dorsalPicked);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Inscription> findByEmail(Connection connection, String email)  {

    /* Create "queryString". */
    String[] keywords = email != null ? email.split(" ") : null;
    String queryString = "SELECT inscriptionId ,email, card,"+
            "raceId, dorsal, dateInscription, dorsalPicked FROM inscription";

    if (keywords != null && keywords.length > 0) {
        queryString += " WHERE";
        for (int i = 0; i < keywords.length; i++){
            if (i > 0){
                queryString += " AND";
            }
            queryString += " LOWER(email) LIKE LOWER(?)";
        }
    }
    try (PreparedStatement preparedStatement =
                 connection.prepareStatement(queryString)) {

        if (keywords != null){
            /* Fill "preparedStatement".*/
            for (int i = 0; i < keywords.length; i++){
                preparedStatement.setString(i + 1, "%" + keywords[i] + "%");
            }
        }

        /* Execute query. */
        ResultSet resultSet = preparedStatement.executeQuery();

        /* Read race. */
        List<Inscription> inscriptions = new ArrayList<>();

        while (resultSet.next()) {

            int i = 1;
            Long inscriptionId = resultSet.getLong(i++);
            String email1 = resultSet.getString(i++);
            String card = resultSet.getString(i++);
            Long raceId = resultSet.getLong(i++);
            Integer dorsal = resultSet.getInt(i++);
            Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime dateInscription = creationDateAsTimestamp.toLocalDateTime();
            Boolean dorsalPicked = resultSet.getBoolean(i++);


            inscriptions.add(new Inscription( inscriptionId, email1, card, raceId, dorsal, dateInscription, dorsalPicked));
        }

        return inscriptions;

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

    @Override
    public void update(Connection connection, Inscription inscription) throws InscriptionNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE inscription"
                + " SET email = ?, card = ?, raceId = ?, dorsalPicked = ? "
                + "WHERE inscriptionId = ?";

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, inscription.getEmail());
            preparedStatement.setString(i++, inscription.getCard());
            preparedStatement.setLong(i++, inscription.getRaceId());
            preparedStatement.setBoolean(i++, inscription.getDorsalPicked());
            preparedStatement.setLong(i++,inscription.getInscriptionId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InscriptionNotFoundException("Inscription not found");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long inscriptionId) throws InscriptionNotFoundException {

            /* Create "queryString". */
            String queryString = "DELETE FROM inscription WHERE" +
                    " inscriptionId = ?";

            try (PreparedStatement preparedStatement =
                         connection.prepareStatement(queryString)) {

                /* Fill "preparedStatement". */
                int i = 1;
                preparedStatement.setLong(i++, inscriptionId);

                /* Execute query. */
                int removedRows = preparedStatement.executeUpdate();

                if (removedRows == 0) {
                    throw new InscriptionNotFoundException("Inscription not found");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public boolean exist(Connection connection, String email, Long raceId){
        String queryString = "SELECT email, card, raceId, dorsal, dateInscription, dorsalPicked" +
                " FROM Inscription WHERE email = ? AND raceId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, email);
            preparedStatement.setLong(i++, raceId);

            /*Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return false;
            }

            /* Get results. */
            return true;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
