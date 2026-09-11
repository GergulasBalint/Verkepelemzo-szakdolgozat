package com.szakdolgozat.dto;

import java.util.List;

public class MlAnomaly {

    private String date;
    private Double anomalyScore;
    private List<MlAnomalyMarker> markers;

    public MlAnomaly() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAnomalyScore() {
        return anomalyScore;
    }

    public void setAnomalyScore(Double anomalyScore) {
        this.anomalyScore = anomalyScore;
    }

    public List<MlAnomalyMarker> getMarkers() {
        return markers;
    }

    public void setMarkers(List<MlAnomalyMarker> markers) {
        this.markers = markers;
    }
}