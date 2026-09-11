package com.szakdolgozat.Controller;

import com.szakdolgozat.Service.MlAnalysisException;
import com.szakdolgozat.Service.MlAnalysisService;
import com.szakdolgozat.dto.MlAnalysisErrorResponse;
import com.szakdolgozat.dto.MlAnomalyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis")
public class MlAnalysisController {

    private final MlAnalysisService mlAnalysisService;

    public MlAnalysisController(
            MlAnalysisService mlAnalysisService) {

        this.mlAnalysisService =
                mlAnalysisService;
    }

    @GetMapping("/athlete/{athleteId}/anomalies")
    public ResponseEntity<?> analyzeAthlete(
            @PathVariable Long athleteId) {

        try {

            MlAnomalyResponse result =
                    mlAnalysisService.analyzeAthlete(
                            athleteId
                    );

            return ResponseEntity.ok(result);

        } catch (MlAnalysisException e) {

            String message =
                    e.getMessage();

            if (message != null &&
                    message.startsWith(
                            "A sportoló nem található")) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                                new MlAnalysisErrorResponse(
                                        "ATHLETE_NOT_FOUND",
                                        message
                                )
                        );
            }

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            new MlAnalysisErrorResponse(
                                    "ML_ANALYSIS_ERROR",
                                    message
                            )
                    );
        }
    }
}