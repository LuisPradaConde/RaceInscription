package es.udc.ws.races.client.service.dto;

import java.time.LocalDateTime;

public class ClientRaceDto {

    private Long raceId;
    private String city;
    private String description;
    private LocalDateTime dateRace;
    private Float price;
    private Integer maxParticipants;
    private Integer nParticipants = 0;


    //Constructor
    public ClientRaceDto(Long raceId, String city, String description, LocalDateTime dateRace, Float price, Integer maxParticipants, Integer nParticipants) {
        this.raceId = raceId;
        this.city = city;
        this.description = description;
        this.dateRace = dateRace;
        this.price = price;
        this.maxParticipants = maxParticipants;
        this.nParticipants = nParticipants;
    }

    public ClientRaceDto(Long raceId, String city, String description, LocalDateTime dateRace, Float price, Integer maxParticipants) {
        this.raceId = raceId;
        this.city = city;
        this.description = description;
        this.dateRace = dateRace;
        this.price = price;
        this.maxParticipants = maxParticipants;
    }


    //Getters

    public Long getRaceId() {
        return raceId;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateRace() {
        return dateRace;
    }

    public Float getPrice() {
        return price;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public Integer getNParticipants() {
        return nParticipants;
    }


    //Setters

    public void setRaceId(Long raceId) {
        this.raceId = raceId;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateRace(LocalDateTime dateRace) {
        this.dateRace = dateRace;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setNParticipants(Integer nParticipants) {
        this.nParticipants = nParticipants;
    }

    @Override
    public String toString() {
        return "ClientRaceDto{" +
                "raceId=" + raceId +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", dateRace=" + dateRace +
                ", price=" + price +
                ", maxParticipants=" + maxParticipants +
                ", nParticipants=" + nParticipants +
                '}';
    }
}
