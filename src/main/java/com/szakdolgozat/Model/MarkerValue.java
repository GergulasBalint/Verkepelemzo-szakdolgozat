package com.szakdolgozat.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "marker_value")
public class MarkerValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // MANY marker values belong to ONE blood test
    @ManyToOne
    @JoinColumn(name = "blood_test_id", nullable = false)
    private BloodTest bloodTest;

    @ManyToOne
    @JoinColumn(name = "marker_id", nullable = false)
    private Marker marker;

    @Column(nullable = false)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SampleType sampleType;


    public MarkerValue(
            Long id,
            BloodTest bloodTest,
            Marker marker,
            String value,
            SampleType sampleType) {

        this.id = id;
        this.bloodTest = bloodTest;
        this.marker = marker;
        this.value = value;
        this.sampleType = sampleType;
    }

    public MarkerValue() {}


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


    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public SampleType getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }
}