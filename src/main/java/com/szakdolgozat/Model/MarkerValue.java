package com.szakdolgozat.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "MarkerValue")
public class MarkerValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // MANY marker values belong to ONE blood test
    @ManyToOne
    @JoinColumn(name = "blood_test_id", nullable = false)
    private BloodTest bloodTest;

    @Column(nullable = false)
    private String markerName;

    @Column(nullable = false)
    private Double value;

    private String unit;

    private Double referenceMin;

    private Double referenceMax;

    public MarkerValue() {}

    public MarkerValue(BloodTest bloodTest, String markerName, Double value, String unit,
                       Double referenceMin, Double referenceMax) {
        this.bloodTest = bloodTest;
        this.markerName = markerName;
        this.value = value;
        this.unit = unit;
        this.referenceMin = referenceMin;
        this.referenceMax = referenceMax;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BloodTest getBloodTest() {
        return bloodTest;
    }

    public void setBloodTest(BloodTest bloodTest) {
        this.bloodTest = bloodTest;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getReferenceMin() {
        return referenceMin;
    }

    public void setReferenceMin(Double referenceMin) {
        this.referenceMin = referenceMin;
    }

    public Double getReferenceMax() {
        return referenceMax;
    }

    public void setReferenceMax(Double referenceMax) {
        this.referenceMax = referenceMax;
    }
}
