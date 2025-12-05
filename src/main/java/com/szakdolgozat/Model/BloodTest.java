package com.szakdolgozat.Model;

import jakarta.persistence.*;

import java.time.LocalDate;


    @Entity
    @Table(name = "blood_test")
    public class BloodTest {
        @Id
        private Long id;
        @ManyToOne
        @JoinColumn(name = "athlete_id", nullable = false)
        private Athlete athlete;
        private LocalDate date;
        private String LaboratoryName;

        public BloodTest(Long id, Athlete athlete, LocalDate date, String laboratoryName) {
            this.id = id;
            this.athlete = athlete;
            this.date = date;
            LaboratoryName = laboratoryName;
        }

        public BloodTest() {}

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Athlete getAthlete() {
            return athlete;
        }

        public void setAthlete(Athlete athlete) {
            this.athlete = athlete;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public String getLaboratoryName() {
            return LaboratoryName;
        }

        public void setLaboratoryName(String laboratoryName) {
            LaboratoryName = laboratoryName;
        }
    }

