package com.szakdolgozat.dto;

import java.time.LocalDate;
import java.util.List;

public class MultiMarkerAnalysisResponse {

    private Long athleteId;
    private String athleteName;

    private LocalDate date;

    private int measurementCount;
    private int numericMeasurementCount;

    private List<MarkerAnalysisItem> markers;

    public MultiMarkerAnalysisResponse(
            Long athleteId,
            String athleteName,
            LocalDate date,
            int measurementCount,
            int numericMeasurementCount,
            List<MarkerAnalysisItem> markers) {

        this.athleteId = athleteId;
        this.athleteName = athleteName;
        this.date = date;
        this.measurementCount = measurementCount;
        this.numericMeasurementCount = numericMeasurementCount;
        this.markers = markers;
    }

    public Long getAthleteId() {
        return athleteId;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getMeasurementCount() {
        return measurementCount;
    }

    public int getNumericMeasurementCount() {
        return numericMeasurementCount;
    }

    public List<MarkerAnalysisItem> getMarkers() {
        return markers;
    }

    public static class MarkerAnalysisItem {

        private Long markerId;
        private String markerName;
        private String unit;
        private String value;
        private Double numericValue;
        private String sampleType;

        public MarkerAnalysisItem(
                Long markerId,
                String markerName,
                String unit,
                String value,
                Double numericValue,
                String sampleType) {

            this.markerId = markerId;
            this.markerName = markerName;
            this.unit = unit;
            this.value = value;
            this.numericValue = numericValue;
            this.sampleType = sampleType;
        }

        public Long getMarkerId() {
            return markerId;
        }

        public String getMarkerName() {
            return markerName;
        }

        public String getUnit() {
            return unit;
        }

        public String getValue() {
            return value;
        }

        public Double getNumericValue() {
            return numericValue;
        }

        public String getSampleType() {
            return sampleType;
        }
    }
}