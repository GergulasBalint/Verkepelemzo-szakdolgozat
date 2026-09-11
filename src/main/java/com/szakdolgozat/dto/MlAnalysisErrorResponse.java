package com.szakdolgozat.dto;

public class MlAnalysisErrorResponse {

    private String error;
    private String message;

    public MlAnalysisErrorResponse(
            String error,
            String message) {

        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}