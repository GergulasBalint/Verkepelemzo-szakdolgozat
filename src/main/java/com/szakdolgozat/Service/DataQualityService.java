package com.szakdolgozat.Service;

import com.szakdolgozat.Model.Athlete;
import com.szakdolgozat.Model.MarkerValue;
import com.szakdolgozat.Repository.AthleteRepository;
import com.szakdolgozat.Repository.MarkerValueRepository;
import com.szakdolgozat.dto.DataQualityIssue;
import com.szakdolgozat.dto.DataQualityResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DataQualityService {

    private final AthleteRepository athleteRepository;
    private final MarkerValueRepository markerValueRepository;

    public DataQualityService(
            AthleteRepository athleteRepository,
            MarkerValueRepository markerValueRepository) {

        this.athleteRepository = athleteRepository;
        this.markerValueRepository = markerValueRepository;
    }

    public DataQualityResponse analyzeAthlete(Long athleteId) {

        Athlete athlete = athleteRepository.findById(athleteId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "A sportoló nem található: " + athleteId
                        )
                );

        List<MarkerValue> markerValues =
                markerValueRepository.findByBloodTest_Athlete(
                        athlete
                );

        int totalMeasurements = markerValues.size();

        int numericMeasurements = 0;
        int missingValues = 0;
        int nonNumericValues = 0;
        int specialValues = 0;
        int textValues = 0;

        List<DataQualityIssue> issues =
                new ArrayList<>();

        Set<String> markersWithIssues =
                new HashSet<>();

        Map<String, Integer> markerMeasurementCount =
                new HashMap<>();

        for (MarkerValue markerValue : markerValues) {

            String markerName =
                    markerValue.getMarker().getName();

            String value =
                    markerValue.getValue();

            markerMeasurementCount.put(
                    markerName,
                    markerMeasurementCount.getOrDefault(
                            markerName,
                            0
                    ) + 1
            );

            // 1. Hiányzó érték
            if (value == null ||
                    value.trim().isEmpty()) {

                missingValues++;

                issues.add(
                        new DataQualityIssue(
                                markerName,
                                "MISSING_VALUE",
                                "A méréshez nem tartozik érték."
                        )
                );

                markersWithIssues.add(markerName);

                continue;
            }

            String trimmedValue =
                    value.trim();

// 2. Speciális érték
            if (containsSpecialFormat(trimmedValue)) {

                specialValues++;

                issues.add(
                        new DataQualityIssue(
                                markerName,
                                "SPECIAL_VALUE",
                                "Speciális értékformátum: "
                                        + trimmedValue
                        )
                );

                markersWithIssues.add(markerName);

                continue;
            }

// 3. Numerikus érték
            if (isNumeric(trimmedValue)) {

                numericMeasurements++;

            } else if (isKnownTextValue(trimmedValue)) {

                // 4. Elfogadott szöveges érték
                textValues++;

            } else {

                // 5. Ténylegesen nem értelmezhető érték
                nonNumericValues++;

                issues.add(
                        new DataQualityIssue(
                                markerName,
                                "NON_NUMERIC",
                                "Nem értelmezhető érték: "
                                        + trimmedValue
                        )
                );

                markersWithIssues.add(markerName);
            }
        }

        String overallQuality =
                determineOverallQuality(
                        totalMeasurements,
                        missingValues,
                        nonNumericValues,
                        specialValues
                );

        return new DataQualityResponse(
                athlete.getId(),
                athlete.getName(),
                totalMeasurements,
                numericMeasurements,
                missingValues,
                nonNumericValues,
                specialValues,
                textValues,
                markersWithIssues.size(),
                overallQuality,
                issues
        );
    }

    private boolean isNumeric(String value) {

        try {

            Double.parseDouble(
                    value.replace(",", ".")
            );

            return true;

        } catch (NumberFormatException e) {

            return false;
        }
    }

    private boolean containsSpecialFormat(String value) {

        return value.startsWith("<")
                || value.startsWith(">")
                || value.contains("*");
    }

    private String determineOverallQuality(
            int total,
            int missing,
            int nonNumeric,
            int special) {

        if (total == 0) {
            return "NO_DATA";
        }

        double problematic =
                missing
                        + nonNumeric
                        + special;

        double ratio =
                problematic / total;

        if (ratio == 0) {
            return "EXCELLENT";
        }

        if (ratio <= 0.05) {
            return "GOOD";
        }

        if (ratio <= 0.15) {
            return "ACCEPTABLE";
        }

        return "POOR";
    }

    private boolean isKnownTextValue(String value) {

        String normalized =
                value.trim().toLowerCase();

        return normalized.equals("negatív")
                || normalized.equals("neg")
                || normalized.equals("negative")
                || normalized.equals("normal")
                || normalized.equals("pozitív")
                || normalized.equals("poz")
                || normalized.equals("positive");
    }
}