-- ----------------------------------------------------------------------------
-- Model
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- Drop tables. NOTE: before dropping a table (when re-executing the script),
-- the tables having columns acting as foreign keys of the table to be dropped,
-- must be dropped first (otherwise, the corresponding checks on those tables
-- could not be done).

DROP TABLE IF EXISTS inscription;
DROP TABLE IF EXISTS race;

-- --------------------------------- Race ------------------------------------
CREATE TABLE race ( 
    raceId BIGINT NOT NULL AUTO_INCREMENT,
    city VARCHAR(255) NOT NULL,
    description VARCHAR(1024) NOT NULL,
    dateRace DATETIME NOT NULL,
    price FLOAT NOT NULL,
    maxParticipants INT NOT NULL,
    nParticipants INT NOT NULL,
    creationDate DATETIME NOT NULL,
    CONSTRAINT RacePK PRIMARY KEY(raceId))
ENGINE = InnoDB;

-- --------------------------------- Inscription -----------------------------------

CREATE TABLE inscription (
    inscriptionId BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(40) NOT NULL,
    card VARCHAR(16) NOT NULL,
    raceId BIGINT NOT NULL,
    dorsal INT NOT NULL,
    dateInscription DATETIME NOT NULL,
    dorsalPicked BOOLEAN NOT NULL,
    CONSTRAINT InscriptionPK PRIMARY KEY(inscriptionId),
    CONSTRAINT InscriptionRaceIdFK FOREIGN KEY (raceId)
        REFERENCES Race (raceId) ON DELETE CASCADE )
ENGINE = InnoDB;
