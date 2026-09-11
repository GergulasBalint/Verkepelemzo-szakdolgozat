package com.szakdolgozat.dto;

import com.szakdolgozat.Model.SampleType;

import java.util.List;

public class MarkerAnalysisResponse {

    private Long athleteId;
    private String athleteName;

    private Long markerId;
    private String markerName;
    private String unit;

    private SampleType sampleType;

    private int measurementCount;
    private int numericMeasurementCount;

    private Double average;
    private Double minimum;
    private Double maximum;

    private List<MeasurementPoint> measurements;

    public MarkerAnalysisResponse() {
    }

    public MarkerAnalysisResponse(
            Long athleteId,
            String athleteName,
            Long markerId,
            String markerName,
            String unit,
            SampleType sampleType,
            int measurementCount,
            int numericMeasurementCount,
            Double average,
            Double minimum,
            Double maximum,
            List<MeasurementPoint> measurements) {

        this.athleteId = athleteId;
        this.athleteName = athleteName;
        this.markerId = markerId;
        this.markerName = markerName;
        this.unit = unit;
        this.sampleType = sampleType;
        this.measurementCount = measurementCount;
        this.numericMeasurementCount = numericMeasurementCount;
        this.average = average;
        this.minimum = minimum;
        this.maximum = maximum;
        this.measurements = measurements;
    }

    public Long getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(Long athleteId) {
        this.athleteId = athleteId;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public Long getMarkerId() {
        return markerId;
    }

    public void setMarkerId(Long markerId) {
        this.markerId = markerId;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public int getMeasurementCount() {
        return measurementCount;
    }

    public void setMeasurementCount(int measurementCount) {
        this.measurementCount = measurementCount;
    }

    public int getNumericMeasurementCount() {
        return numericMeasurementCount;
    }

    public void setNumericMeasurementCount(int numericMeasurementCount) {
        this.numericMeasurementCount = numericMeasurementCount;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getMinimum() {
        return minimum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }

    public Double getMaximum() {
        return maximum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }

    public List<MeasurementPoint> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(
            List<MeasurementPoint> measurements) {

        this.measurements = measurements;
    }

    public static class MeasurementPoint {

        private String date;
        private String value;

        public MeasurementPoint() {
        }

        public MeasurementPoint(
                String date,
                String value) {

            this.date = date;
            this.value = value;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}