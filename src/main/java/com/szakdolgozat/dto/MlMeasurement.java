package com.szakdolgozat.dto;

import com.szakdolgozat.Model.SampleType;

import java.time.LocalDate;

public class MlMeasurement {

    private LocalDate date;
    private String marker;
    private Double value;
    private SampleType sampleType;

    public MlMeasurement(
            LocalDate date,
            String marker,
            Double value,
            SampleType sampleType) {

        this.date = date;
        this.marker = marker;
        this.value = value;
        this.sampleType = sampleType;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMarker() {
        return marker;
    }

    public Double getValue() {
        return value;
    }

    public SampleType getSampleType() {
        return sampleType;
    }
}