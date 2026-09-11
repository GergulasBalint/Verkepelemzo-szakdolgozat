package com.szakdolgozat.Controller;

import com.szakdolgozat.Model.Marker;
import com.szakdolgozat.Model.SampleType;
import com.szakdolgozat.Service.AnalysisService;
import com.szakdolgozat.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(
            AnalysisService analysisService) {

        this.analysisService =
                analysisService;
    }

    /*
     * ==========================================
     * MARKER IDŐBELI ELEMZÉSE
     * ==========================================
     *
     * Példa:
     *
     * GET
     * /analysis/athlete/4/marker/1?sampleType=BLOOD
     */
    @GetMapping(
            "/athlete/{athleteId}/marker/{markerId}"
    )
    public ResponseEntity<MarkerAnalysisResponse>
    analyzeMarker(
            @PathVariable Long athleteId,
            @PathVariable Long markerId,
            @RequestParam
            SampleType sampleType) {

        MarkerAnalysisResponse result =
                analysisService.analyzeMarker(
                        athleteId,
                        markerId,
                        sampleType
                );

        return ResponseEntity.ok(result);
    }


    @GetMapping("/athlete/{athleteId}/date/{date}")
    public ResponseEntity<MultiMarkerAnalysisResponse> analyzeBloodTest(
            @PathVariable Long athleteId,
            @PathVariable LocalDate date) {

        MultiMarkerAnalysisResponse result =
                analysisService.analyzeBloodTest(
                        athleteId,
                        date
                );

        return ResponseEntity.ok(result);
    }

    @GetMapping("/athlete/{athleteId}/correlation")
    public ResponseEntity<MarkerCorrelationResponse> analyzeCorrelation(
            @PathVariable Long athleteId,
            @RequestParam Long marker1Id,
            @RequestParam Long marker2Id,
            @RequestParam SampleType sampleType) {

        MarkerCorrelationResponse result =
                analysisService.analyzeCorrelation(
                        athleteId,
                        marker1Id,
                        marker2Id,
                        sampleType
                );

        return ResponseEntity.ok(result);
    }

    @GetMapping("/athlete/{athleteId}/correlations")
    public ResponseEntity<List<AutomaticCorrelationResponse>> analyzeAllCorrelations(
            @PathVariable Long athleteId,
            @RequestParam SampleType sampleType) {

        List<AutomaticCorrelationResponse> result =
                analysisService.analyzeAllCorrelations(
                        athleteId,
                        sampleType
                );

        return ResponseEntity.ok(result);
    }

    @GetMapping("/athlete/{athleteId}/ml-data")
    public ResponseEntity<List<MlMeasurement>> getMlData(
            @PathVariable Long athleteId) {

        return ResponseEntity.ok(
                analysisService.getMlMeasurements(
                        athleteId
                )
        );
    }

    @GetMapping("/athlete/{athleteId}/markers")
    public ResponseEntity<List<Marker>> getAthleteMarkers(
            @PathVariable Long athleteId) {

        return ResponseEntity.ok(
                analysisService.getMarkersForAthlete(
                        athleteId
                )
        );
    }
}