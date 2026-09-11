package com.szakdolgozat.dto;

import java.util.List;

public class DataQualityResponse {

    private Long athleteId;
    private String athleteName;

    private int totalMeasurements;
    private int numericMeasurements;
    private int missingValues;
    private int nonNumericValues;
    private int specialValues;

    private int markersWithIssues;

    private String overallQuality;

    private List<DataQualityIssue> issues;
    private int textValues;

    public DataQualityResponse(
            Long athleteId,
            String athleteName,
            int totalMeasurements,
            int numericMeasurements,
            int missingValues,
            int nonNumericValues,
            int specialValues,
            int textValues,
            int markersWithIssues,
            String overallQuality,
            List<DataQualityIssue> issues) {

        this.athleteId = athleteId;
        this.athleteName = athleteName;
        this.totalMeasurements = totalMeasurements;
        this.numericMeasurements = numericMeasurements;
        this.missingValues = missingValues;
        this.nonNumericValues = nonNumericValues;
        this.specialValues = specialValues;
        this.textValues = textValues;
        this.markersWithIssues = markersWithIssues;
        this.overallQuality = overallQuality;
        this.issues = issues;
    }

    public Long getAthleteId() {
        return athleteId;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public int getTotalMeasurements() {
        return totalMeasurements;
    }

    public int getNumericMeasurements() {
        return numericMeasurements;
    }

    public int getMissingValues() {
        return missingValues;
    }

    public int getNonNumericValues() {
        return nonNumericValues;
    }

    public int getSpecialValues() {
        return specialValues;
    }

    public int getMarkersWithIssues() {
        return markersWithIssues;
    }

    public String getOverallQuality() {
        return overallQuality;
    }

    public List<DataQualityIssue> getIssues() {
        return issues;
    }
    public int getTextValues() {
        return textValues;
    }
}