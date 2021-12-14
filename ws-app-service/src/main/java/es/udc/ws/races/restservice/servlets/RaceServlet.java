package es.udc.ws.races.restservice.servlets;

import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.raceservice.RaceServiceFactory;
import es.udc.ws.races.restservice.dto.RaceToRestRaceDtoConversor;
import es.udc.ws.races.restservice.dto.RestRaceDto;
import es.udc.ws.races.restservice.json.JsonToRestRaceDtoConversor;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.races.restservice.json.JsonToExceptionConversor;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class RaceServlet extends HttpServlet {

@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if ((path == null) || path.length() == 0 ){
            String dateString = req.getParameter("date");
            String city = req.getParameter("city");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime date = LocalDate.parse(dateString, formatter).atStartOfDay();

            if (city.trim().isEmpty()){
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor.toInputValidationException(new InputValidationException( "Invalid RequestParameters (city): Cannot be null or empty")),null);
            } else try{
                List<Race> races = RaceServiceFactory.getService().findRaces(date,city);
                List<RestRaceDto> raceDtos = RaceToRestRaceDtoConversor.toRestRaceDtos(races);
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestRaceDtoConversor.toArrayNode(raceDtos), null);
            }catch (InputValidationException ex){
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor.toInputValidationException( new InputValidationException("Invalid Request Parameters (Date): Cannot be null or past")),null);
                }
        }else {
            String raceIdAsString = path.substring(1);
            Long raceId;
            try {
                raceId = Long.valueOf(raceIdAsString);
            } catch (NumberFormatException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        JsonToExceptionConversor.toInputValidationException(
                                new InputValidationException("Invalid Request: " + "invalid race id '" + path)),
                        null);
                return;
            }
            Race race;
            try {
                race = RaceServiceFactory.getService().findRace(raceId);
            } catch (InstanceNotFoundException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        JsonToExceptionConversor.toInstanceNotFoundException(ex), null);
                return;
            }

            RestRaceDto raceDto = RaceToRestRaceDtoConversor.toRestRaceDto(race);

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestRaceDtoConversor.toObjectNode(raceDto), null);

        }

    }

@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path != null && path.length() > 0) {
            ServletUtils.writeServiceResponse(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " +
                                    "invalid path " + path)),
                    null);
            return;
        }
        RestRaceDto rDto;
        try{
            rDto = JsonToRestRaceDtoConversor.toServiceRaceDto(req.getInputStream());
        } catch(ParsingException ex){
            ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_BAD_REQUEST,JsonToExceptionConversor.toInputValidationException(new InputValidationException(ex.getMessage())),null);
        return;
        }

        Race r = RaceToRestRaceDtoConversor.toRace(rDto);
        try{
            r = RaceServiceFactory.getService().addRace(r);
        } catch (InputValidationException ex){
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor.toInputValidationException(ex), null);
            return;
        }

        rDto = RaceToRestRaceDtoConversor.toRestRaceDto(r);

        String rURL = ServletUtils.normalizePath(req.getRequestURL().toString())  + "/" + r.getRaceId();
        Map<String,String> headers = new HashMap<>(1);
        headers.put("Location",rURL);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED, JsonToRestRaceDtoConversor.toObjectNode(rDto),headers);
    }
}
