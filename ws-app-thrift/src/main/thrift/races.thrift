namespace java es.udc.ws.races.thrift

struct ThriftRaceDto {
    1: i64 raceId;
    2: string city;
    3: string description;
    4: string dateRace;
    5: double price;
    6: i32 maxParticipants;
    7: i32 nParticipants = 0;
}

struct ThriftInscriptionDto {
    1: i64 inscriptionId;
    2: string email;
    3: string card;
    4: i64 raceId;
    5: i32 dorsal;
    6: string dateInscription;
    7: bool dorsalPicked;
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftRaceException {
    1: string message
}

exception ThriftInscriptionDateException {
    1: string message
}

exception ThriftMaxParticipantionException {
    1: string message
}

exception ThriftAlreadyRegistered {
    1: string message
}

exception ThriftAlreadyPickedException {
    1: string message
}

exception ThriftInvalidCardException {
    1: string message
}

exception ThriftInscriptionNotFoundException {
    1: string message
}

service ThriftRaceService {
    i64 addRace(1: ThriftRaceDto raceDto) throws (1: ThriftInputValidationException e);

    ThriftRaceDto findRace(1: i64 raceId) throws (1: ThriftInstanceNotFoundException e);

    list<ThriftRaceDto> findRaces (1: string date, 2: string city) throws (1: ThriftInputValidationException e);

    i64 addInscription(1: string email,2: string creditCard,3: i64 raceId)
                    throws (1: ThriftInputValidationException e1, 2: ThriftInscriptionDateException e2,
                    3: ThriftRaceException e3, 4: ThriftMaxParticipantionException e4,
                    5: ThriftAlreadyRegistered e5);

    list<ThriftInscriptionDto> findInscriptions (1: string email) throws (1: ThriftInputValidationException e);

    void pickUpDorsal(1: i64 inscriptionId, 2: string card)
            throws (1: ThriftInscriptionNotFoundException e1, 2: ThriftAlreadyPickedException e2,
            3: ThriftInvalidCardException e3);
}