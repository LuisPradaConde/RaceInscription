package es.udc.ws.races.client.service.thrift;

import es.udc.ws.races.client.service.ClientRaceService;
import es.udc.ws.races.client.service.dto.ClientInscriptionDto;
import es.udc.ws.races.client.service.dto.ClientRaceDto;
import es.udc.ws.races.thrift.ThriftInputValidationException;
import es.udc.ws.races.thrift.ThriftRaceService;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.List;

public class ThriftClientRaceService implements ClientRaceService {


    private final static String ENDPOINT_ADDRESS_PARAMETER = "ThriftClientRaceService.endpointAddress";
    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);




    @Override
    public Long addRace (ClientRaceDto r) throws InputValidationException {
        ThriftRaceService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();
        try {
            transport.open();
            return client.addRace(ClientRaceDtoToThriftRaceDtoConversor.toThriftRaceDto(r));
        }catch (ThriftInputValidationException e){
            throw new InputValidationException(e.getMessage());
        }catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }


    @Override
    public List<ClientRaceDto> findRaces(String date, String city) {
        ThriftRaceService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {
            transport.open();

            return ClientRaceDtoToThriftRaceDtoConversor.toClientRaceDto(client.findRaces(date, city));

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    @Override
    public ClientRaceDto findRace(Long raceId) {
        ThriftRaceService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {
            transport.open();
            return ClientRaceDtoToThriftRaceDtoConversor.toClientRaceDto(client.findRace(raceId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    @Override
    public Long addInscription(String email, String card,Long raceId) {
        ThriftRaceService.Client c = getClient();
        TTransport transport = c.getInputProtocol().getTransport();
        try {

            transport.open();
            return c.addInscription(email, card, raceId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    public List<ClientInscriptionDto> findInscriptions (String email) {
        ThriftRaceService.Client c = getClient();
        TTransport transport = c.getInputProtocol().getTransport();
        try {

            transport.open();
            return ClientInscriptionToThriftInscriptionDtoConversor.toClientInscriptionDto(c.findInscriptions(email));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    public void pickUpDorsal(Long inscriptionId, String card) {
        ThriftRaceService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {
            transport.open();
            client.pickUpDorsal(inscriptionId, card);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    private ThriftRaceService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftRaceService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }

}

