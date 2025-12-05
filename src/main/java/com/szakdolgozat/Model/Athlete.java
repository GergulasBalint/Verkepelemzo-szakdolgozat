package com.szakdolgozat.Model;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="athlete")
public class Athlete {
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private List<MarkerValue> markerValues = new ArrayList<>();

    public Athlete(Long id, String name, LocalDate birthDate, Gender gender) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public Athlete() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<MarkerValue> getMarkerValues() {
        return markerValues;
    }

    public void setMarkerValues(List<MarkerValue> markerValues) {
        this.markerValues = markerValues;
    }
}


