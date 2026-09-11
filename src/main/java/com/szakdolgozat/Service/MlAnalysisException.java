package com.szakdolgozat.Service;

public class MlAnalysisException extends RuntimeException {

    public MlAnalysisException(String message) {
        super(message);
    }

    public MlAnalysisException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}