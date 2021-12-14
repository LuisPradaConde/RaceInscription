package es.udc.ws.races.restservice.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.races.restservice.dto.RestInscriptionDto;
import es.udc.ws.races.model.raceservice.RaceServiceFactory;
import es.udc.ws.races.model.raceservice.exceptions.*;
import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.restservice.json.JsonToExceptionConversor;
import es.udc.ws.races.restservice.json.JsonToRestInscriptionDtoConversor;
import es.udc.ws.races.restservice.dto.InscriptionToRestInscriptionDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;


public class InscriptionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String path = ServletUtils.normalizePath(req.getPathInfo());

        if (path != null && path.length() > 0) {
            String[] parameters = path.split("/");
            if (!parameters[2].equals("pickUp")){
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "invalid path " + path)),
                    null);
            return;
            }

            String inscriptionIdParameter = parameters[1];
            if (inscriptionIdParameter == null) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toInputValidationException(
                                new InputValidationException("Invalid Request: " + "parameter 'inscriptionId' is mandatory")),
                        null);
                return;
            }
            Long inscriptionId;
            try {
                inscriptionId = Long.valueOf(inscriptionIdParameter);
            } catch (NumberFormatException ex) {
                ServletUtils
                        .writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                                JsonToExceptionConversor.toInputValidationException(new InputValidationException(
                                        "Invalid Request: " + "parameter 'inscriptionId' is invalid '" + inscriptionIdParameter + "'")),
                                null);

                return;
            }
            String card = req.getParameter("card");
            if (card == null) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toInputValidationException(
                                new InputValidationException("Invalid Request: " + "parameter 'card' is mandatory")),
                        null);
                return;
            }

            try {
                RaceServiceFactory.getService().pickUpDorsal(inscriptionId, card);
            } catch (InscriptionNotFoundException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        JsonToExceptionConversor.toInscriptionNotFoundException(ex), null);
                return;
            } catch (AlreadyPickedUpException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toAlreadyPickedUpException(ex), null);
                return;
            } catch (InvalidCardException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor.
                        toInvalidCardException(e), null);
                return;
            }

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);

        } else {
            String raceIdParameter =  req.getParameter("raceId");

            if (raceIdParameter == null) {
                ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toInputValidationException(new InputValidationException("Invalid Request: " + "parameter 'raceId' is mandatory")),null);
                return;
            }
            Long raceId;
            try {
                raceId = Long.parseLong(raceIdParameter);
            } catch (NumberFormatException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toInputValidationException(new InputValidationException(
                                "Invalid Request: " + "parameter 'raceId' is invalid '" + raceIdParameter + "'")),null);
                return;
            }
            String emailParameter = req.getParameter("email");
            if (emailParameter == null) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toInputValidationException(new InputValidationException("Invalid Request: " + "parameter 'emailParameter' is mandatory")),null);
                return;
            }
            String card = req.getParameter("card");
            if (card == null) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toInputValidationException(new InputValidationException(
                                "Invalid Request: " + "parameter 'card' is mandatory")),null);
                return;
            }
            Long inscriptionId;
            try {
                inscriptionId = RaceServiceFactory.getService().addInscription(emailParameter,card,raceId);
            } catch (InputValidationException ex) {
                ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toInputValidationException(ex),null);
                return;
            } catch (InscriptionDateException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toInscriptionDateException(ex),null);
                return;
            } catch (RaceException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toRaceException(ex),null);
                return;
            } catch (MaxParticipationException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toMaxParticipation(ex),null);
                return;
            } catch (AlreadyRegisteredException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toAlreadyRegisteredException(ex),null);
                return;
            }
            RestInscriptionDto inscriptionDto = new RestInscriptionDto();
            inscriptionDto.setInscriptionId(inscriptionId);
            String inscriptionURL = ServletUtils.normalizePath(req.getRequestURL().toString() + "/" + inscriptionId.toString());
            Map<String,String> headers = new HashMap<>(1);
            headers.put("Location", inscriptionURL);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                    JsonToRestInscriptionDtoConversor.toObjectNode(inscriptionDto),headers);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0) {
            String email = req.getParameter("email");
            if (email.trim().isEmpty()){
                ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toInputValidationException(
                                new InputValidationException("Invalid request: " + "parameter 'email' " +
                                        "is necessary")),null);
            }
            try {
                List<Inscription> inscriptions = RaceServiceFactory.getService().findInscriptions(email);
                List<RestInscriptionDto> inscriptionDtos = InscriptionToRestInscriptionDtoConversor.toRaceInscriptionDtos(inscriptions);
                ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_OK,JsonToRestInscriptionDtoConversor.toArrayNode(inscriptionDtos),null);
            } catch (InputValidationException exception) {
                ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toInputValidationException(
                                new InputValidationException("Invalid request: email format not valid")),null);
            }
        }

    }


}
