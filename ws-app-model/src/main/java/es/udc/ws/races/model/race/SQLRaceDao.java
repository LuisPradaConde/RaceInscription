package es.udc.ws.races.model.race;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import es.udc.ws.util.exceptions.*;

public interface SQLRaceDao {

    Race create(Connection connection, Race race);

    Race find(Connection connection, Long raceId)
            throws InstanceNotFoundException;

    List<Race> findRaces(Connection connection, LocalDateTime date, String city) throws InputValidationException;

    void update(Connection connection, Race race)
            throws InstanceNotFoundException;

    public void remove (Connection connection, Long raceId)
            throws InstanceNotFoundException;
}