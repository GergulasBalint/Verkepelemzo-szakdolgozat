package com.szakdolgozat.dto;

public class DataQualityIssue {

    private String markerName;
    private String issueType;
    private String details;

    public DataQualityIssue(
            String markerName,
            String issueType,
            String details) {

        this.markerName = markerName;
        this.issueType = issueType;
        this.details = details;
    }

    public String getMarkerName() {
        return markerName;
    }

    public String getIssueType() {
        return issueType;
    }

    public String getDetails() {
        return details;
    }
}