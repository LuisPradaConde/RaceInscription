package es.udc.ws.races.client.service.dto;

import java.time.LocalDateTime;

public class ClientInscriptionDto {
        private Long inscriptionId;
        private String email;
        private String card;
        private Long raceId;
        private Integer dorsal = 0;
        private LocalDateTime dateInscription;
        private Boolean dorsalPicked = false;



        public ClientInscriptionDto(Long inscriptionId, String email, String card, Long raceId, Integer dorsal, LocalDateTime dateInscription, Boolean dorsalPicked) {
            this.inscriptionId = inscriptionId;
            this.email = email;
            this.card = card;
            this.raceId = raceId;
            this.dorsal = dorsal;
            this.dateInscription = dateInscription;
            this.dorsalPicked = dorsalPicked;
        }

        public ClientInscriptionDto(Long inscriptionId) {
                this.inscriptionId = inscriptionId;
        }


    //Getters

        public Long getInscriptionId() {
            return inscriptionId;
        }

        public String getEmail() {
            return email;
        }

        public String getCard() {
            return card;
        }

        public Long getRaceId() {
            return raceId;
        }

        public Integer getDorsal() {
            return dorsal;
        }

        public LocalDateTime getDateInscription() {
            return dateInscription;
        }

        public Boolean getDorsalPicked() {
            return dorsalPicked;
        }

        //Setters


        public void setInscriptionId(Long inscriptionId) {
            this.inscriptionId = inscriptionId;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public void setRaceId(Long raceId) {
            this.raceId = raceId;
        }

        public void setDorsal(Integer dorsal) {
            this.dorsal = dorsal;
        }

        public void setDateInscription(LocalDateTime dateInscription) {
            this.dateInscription = dateInscription;
        }

        public void setDorsalPicked(Boolean dorsalPicked) {
            this.dorsalPicked = dorsalPicked;
        }


}


