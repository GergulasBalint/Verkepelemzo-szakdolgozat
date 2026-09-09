package com.szakdolgozat.Model;


import jakarta.persistence.*;

@Entity
@Table(name = "marker")
public class Marker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String unit;

    public Marker(Long id, String name, String unit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
    }

    public Marker() {}

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
