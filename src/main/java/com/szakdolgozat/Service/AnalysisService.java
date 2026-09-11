package com.szakdolgozat.Service;

import com.szakdolgozat.Model.Athlete;
import com.szakdolgozat.Model.BloodTest;
import com.szakdolgozat.Model.Marker;
import com.szakdolgozat.Model.MarkerValue;
import com.szakdolgozat.Model.SampleType;
import com.szakdolgozat.Repository.AthleteRepository;
import com.szakdolgozat.Repository.BloodTestRepository;
import com.szakdolgozat.Repository.MarkerRepository;
import com.szakdolgozat.Repository.MarkerValueRepository;
import com.szakdolgozat.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {

    private final AthleteRepository athleteRepository;
    private final MarkerRepository markerRepository;
    private final MarkerValueRepository markerValueRepository;
    private final BloodTestRepository bloodTestRepository;

    public AnalysisService(
            AthleteRepository athleteRepository,
            MarkerRepository markerRepository,
            MarkerValueRepository markerValueRepository,
            BloodTestRepository bloodTestRepository) {

        this.athleteRepository = athleteRepository;
        this.markerRepository = markerRepository;
        this.markerValueRepository = markerValueRepository;
        this.bloodTestRepository = bloodTestRepository;
    }

    // =========================================================
    // 1. EGY MARKER IDŐBELI ELEMZÉSE
    // =========================================================

    public MarkerAnalysisResponse analyzeMarker(
            Long athleteId,
            Long markerId,
            SampleType sampleType) {

        Athlete athlete = athleteRepository
                .findById(athleteId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Sportoló nem található: " + athleteId
                        )
                );

        Marker marker = markerRepository
                .findById(markerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Marker nem található: " + markerId
                        )
                );

        List<MarkerValue> markerValues =
                markerValueRepository
                        .findByBloodTest_AthleteAndMarkerAndSampleTypeOrderByBloodTest_DateAsc(
                                athlete,
                                marker,
                                sampleType
                        );

        List<MarkerAnalysisResponse.MeasurementPoint> measurements =
                new ArrayList<>();

        List<Double> numericValues =
                new ArrayList<>();

        for (MarkerValue markerValue : markerValues) {

            String rawValue =
                    markerValue.getValue();

            Double numericValue =
                    parseNumericValue(rawValue);

            if (numericValue != null) {
                numericValues.add(numericValue);
            }

            measurements.add(
                    new MarkerAnalysisResponse.MeasurementPoint(
                            markerValue
                                    .getBloodTest()
                                    .getDate()
                                    .toString(),
                            rawValue
                    )
            );
        }

        Double average = null;
        Double minimum = null;
        Double maximum = null;

        if (!numericValues.isEmpty()) {

            double sum = 0;

            for (Double value : numericValues) {
                sum += value;
            }

            average =
                    sum / numericValues.size();

            minimum =
                    numericValues.get(0);

            maximum =
                    numericValues.get(0);

            for (Double value : numericValues) {

                if (value < minimum) {
                    minimum = value;
                }

                if (value > maximum) {
                    maximum = value;
                }
            }
        }

        return new MarkerAnalysisResponse(
                athlete.getId(),
                athlete.getName(),
                marker.getId(),
                marker.getName(),
                marker.getUnit(),
                sampleType,
                markerValues.size(),
                numericValues.size(),
                average,
                minimum,
                maximum,
                measurements
        );
    }

    // =========================================================
    // 2. EGY VÉRVÉTEL TÖBB MARKERES ELEMZÉSE
    // =========================================================

    public MultiMarkerAnalysisResponse analyzeBloodTest(
            Long athleteId,
            LocalDate date) {

        Athlete athlete = athleteRepository
                .findById(athleteId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Sportoló nem található: " + athleteId
                        )
                );

        BloodTest bloodTest = bloodTestRepository
                .findByAthleteAndDate(athlete, date)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vérvétel nem található ezen a dátumon: "
                                        + date
                        )
                );

        List<MarkerValue> markerValues =
                bloodTest.getMarkerValues();

        List<MultiMarkerAnalysisResponse.MarkerAnalysisItem> markers =
                new ArrayList<>();

        int numericMeasurementCount = 0;

        for (MarkerValue markerValue : markerValues) {

            String rawValue =
                    markerValue.getValue();

            Double numericValue =
                    parseNumericValue(rawValue);

            if (numericValue != null) {
                numericMeasurementCount++;
            }

            markers.add(
                    new MultiMarkerAnalysisResponse.MarkerAnalysisItem(
                            markerValue.getMarker().getId(),
                            markerValue.getMarker().getName(),
                            markerValue.getMarker().getUnit(),
                            rawValue,
                            numericValue,
                            markerValue.getSampleType().name()
                    )
            );
        }

        return new MultiMarkerAnalysisResponse(
                athlete.getId(),
                athlete.getName(),
                bloodTest.getDate(),
                markerValues.size(),
                numericMeasurementCount,
                markers
        );
    }

    // =========================================================
    // 3. KÉT MEGADOTT MARKER KORRELÁCIÓJA
    // =========================================================

    public MarkerCorrelationResponse analyzeCorrelation(
            Long athleteId,
            Long marker1Id,
            Long marker2Id,
            SampleType sampleType) {

        Athlete athlete = athleteRepository
                .findById(athleteId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Sportoló nem található: " + athleteId
                        )
                );

        Marker marker1 = markerRepository
                .findById(marker1Id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Az első marker nem található: "
                                        + marker1Id
                        )
                );

        Marker marker2 = markerRepository
                .findById(marker2Id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "A második marker nem található: "
                                        + marker2Id
                        )
                );

        List<MarkerValue> values1 =
                markerValueRepository
                        .findByBloodTest_AthleteAndMarkerAndSampleTypeOrderByBloodTest_DateAsc(
                                athlete,
                                marker1,
                                sampleType
                        );

        List<MarkerValue> values2 =
                markerValueRepository
                        .findByBloodTest_AthleteAndMarkerAndSampleTypeOrderByBloodTest_DateAsc(
                                athlete,
                                marker2,
                                sampleType
                        );

        List<Double> xValues =
                new ArrayList<>();

        List<Double> yValues =
                new ArrayList<>();

        for (MarkerValue value1 : values1) {

            Double numericValue1 =
                    parseNumericValue(
                            value1.getValue()
                    );

            if (numericValue1 == null) {
                continue;
            }

            Long bloodTestId1 =
                    value1.getBloodTest().getId();

            for (MarkerValue value2 : values2) {

                Long bloodTestId2 =
                        value2.getBloodTest().getId();

                if (!bloodTestId1.equals(bloodTestId2)) {
                    continue;
                }

                Double numericValue2 =
                        parseNumericValue(
                                value2.getValue()
                        );

                if (numericValue2 == null) {
                    continue;
                }

                xValues.add(numericValue1);
                yValues.add(numericValue2);

                break;
            }
        }

        if (xValues.size() < 2) {

            return new MarkerCorrelationResponse(
                    athlete.getId(),
                    athlete.getName(),
                    marker1.getId(),
                    marker1.getName(),
                    marker2.getId(),
                    marker2.getName(),
                    sampleType.name(),
                    xValues.size(),
                    null,
                    "INSUFFICIENT_DATA"
            );
        }

        double correlation =
                calculatePearsonCorrelation(
                        xValues,
                        yValues
                );

        String strength =
                getCorrelationStrength(
                        correlation
                );

        return new MarkerCorrelationResponse(
                athlete.getId(),
                athlete.getName(),
                marker1.getId(),
                marker1.getName(),
                marker2.getId(),
                marker2.getName(),
                sampleType.name(),
                xValues.size(),
                correlation,
                strength
        );
    }

    // =========================================================
    // 4. AUTOMATIKUS KORRELÁCIÓELEMZÉS
    // =========================================================

    public List<AutomaticCorrelationResponse> analyzeAllCorrelations(
            Long athleteId,
            SampleType sampleType) {

        Athlete athlete = athleteRepository
                .findById(athleteId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Sportoló nem található: " + athleteId
                        )
                );

        List<MarkerValue> allValues =
                markerValueRepository
                        .findByBloodTest_AthleteAndSampleType(
                                athlete,
                                sampleType
                        );

        Map<Long, List<MarkerValue>> valuesByMarker =
                new HashMap<>();

        for (MarkerValue markerValue : allValues) {

            Long markerId =
                    markerValue
                            .getMarker()
                            .getId();

            valuesByMarker
                    .computeIfAbsent(
                            markerId,
                            key -> new ArrayList<>()
                    )
                    .add(markerValue);
        }

        List<Long> markerIds =
                new ArrayList<>(
                        valuesByMarker.keySet()
                );

        List<AutomaticCorrelationResponse> results =
                new ArrayList<>();

        for (int i = 0;
             i < markerIds.size();
             i++) {

            Long marker1Id =
                    markerIds.get(i);

            List<MarkerValue> marker1Values =
                    valuesByMarker.get(marker1Id);

            for (int j = i + 1;
                 j < markerIds.size();
                 j++) {

                Long marker2Id =
                        markerIds.get(j);

                List<MarkerValue> marker2Values =
                        valuesByMarker.get(marker2Id);

                List<Double> xValues =
                        new ArrayList<>();

                List<Double> yValues =
                        new ArrayList<>();

                for (MarkerValue value1 : marker1Values) {

                    Double numericValue1 =
                            parseNumericValue(
                                    value1.getValue()
                            );

                    if (numericValue1 == null) {
                        continue;
                    }

                    Long bloodTestId1 =
                            value1
                                    .getBloodTest()
                                    .getId();

                    for (MarkerValue value2 : marker2Values) {

                        Long bloodTestId2 =
                                value2
                                        .getBloodTest()
                                        .getId();

                        if (!bloodTestId1.equals(
                                bloodTestId2)) {

                            continue;
                        }

                        Double numericValue2 =
                                parseNumericValue(
                                        value2.getValue()
                                );

                        if (numericValue2 == null) {
                            continue;
                        }

                        xValues.add(numericValue1);
                        yValues.add(numericValue2);

                        break;
                    }
                }

                /*
                 * Legalább 3 közös mérés szükséges.
                 */
                if (xValues.size() < 3) {
                    continue;
                }

                double correlation =
                        calculatePearsonCorrelation(
                                xValues,
                                yValues
                        );

                String strength =
                        getCorrelationStrength(
                                correlation
                        );

                String dataQuality =
                        getCorrelationDataQuality(
                                xValues.size()
                        );

                Marker marker1 =
                        marker1Values
                                .get(0)
                                .getMarker();

                Marker marker2 =
                        marker2Values
                                .get(0)
                                .getMarker();

                results.add(
                        new AutomaticCorrelationResponse(
                                marker1.getId(),
                                marker1.getName(),
                                marker2.getId(),
                                marker2.getName(),
                                xValues.size(),
                                correlation,
                                strength,
                                dataQuality
                        )
                );
            }
        }

        /*
         * A legerősebb korrelációk kerülnek előre.
         */
        results.sort(
                Comparator.comparing(
                        AutomaticCorrelationResponse::getCorrelation,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()
                        )
                )
        );

        return results;
    }

    // =========================================================
    // 5. PEARSON KORRELÁCIÓ
    // =========================================================

    private double calculatePearsonCorrelation(
            List<Double> xValues,
            List<Double> yValues) {

        int n =
                xValues.size();

        double sumX = 0;
        double sumY = 0;

        for (int i = 0; i < n; i++) {

            sumX += xValues.get(i);
            sumY += yValues.get(i);
        }

        double averageX =
                sumX / n;

        double averageY =
                sumY / n;

        double numerator = 0;
        double denominatorX = 0;
        double denominatorY = 0;

        for (int i = 0; i < n; i++) {

            double differenceX =
                    xValues.get(i) - averageX;

            double differenceY =
                    yValues.get(i) - averageY;

            numerator +=
                    differenceX * differenceY;

            denominatorX +=
                    differenceX * differenceX;

            denominatorY +=
                    differenceY * differenceY;
        }

        if (denominatorX == 0 ||
                denominatorY == 0) {

            return 0;
        }

        return numerator /
                Math.sqrt(
                        denominatorX * denominatorY
                );
    }

    // =========================================================
    // 6. KORRELÁCIÓ ERŐSSÉGE
    // =========================================================

    private String getCorrelationStrength(
            double correlation) {

        double absoluteValue =
                Math.abs(correlation);

        if (absoluteValue >= 0.8) {
            return "VERY_STRONG";
        }

        if (absoluteValue >= 0.6) {
            return "STRONG";
        }

        if (absoluteValue >= 0.4) {
            return "MODERATE";
        }

        if (absoluteValue >= 0.2) {
            return "WEAK";
        }

        return "VERY_WEAK";
    }

    // =========================================================
    // 7. KORRELÁCIÓ ADATMINŐSÉGE
    // =========================================================

    private String getCorrelationDataQuality(
            int sampleCount) {

        if (sampleCount >= 10) {
            return "SUFFICIENT_DATA";
        }

        if (sampleCount >= 5) {
            return "LIMITED_DATA";
        }

        return "LOW_DATA";
    }

    // =========================================================
    // 8. SZÖVEGES ÉRTÉK → SZÁM
    // =========================================================

    private Double parseNumericValue(
            String rawValue) {

        if (rawValue == null) {
            return null;
        }

        String value =
                rawValue.trim();

        if (value.isEmpty()) {
            return null;
        }

        // A < és > jelű értékeket nem használjuk ML-hez,
        // mert ezek nem pontos mérési értékek.
        if (value.startsWith("<") ||
                value.startsWith(">")) {

            return null;
        }

        try {

            value =
                    value.replace(",", ".");

            value =
                    value.replace("*", "");

            return Double.parseDouble(value);

        } catch (NumberFormatException e) {

            return null;
        }
    }
    public List<MlMeasurement> getMlMeasurements(Long athleteId) {

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

        List<MlMeasurement> result =
                new ArrayList<>();

        for (MarkerValue markerValue : markerValues) {

            Double numericValue =
                    parseNumericValue(
                            markerValue.getValue()
                    );

            if (numericValue == null) {
                continue;
            }

            result.add(
                    new MlMeasurement(
                            markerValue.getBloodTest().getDate(),
                            markerValue.getMarker().getName(),
                            numericValue,
                            markerValue.getSampleType()
                    )
            );
        }

        return result;
    }

    public List<Marker> getMarkersForAthlete(Long athleteId) {

        Athlete athlete =
                athleteRepository.findById(athleteId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "A sportoló nem található: "
                                                + athleteId
                                )
                        );

        List<MarkerValue> markerValues =
                markerValueRepository.findByBloodTest_AthleteAndSampleType(
                        athlete,
                        SampleType.BLOOD
                );

        return markerValues.stream()
                .map(MarkerValue::getMarker)
                .distinct()
                .sorted(
                        Comparator.comparing(
                                Marker::getName,
                                String.CASE_INSENSITIVE_ORDER
                        )
                )
                .toList();
    }
}