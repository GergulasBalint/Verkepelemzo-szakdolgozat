package com.szakdolgozat.dto;

import com.szakdolgozat.Model.SampleType;

import java.time.LocalDate;

public class ExcelMeasurement {

    private LocalDate date;
    private String markerName;
    private String value;
    private SampleType sampleType;

    public ExcelMeasurement(
            LocalDate date,
            String markerName,
            String value,
            SampleType sampleType) {

        this.date = date;
        this.markerName = markerName;
        this.value = value;
        this.sampleType = sampleType;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMarkerName() {
        return markerName;
    }

    public String getValue() {
        return value;
    }

    public SampleType getSampleType() {
        return sampleType;
    }
}