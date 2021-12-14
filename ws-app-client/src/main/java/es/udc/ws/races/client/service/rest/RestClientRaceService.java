package es.udc.ws.races.client.service.rest;


import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.races.client.service.ClientRaceService;
import es.udc.ws.races.client.service.dto.ClientInscriptionDto;
import es.udc.ws.races.client.service.dto.ClientRaceDto;
import es.udc.ws.races.client.service.exceptions.*;
import es.udc.ws.races.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.races.client.service.rest.json.JsonToClientInscriptionDtoConversor;
import es.udc.ws.races.client.service.rest.json.JsonToClientRaceDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;


public class RestClientRaceService implements  ClientRaceService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientRaceService.endpointAddress";
    private String endpointAddress;


    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

@Override
    public Long addRace( ClientRaceDto r) throws InputValidationException {
        try{
            HttpResponse response = Request.Post(getEndpointAddress() + "races").
                    bodyStream(toInputStream(r), ContentType.create("application/json")).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_CREATED, response);
            return JsonToClientRaceDtoConversor.toClientRaceDto(response.getEntity().getContent()).getRaceId();

        } catch (InputValidationException e){
            throw e;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private InputStream toInputStream(ClientRaceDto race) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientRaceDtoConversor.toObjectNode(race));

            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

@Override
    public List<ClientRaceDto> findRaces(String date, String city){
        try{
            HttpResponse response = Request.Get(getEndpointAddress() +
                    "races?date=" +
                    URLEncoder.encode(date,"UTF-8") +
                    "&city=" +
                    URLEncoder.encode(city,"UTF-8"))
                    .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);
            return JsonToClientRaceDtoConversor.toClientRaceDtos(response.getEntity().getContent());

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void validateStatusCode(int successCode, HttpResponse response) throws Exception {
        try {

            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND:
                    throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                            response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ClientRaceDto findRace(Long raceId) throws InstanceNotFoundException{

        try{
            HttpResponse response = Request.Get(getEndpointAddress() + "races/" + URLEncoder.encode(String.valueOf(raceId), "UTF-8")).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientRaceDtoConversor.toClientRaceDto(response.getEntity().getContent());

        } catch (InstanceNotFoundException ex){
            throw ex;
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long addInscription(String email, String card, Long raceId) throws InputValidationException,InstanceNotFoundException,RaceException, InscriptionDateException,
            MaxParticipationException, AlreadyRegisteredException{
        try {
            HttpResponse resp = Request.Post(getEndpointAddress() + "inscriptions").
                    bodyForm(Form.form().add("email", email).add("card", card).add("raceId", Long.toString(raceId)).build()).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, resp);

            return JsonToClientInscriptionDtoConversor.toClientInscriptionDto(resp.getEntity().getContent()).getInscriptionId();

        }catch (InputValidationException | InstanceNotFoundException | RaceException | InscriptionDateException |
                 MaxParticipationException | AlreadyRegisteredException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientInscriptionDto> findInscriptions (String email) throws InputValidationException {
        try {
            HttpResponse resp = Request.Get(getEndpointAddress() + "inscriptions?email=" +
                    URLEncoder.encode(email, "UTF-8")).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, resp);

            return JsonToClientInscriptionDtoConversor.toClientInscriptionDtos(resp.getEntity().getContent());


        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

@Override
    public void pickUpDorsal(Long inscriptionId, String card) throws InstanceNotFoundException, AlreadyPickedUp, InvalidCard{

        try{
            HttpResponse response = Request.Post(getEndpointAddress() + "inscriptions/"
                    + inscriptionId + "/pickUp?card=" + URLEncoder.encode(card,"UTF-8")).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InstanceNotFoundException | AlreadyPickedUp | InvalidCard e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
