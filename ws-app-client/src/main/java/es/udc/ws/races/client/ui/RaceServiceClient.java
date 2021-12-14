package es.udc.ws.races.client.ui;


import es.udc.ws.races.client.service.ClientRaceService;
import es.udc.ws.races.client.service.ClientRaceServiceFactory;
import es.udc.ws.races.client.service.dto.ClientInscriptionDto;
import es.udc.ws.races.client.service.dto.ClientRaceDto;
import es.udc.ws.races.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.net.StandardSocketOptions;
import java.time.LocalDateTime;
import java.util.List;

public class RaceServiceClient {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsageAndExit();
        }
        ClientRaceService clientraceservice = ClientRaceServiceFactory.getService();

        if ("-addRace".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[]{4, 5});

            //[addRace] RaceServiceClient -addRace <city> <description> <date> <price> <maxParticipants>

            try {
                Long raceId;
                raceId = clientraceservice.addRace(new ClientRaceDto(null, args[1], args[2], LocalDateTime.parse(args[3]),
                        Float.valueOf(args[4]), Integer.valueOf(args[5])));

                System.out.println("Race with ID: " + raceId + " created successfully");

            } catch (NumberFormatException | InputValidationException e) {
                e.printStackTrace(System.err);
            }
        } else if ("-findRaces".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[]{});

            //[findRaces] RaceServiceClient -findRaces <date> <city>

            try {
                List<ClientRaceDto> races = clientraceservice.findRaces(args[1], args[2]);
                System.out.println(races.size() + " races were found :");
                System.out.println("--------------------");
                for (ClientRaceDto dto : races) {
                    System.out.println(
                                    " RaceId: " + dto.getRaceId() +
                                    ", City: " + dto.getCity() +
                                    ", Description: " + dto.getDescription() +
                                    ", Date: " + dto.getDateRace() +
                                    ", Price: " + dto.getPrice() +
                                    ", MaxParticipants: " + (dto.getMaxParticipants()) +
                                    ", Free Places: " + (dto.getMaxParticipants() - dto.getNParticipants()));
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        } else if ("-findRace".equalsIgnoreCase(args[0])) {

            validateArgs(args, 2, new int[]{1});

            try {
                ClientRaceDto race = clientraceservice.findRace(Long.valueOf(args[1]));

                System.out.println("The race found has raceId '" + args[1] + "'");

                System.out.println("\nCity: " + race.getCity() +
                        ", \nDescription: " + race.getDescription() +
                        ", \nDate: " + race.getDateRace() +
                        ", \nPrice: " + race.getPrice() +
                        ", \nPlaces available: " + (race.getMaxParticipants() - race.getNParticipants()));

            } catch (InstanceNotFoundException ex) {
                System.out.println("Race not found");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-register".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[]{3});
            try {
                Long code = clientraceservice.addInscription(args[1], args[2], Long.parseLong(args[3]));


                System.out.println("Inscrited in the race " + args[3] + ".");
                System.out.println("Your inscription code is " + code + ".");
            } catch ( InputValidationException | InstanceNotFoundException | MaxParticipationException |
                    RaceException | AlreadyRegisteredException | InscriptionDateException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if (("-findRegisters").equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[]{});

            try {
                List<ClientInscriptionDto> inscriptions = clientraceservice.findInscriptions(args[1]);
                System.out.println("Found " + inscriptions.size() + " inscription(s) for " + args[1]);
                for (ClientInscriptionDto i : inscriptions) {
                    System.out.println("InscriptionId: " + i.getInscriptionId() +
                            ", Credit Card: " + i.getCard() +
                            ", RaceId: " + i.getRaceId() +
                            ", Dorsal: " + i.getDorsal() +
                            ", Inscription Date: " + i.getDateInscription() +
                            ", Number Picked: " + i.getDorsalPicked());

                }
            } catch (InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);

            }
        } else if("-deliverNumber".equalsIgnoreCase(args[0])) {

            validateArgs(args, 3, new int[]{1});

            try {
                clientraceservice.pickUpDorsal(Long.valueOf(args[1]), args[2]);
                System.out.println("Dorsal for inscription nº: " + args[1] + " has picked");
            } catch (InstanceNotFoundException e){
                e.printStackTrace(System.err);
                System.out.println("Inscription [" + args[1]+ "] not found");
            } catch(InvalidCard | AlreadyPickedUp ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [addRace]    RaceServiceClient -addRace <city> <description> <date> <price> <maxParticipants>\n" +
                "    [findRaces]  RaceServiceClient -findRaces <date> <ciy>\n" +
                "    [findRace]   RaceServiceClient -findRace <raceId>\n" +
                "    [deliverNumber] RaceServiceClient -deliverNumber <id|code> <card>\n" +
                "    [register] RaceServiceClient -register <email> <card> <raceId>\n" +
                "    [findRegisters] RaceServiceClient -findRegisters <email> \n ");
    }


    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }
}
