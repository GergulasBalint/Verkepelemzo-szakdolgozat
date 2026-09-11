package com.szakdolgozat.dto;

public class MarkerCorrelationResponse {

    private Long athleteId;
    private String athleteName;

    private Long marker1Id;
    private String marker1Name;

    private Long marker2Id;
    private String marker2Name;

    private String sampleType;

    private int sampleCount;

    private Double correlation;

    private String strength;

    public MarkerCorrelationResponse(
            Long athleteId,
            String athleteName,
            Long marker1Id,
            String marker1Name,
            Long marker2Id,
            String marker2Name,
            String sampleType,
            int sampleCount,
            Double correlation,
            String strength) {

        this.athleteId = athleteId;
        this.athleteName = athleteName;
        this.marker1Id = marker1Id;
        this.marker1Name = marker1Name;
        this.marker2Id = marker2Id;
        this.marker2Name = marker2Name;
        this.sampleType = sampleType;
        this.sampleCount = sampleCount;
        this.correlation = correlation;
        this.strength = strength;
    }

    public Long getAthleteId() {
        return athleteId;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public Long getMarker1Id() {
        return marker1Id;
    }

    public String getMarker1Name() {
        return marker1Name;
    }

    public Long getMarker2Id() {
        return marker2Id;
    }

    public String getMarker2Name() {
        return marker2Name;
    }

    public String getSampleType() {
        return sampleType;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public Double getCorrelation() {
        return correlation;
    }

    public String getStrength() {
        return strength;
    }
}