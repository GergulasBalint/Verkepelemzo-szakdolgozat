package com.szakdolgozat.Service;

import com.szakdolgozat.Model.Athlete;
import com.szakdolgozat.Model.BloodTest;
import com.szakdolgozat.Model.Gender;
import com.szakdolgozat.Model.Marker;
import com.szakdolgozat.Model.MarkerValue;
import com.szakdolgozat.Model.SampleType;
import com.szakdolgozat.Repository.AthleteRepository;
import com.szakdolgozat.Repository.BloodTestRepository;
import com.szakdolgozat.Repository.MarkerRepository;
import com.szakdolgozat.Repository.MarkerValueRepository;
import com.szakdolgozat.dto.ExcelMeasurement;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelImportService {

    private final MarkerNormalizationService markerNormalizationService;
    private final AthleteRepository athleteRepository;
    private final BloodTestRepository bloodTestRepository;
    private final MarkerRepository markerRepository;
    private final MarkerValueRepository markerValueRepository;

    public ExcelImportService(
            MarkerNormalizationService markerNormalizationService,
            AthleteRepository athleteRepository,
            BloodTestRepository bloodTestRepository,
            MarkerRepository markerRepository,
            MarkerValueRepository markerValueRepository) {

        this.markerNormalizationService =
                markerNormalizationService;

        this.athleteRepository =
                athleteRepository;

        this.bloodTestRepository =
                bloodTestRepository;

        this.markerRepository =
                markerRepository;

        this.markerValueRepository =
                markerValueRepository;
    }

    /*
     * ==========================================
     * EXCEL IMPORT
     * ==========================================
     *
     * A fájlt most már InputStream-ként kapjuk.
     *
     * Ez lehet:
     * - feltöltött MultipartFile
     * - később akár más forrásból érkező fájl is
     */
    @Transactional
    public void importExcel(
            InputStream inputStream)
            throws IOException {

        try (Workbook workbook =
                     WorkbookFactory.create(inputStream)) {

            System.out.println();
            System.out.println("================================");
            System.out.println("EXCEL IMPORT INDÍTÁSA");
            System.out.println("================================");

            System.out.println(
                    "Sheetek száma: "
                            + workbook.getNumberOfSheets()
            );

            int totalMeasurements = 0;
            int savedMeasurements = 0;

            /*
             * Végigmegyünk az összes munkalapon.
             */
            for (int sheetIndex = 0;
                 sheetIndex < workbook.getNumberOfSheets();
                 sheetIndex++) {

                Sheet sheet =
                        workbook.getSheetAt(sheetIndex);

                String sheetName =
                        sheet.getSheetName();

                System.out.println();
                System.out.println(
                        "--- SHEET: "
                                + sheetName
                                + " ---"
                );

                /*
                 * A sheet neve alapján
                 * létrehozzuk vagy megkeressük
                 * a sportolót.
                 */
                Athlete athlete =
                        getOrCreateAthlete(sheetName);

                /*
                 * Beolvassuk a mérési adatokat.
                 */
                List<ExcelMeasurement> measurements =
                        readMeasurements(sheet);

                totalMeasurements +=
                        measurements.size();

                /*
                 * Minden mérési adatot elmentünk.
                 */
                for (ExcelMeasurement measurement :
                        measurements) {

                    /*
                     * Megkeressük vagy létrehozzuk
                     * az adott dátumhoz tartozó
                     * BloodTest rekordot.
                     */
                    BloodTest bloodTest =
                            getOrCreateBloodTest(
                                    athlete,
                                    measurement.getDate()
                            );

                    /*
                     * A marker nevét normalizáljuk.
                     *
                     * Például:
                     * Hemoglobin
                     * Haemoglobin
                     *
                     * ugyanahhoz a markerhez
                     * tartozhat.
                     */
                    String normalizedMarkerName =
                            markerNormalizationService
                                    .normalize(
                                            measurement
                                                    .getMarkerName()
                                    );

                    /*
                     * Megkeressük vagy létrehozzuk
                     * a markert.
                     */
                    Marker marker =
                            getOrCreateMarker(
                                    normalizedMarkerName
                            );

                    /*
                     * FONTOS:
                     *
                     * A BloodTest + Marker + SampleType
                     * együtt azonosítja a mérési rekordot.
                     *
                     * Ez lehetővé teszi például:
                     *
                     * Glucose + BLOOD
                     *
                     * és
                     *
                     * Glucose + URINE
                     *
                     * egyidejű tárolását.
                     */
                    MarkerValue markerValue =
                            markerValueRepository
                                    .findByBloodTestAndMarkerAndSampleType(
                                            bloodTest,
                                            marker,
                                            measurement
                                                    .getSampleType()
                                    )
                                    .orElse(null);

                    /*
                     * Ha még nincs ilyen rekord,
                     * létrehozzuk.
                     */
                    if (markerValue == null) {

                        markerValue =
                                new MarkerValue();

                        markerValue.setBloodTest(
                                bloodTest
                        );

                        markerValue.setMarker(
                                marker
                        );

                        markerValue.setValue(
                                measurement.getValue()
                        );

                        markerValue.setSampleType(
                                measurement.getSampleType()
                        );

                        markerValueRepository.save(
                                markerValue
                        );

                        savedMeasurements++;
                    }
                }

                System.out.println(
                        "Beolvasott mérések: "
                                + measurements.size()
                );
            }

            System.out.println();
            System.out.println("================================");
            System.out.println("EXCEL IMPORT KÉSZ");
            System.out.println("================================");

            System.out.println(
                    "Összes beolvasott mérés: "
                            + totalMeasurements
            );

            System.out.println(
                    "Újonnan mentett mérés: "
                            + savedMeasurements
            );

            System.out.println(
                    "Athlete-ek száma: "
                            + athleteRepository.count()
            );

            System.out.println(
                    "BloodTest-ek száma: "
                            + bloodTestRepository.count()
            );

            System.out.println(
                    "Markerek száma: "
                            + markerRepository.count()
            );

            System.out.println(
                    "MarkerValue-k száma: "
                            + markerValueRepository.count()
            );
        }
    }

    /*
     * ==========================================
     * ATHLETE
     * ==========================================
     */

    private Athlete getOrCreateAthlete(
            String sheetName) {

        return athleteRepository
                .findByName(sheetName)
                .orElseGet(() -> {

                    Athlete athlete =
                            new Athlete();

                    athlete.setName(sheetName);

                    /*
                     * Például:
                     *
                     * 2003_Male_Road
                     *
                     * első rész:
                     * 2003
                     */
                    String[] parts =
                            sheetName.split("_");

                    if (parts.length > 0) {

                        try {

                            int birthYear =
                                    Integer.parseInt(
                                            parts[0]
                                    );

                            /*
                             * Az Excelből csak a
                             * születési év ismert.
                             *
                             * Ezért ideiglenesen
                             * január 1-et használunk.
                             */
                            athlete.setBirthDate(
                                    LocalDate.of(
                                            birthYear,
                                            1,
                                            1
                                    )
                            );

                        } catch (Exception ignored) {
                        }
                    }

                    /*
                     * Nem alapján
                     * beállítjuk a Gender enumot.
                     */
                    String lowerSheetName =
                            sheetName.toLowerCase();

                    if (lowerSheetName.contains("male")) {

                        athlete.setGender(
                                Gender.MALE
                        );

                    } else if (
                            lowerSheetName.contains("female")
                    ) {

                        athlete.setGender(
                                Gender.FEMALE
                        );
                    }

                    return athleteRepository.save(
                            athlete
                    );
                });
    }

    /*
     * ==========================================
     * BLOOD TEST
     * ==========================================
     */

    private BloodTest getOrCreateBloodTest(
            Athlete athlete,
            LocalDate date) {

        return bloodTestRepository
                .findByAthleteAndDate(
                        athlete,
                        date
                )
                .orElseGet(() -> {

                    BloodTest bloodTest =
                            new BloodTest();

                    bloodTest.setAthlete(
                            athlete
                    );

                    bloodTest.setDate(
                            date
                    );

                    return bloodTestRepository.save(
                            bloodTest
                    );
                });
    }

    /*
     * ==========================================
     * MARKER
     * ==========================================
     */

    private Marker getOrCreateMarker(
            String markerName) {

        return markerRepository
                .findByName(markerName)
                .orElseGet(() -> {

                    Marker marker =
                            new Marker();

                    marker.setName(
                            markerName
                    );

                    /*
                     * Az egységeket később
                     * fogjuk kezelni.
                     */
                    marker.setUnit(null);

                    return markerRepository.save(
                            marker
                    );
                });
    }

    /*
     * ==========================================
     * EXCEL BEOLVASÁS
     * ==========================================
     */

    private List<ExcelMeasurement> readMeasurements(
            Sheet sheet) {

        List<ExcelMeasurement> measurements =
                new ArrayList<>();

        var headerRow =
                sheet.getRow(0);

        if (headerRow == null) {
            return measurements;
        }

        /*
         * Az első oszlopban vannak
         * a markernevek.
         *
         * A további oszlopok dátumok.
         */
        for (int columnIndex = 1;
             columnIndex < headerRow.getLastCellNum();
             columnIndex++) {

            var dateCell =
                    headerRow.getCell(columnIndex);

            /*
             * Ha nincs dátum,
             * nincs mit feldolgozni.
             */
            if (dateCell == null ||
                    dateCell.toString().isBlank()) {

                continue;
            }

            LocalDate date =
                    parseDate(
                            dateCell.toString()
                    );

            /*
             * Minden dátumoszlop
             * alapból vérvizsgálattal indul.
             */
            SampleType currentSampleType =
                    SampleType.BLOOD;

            /*
             * Végigmegyünk az összes soron.
             */
            for (int rowIndex = 1;
                 rowIndex <= sheet.getLastRowNum();
                 rowIndex++) {

                var row =
                        sheet.getRow(rowIndex);

                if (row == null) {
                    continue;
                }

                var markerCell =
                        row.getCell(0);

                if (markerCell == null ||
                        markerCell.toString().isBlank()) {

                    continue;
                }

                String markerName =
                        markerCell
                                .toString()
                                .trim();

                var valueCell =
                        row.getCell(columnIndex);

                boolean hasValue =
                        valueCell != null &&
                                !valueCell
                                        .toString()
                                        .isBlank();

                /*
                 * Megnézzük, hogy a sor
                 * szekciófejléc-e.
                 */
                SampleType sectionType =
                        detectSection(markerName);

                /*
                 * Szekciófejléc esetén
                 * megváltoztatjuk az aktuális
                 * mintatípust.
                 */
                if (!hasValue &&
                        sectionType != null) {

                    currentSampleType =
                            sectionType;

                    continue;
                }

                /*
                 * Üres mérési cellát
                 * nem mentünk.
                 */
                if (!hasValue) {
                    continue;
                }

                String value =
                        valueCell
                                .toString()
                                .trim();

                /*
                 * A marker neve, az értéke és
                 * az aktuális szekció alapján
                 * meghatározzuk a mintatípust.
                 */
                SampleType detectedType =
                        detectSampleType(
                                markerName,
                                value,
                                currentSampleType
                        );

                currentSampleType =
                        detectedType;

                measurements.add(
                        new ExcelMeasurement(
                                date,
                                markerName,
                                value,
                                detectedType
                        )
                );
            }
        }

        return measurements;
    }

    /*
     * ==========================================
     * SZEKCIÓ FELISMERÉS
     * ==========================================
     */

    private SampleType detectSection(
            String markerName) {

        String marker =
                markerName
                        .trim()
                        .toLowerCase();

        /*
         * VIZELET
         */
        if (marker.equals("urine") ||
                marker.equals("vizelet")) {

            return SampleType.URINE;
        }

        /*
         * VIZELET ÜLEDÉK
         */
        if (marker.equals("urine sediment") ||
                marker.equals("urine sedimentation") ||
                marker.equals("vizelet üledék")) {

            return SampleType.URINE_SEDIMENT;
        }

        /*
         * VÉRVIZSGÁLATI / LABOR SZEKCIÓK
         */
        if (marker.equals("chemistry") ||
                marker.equals("clinical chemistry") ||
                marker.equals("haematology") ||
                marker.equals("hematology") ||
                marker.equals("biochemistry") ||
                marker.equals("immunology") ||
                marker.equals("endocrinology") ||
                marker.equals("hormone") ||
                marker.equals("ionok") ||
                marker.equals("gfr")) {

            return SampleType.BLOOD;
        }

        return null;
    }

    /*
     * ==========================================
     * MINTATÍPUS FELISMERÉS
     * ==========================================
     */

    private SampleType detectSampleType(
            String markerName,
            String value,
            SampleType currentSampleType) {

        String marker =
                markerName
                        .trim()
                        .toLowerCase();

        /*
         * Ha üledékben vagyunk,
         * a vérsejtek is lehetnek
         * vizeletüledék markerek.
         */
        if (currentSampleType ==
                SampleType.URINE_SEDIMENT) {

            if (marker.equals(
                    "red blood cells") ||
                    marker.equals(
                            "white blood cells") ||
                    marker.equals("rbc") ||
                    marker.equals("wbc")) {

                return SampleType.URINE_SEDIMENT;
            }

            if (isUrineSedimentMarker(marker)) {

                return SampleType.URINE_SEDIMENT;
            }

            if (isStrongUrineMarker(marker)) {

                return SampleType.URINE;
            }

            if (isBloodMarker(marker)) {

                return SampleType.BLOOD;
            }

            return currentSampleType;
        }

        /*
         * Egyértelmű vizeletmarkerek.
         */
        if (isStrongUrineMarker(marker)) {

            return SampleType.URINE;
        }

        /*
         * Ha jelenleg vizeletben vagyunk.
         */
        if (currentSampleType ==
                SampleType.URINE) {

            /*
             * Vizeletben is előforduló
             * kémiai markerek.
             */
            if (isPossibleUrineChemistryMarker(
                    marker)) {

                return SampleType.URINE;
            }

            /*
             * Egyértelmű vérmarker esetén
             * visszatérünk BLOOD-ra.
             */
            if (isBloodMarker(marker)) {

                return SampleType.BLOOD;
            }

            return currentSampleType;
        }

        /*
         * Ha vérvizsgálati részben vagyunk,
         * egyértelmű vérmarker = BLOOD.
         */
        if (isBloodMarker(marker)) {

            return SampleType.BLOOD;
        }

        /*
         * Üledék marker.
         */
        if (isUrineSedimentMarker(marker)) {

            return SampleType.URINE_SEDIMENT;
        }

        /*
         * Alapértelmezés.
         */
        return SampleType.BLOOD;
    }

    /*
     * ==========================================
     * ERŐS VIZELET MARKEREK
     * ==========================================
     */

    private boolean isStrongUrineMarker(
            String marker) {

        return marker.equals("acetone") ||
                marker.equals("nitrite") ||
                marker.equals("specific gravity") ||
                marker.equals(
                        "specific gravity (sg)"
                ) ||
                marker.equals("urine color") ||
                marker.equals("urine turbidity") ||
                marker.equals(
                        "urine appearance"
                ) ||
                marker.equals("urine ph") ||
                marker.equals("urine protein") ||
                marker.equals("urine blood") ||
                marker.equals("urine glucose") ||
                marker.equals("urine sugar") ||
                marker.equals("urine acetone") ||
                marker.equals("urine bilirubin") ||
                marker.equals(
                        "urine urobilinogen"
                ) ||
                marker.equals("urobilinogen") ||
                marker.equals("leukocytes") ||
                marker.equals("leucocytes") ||
                marker.equals("leukocyta") ||
                marker.equals("sugar");
    }

    /*
     * ==========================================
     * VIZELETBEN IS ELŐFORDULÓ KÉMIAI MARKEREK
     * ==========================================
     */

    private boolean isPossibleUrineChemistryMarker(
            String marker) {

        return marker.equals("albumin") ||
                marker.equals("bilirubin") ||
                marker.equals("bilirubin #") ||
                marker.equals("blood") ||
                marker.equals("cholesterol") ||
                marker.equals("creatinine") ||
                marker.equals("gamma gt") ||
                marker.equals("glucose") ||
                marker.equals("got") ||
                marker.equals("gpt") ||
                marker.equals("hdl cholesterol") ||
                marker.equals("iron") ||
                marker.equals("potassium") ||
                marker.equals("protein") ||
                marker.equals(
                        "prothrombin inr"
                ) ||
                marker.equals("sodium") ||
                marker.equals(
                        "total bilirubin"
                ) ||
                marker.equals("total protein") ||
                marker.equals("triglycerides") ||
                marker.equals("amylase") ||
                marker.equals(
                        "alkaline phosphatase"
                ) ||
                marker.equals(
                        "aspartate aminotransferase"
                ) ||
                marker.equals(
                        "alanine aminotransferase"
                );
    }

    /*
     * ==========================================
     * VIZELET ÜLEDÉK MARKEREK
     * ==========================================
     */

    private boolean isUrineSedimentMarker(
            String marker) {

        return marker.equals(
                "abnormal cylinders"
        ) ||
                marker.equals("bacteria") ||
                marker.equals("yeast") ||
                marker.equals("mucus") ||
                marker.equals("crystals") ||
                marker.equals("casts") ||
                marker.equals(
                        "epithelial cells"
                ) ||
                marker.equals(
                        "squamous epithelial cells"
                ) ||
                marker.equals(
                        "renal epithelial cells"
                ) ||
                marker.equals(
                        "transitional epithelial cells"
                ) ||
                marker.equals("ash");
    }

    /*
     * ==========================================
     * VÉR MARKEREK
     * ==========================================
     */

    private boolean isBloodMarker(
            String marker) {

        return marker.equals(
                "white blood cells"
        ) ||
                marker.equals(
                        "white blood cell count"
                ) ||
                marker.equals(
                        "red blood cells"
                ) ||
                marker.equals(
                        "red blood cell count"
                ) ||
                marker.equals("hemoglobin") ||
                marker.equals("haemoglobin") ||
                marker.equals("hematocrit") ||
                marker.equals("haematocrit") ||
                marker.equals("mcv") ||
                marker.equals("mch") ||
                marker.equals("mchc") ||
                marker.equals("rdw") ||
                marker.equals("platelets") ||
                marker.equals("thrombocytes") ||
                marker.equals("neutrophils") ||
                marker.equals("neutrophils #") ||
                marker.equals("neutrophils %") ||
                marker.equals("lymphocytes") ||
                marker.equals("lymphocytes #") ||
                marker.equals("lymphocytes %") ||
                marker.equals("monocytes") ||
                marker.equals("monocytes #") ||
                marker.equals("eosinophils") ||
                marker.equals("eosinophils #") ||
                marker.equals("basophils") ||
                marker.equals("basophils #") ||
                marker.equals("ferritin") ||
                marker.equals("ck") ||
                marker.equals("crp") ||
                marker.equals("tsh") ||
                marker.equals("vitamin d");
    }

    /*
     * ==========================================
     * DÁTUM FELISMERÉS
     * ==========================================
     */

    private LocalDate parseDate(
            String dateText) {

        dateText =
                dateText.trim();

        /*
         * Például:
         *
         * 2023.09.08 (NED)
         *
         * -> 2023.09.08
         */
        int parenthesisIndex =
                dateText.indexOf("(");

        if (parenthesisIndex != -1) {

            dateText =
                    dateText.substring(
                            0,
                            parenthesisIndex
                    ).trim();
        }

        /*
         * Például:
         *
         * 2023.12.15-18
         *
         * -> 2023.12.15
         */
        if (dateText.matches(
                "\\d{4}\\.\\d{2}\\.\\d{2}-\\d{2}"
        )) {

            dateText =
                    dateText.substring(
                            0,
                            10
                    );
        }

        /*
         * Például:
         *
         * 2021.04.27.
         *
         * -> 2021.04.27
         */
        if (dateText.endsWith(".")) {

            dateText =
                    dateText.substring(
                            0,
                            dateText.length() - 1
                    );
        }

        List<DateTimeFormatter> formatters =
                List.of(
                        DateTimeFormatter.ofPattern(
                                "yyyy.MM.dd"
                        ),
                        DateTimeFormatter.ofPattern(
                                "dd.MM.yyyy"
                        ),
                        DateTimeFormatter.ofPattern(
                                "M/d/yy"
                        ),
                        DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd"
                        )
                );

        for (DateTimeFormatter formatter :
                formatters) {

            try {

                return LocalDate.parse(
                        dateText,
                        formatter
                );

            } catch (Exception ignored) {
            }
        }

        throw new IllegalArgumentException(
                "Ismeretlen dátumformátum: "
                        + dateText
        );
    }
}