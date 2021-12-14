package es.udc.ws.races.model.race;

import java.time.*;
import java.util.Objects;

public class Race {
    private Long raceId;
    private String city;
    private String description;
    private LocalDateTime dateRace;
    private Float price;
    private Integer maxParticipants;
    private Integer nParticipants = 0;
    private LocalDateTime creationDate;

    //Constructor

    public Race(String city, String description, LocalDateTime dateRace, Float price, Integer maxParticipants) {
        this.city = city;
        this.description = description;
        this.dateRace = dateRace;
        this.price = price;
        this.maxParticipants = maxParticipants;
    }

    public Race(Long raceId, String city, String description, LocalDateTime dateRace, Float price, Integer maxParticipants,LocalDateTime creationDate) {
        this.raceId = raceId;
        this.city = city;
        this.description = description;
        this.dateRace = dateRace;
        this.price = price;
        this.maxParticipants = maxParticipants;
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
    }

    public Race(Long raceId, String city, String description, LocalDateTime dateRace, Float price, Integer maxParticipants, Integer nParticipants) {
        this.raceId = raceId;
        this.city = city;
        this.description = description;
        this.dateRace = dateRace;
        this.price = price;
        this.maxParticipants = maxParticipants;
        this.nParticipants = nParticipants;
    }

    public Race(Long raceId, String city, String description, LocalDateTime dateRace, Float price, Integer maxParticipants, Integer nParticipants, LocalDateTime creationDate) {
        this.raceId = raceId;
        this.city = city;
        this.description = description;
        this.dateRace = dateRace;
        this.price = price;
        this.maxParticipants = maxParticipants;
        this.nParticipants = nParticipants;
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Integer getNParticipants() { return nParticipants; }

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

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
    }
    public void setNumParticipants(Integer nParticipants) { this.nParticipants = nParticipants; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Race race = (Race) o;
        return raceId.equals(race.raceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raceId);
    }
}

