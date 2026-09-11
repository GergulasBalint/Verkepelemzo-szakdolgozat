package com.szakdolgozat.dto;

public class AutomaticCorrelationResponse {

    private Long marker1Id;
    private String marker1Name;

    private Long marker2Id;
    private String marker2Name;

    private int sampleCount;

    private Double correlation;

    private String strength;

    private String dataQuality;

    public AutomaticCorrelationResponse(
            Long marker1Id,
            String marker1Name,
            Long marker2Id,
            String marker2Name,
            int sampleCount,
            Double correlation,
            String strength,
            String dataQuality) {

        this.marker1Id = marker1Id;
        this.marker1Name = marker1Name;
        this.marker2Id = marker2Id;
        this.marker2Name = marker2Name;
        this.sampleCount = sampleCount;
        this.correlation = correlation;
        this.strength = strength;
        this.dataQuality = dataQuality;
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

    public int getSampleCount() {
        return sampleCount;
    }

    public Double getCorrelation() {
        return correlation;
    }

    public String getStrength() {
        return strength;
    }

    public String getDataQuality() {
        return dataQuality;
    }
}