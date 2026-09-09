package com.szakdolgozat.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
    @Table(name = "blood_test")
    public class BloodTest {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "athlete_id", nullable = false)
        private Athlete athlete;
        private LocalDate date;
        private String laboratoryName;

        public BloodTest(Long id, Athlete athlete, LocalDate date, String laboratoryName) {
            this.id = id;
            this.athlete = athlete;
            this.date = date;
            this.laboratoryName = laboratoryName;
        }
        @JsonIgnore
        @OneToMany(mappedBy = "bloodTest", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<MarkerValue> markerValues = new ArrayList<>();

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
        return laboratoryName;
    }

    public void setLaboratoryName(String laboratoryName) {
        this.laboratoryName = laboratoryName;
    }

    public List<MarkerValue> getMarkerValues() {
        return markerValues;
    }

    public void setMarkerValues(List<MarkerValue> markerValues) {
        this.markerValues = markerValues;
    }
}

