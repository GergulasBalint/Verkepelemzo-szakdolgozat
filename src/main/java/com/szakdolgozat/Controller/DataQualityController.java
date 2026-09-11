package com.szakdolgozat.Controller;

import com.szakdolgozat.Service.DataQualityService;
import com.szakdolgozat.dto.DataQualityResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis")
public class DataQualityController {

    private final DataQualityService dataQualityService;

    public DataQualityController(
            DataQualityService dataQualityService) {

        this.dataQualityService =
                dataQualityService;
    }

    @GetMapping("/athlete/{athleteId}/data-quality")
    public ResponseEntity<DataQualityResponse> analyzeDataQuality(
            @PathVariable Long athleteId) {

        DataQualityResponse result =
                dataQualityService.analyzeAthlete(
                        athleteId
                );

        return ResponseEntity.ok(result);
    }
}