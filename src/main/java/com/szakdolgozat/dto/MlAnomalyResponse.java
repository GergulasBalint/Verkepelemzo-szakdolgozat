package com.szakdolgozat.dto;

import java.util.List;

public class MlAnomalyResponse {

    private Long athleteId;
    private int anomalyCount;
    private List<MlAnomaly> anomalies;

    public MlAnomalyResponse() {
    }

    public Long getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(Long athleteId) {
        this.athleteId = athleteId;
    }

    public int getAnomalyCount() {
        return anomalyCount;
    }

    public void setAnomalyCount(int anomalyCount) {
        this.anomalyCount = anomalyCount;
    }

    public List<MlAnomaly> getAnomalies() {
        return anomalies;
    }

    public void setAnomalies(List<MlAnomaly> anomalies) {
        this.anomalies = anomalies;
    }
}