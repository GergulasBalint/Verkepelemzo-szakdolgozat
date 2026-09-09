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
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
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

    @Transactional
    public void importExcel(String filePath) {

        try {

            ClassPathResource resource =
                    new ClassPathResource(filePath);

            try (var inputStream = resource.getInputStream();
                 Workbook workbook =
                         WorkbookFactory.create(inputStream)) {

                System.out.println();
                System.out.println("================================");
                System.out.println("EXCEL IMPORT INDÍTÁSA");
                System.out.println("================================");

                System.out.println(
                        "Sheetek száma: " +
                                workbook.getNumberOfSheets()
                );

                int totalMeasurements = 0;
                int savedMeasurements = 0;

                for (int sheetIndex = 0;
                     sheetIndex < workbook.getNumberOfSheets();
                     sheetIndex++) {

                    Sheet sheet =
                            workbook.getSheetAt(sheetIndex);

                    String sheetName =
                            sheet.getSheetName();

                    System.out.println();
                    System.out.println(
                            "--- SHEET: " +
                                    sheetName +
                                    " ---"
                    );

                    /*
                     * 1. Sportoló létrehozása / megkeresése
                     */
                    Athlete athlete =
                            getOrCreateAthlete(sheetName);

                    System.out.println(
                            "Athlete: " +
                                    athlete.getName()
                    );

                    /*
                     * 2. Excel adatok beolvasása
                     */
                    List<ExcelMeasurement> measurements =
                            readMeasurements(sheet);

                    totalMeasurements +=
                            measurements.size();

                    /*
                     * 3. Mérések mentése
                     */
                    for (ExcelMeasurement measurement :
                            measurements) {

                        /*
                         * Vérvizsgálat megkeresése vagy létrehozása
                         */
                        BloodTest bloodTest =
                                getOrCreateBloodTest(
                                        athlete,
                                        measurement.getDate()
                                );

                        /*
                         * Marker normalizálása
                         */
                        String normalizedMarkerName =
                                markerNormalizationService.normalize(
                                        measurement.getMarkerName()
                                );

                        /*
                         * Marker megkeresése vagy létrehozása
                         */
                        Marker marker =
                                getOrCreateMarker(
                                        normalizedMarkerName
                                );

                        /*
                         * MarkerValue megkeresése vagy létrehozása
                         *
                         * Jelenleg még csak BloodTest + Marker alapján
                         * keresünk. Ezt a következő lépésben módosítjuk
                         * BloodTest + Marker + SampleType alapú keresésre.
                         */
                        MarkerValue markerValue =
                                markerValueRepository
                                        .findByBloodTestAndMarkerAndSampleType(
                                                bloodTest,
                                                marker,
                                                measurement.getSampleType()
                                        )
                                        .orElse(null);

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
                            "Beolvasott mérések: " +
                                    measurements.size()
                    );
                }

                System.out.println();
                System.out.println("================================");
                System.out.println("EXCEL IMPORT KÉSZ");
                System.out.println("================================");

                System.out.println(
                        "Összes beolvasott mérés: " +
                                totalMeasurements
                );

                System.out.println(
                        "Újonnan mentett mérés: " +
                                savedMeasurements
                );

                System.out.println(
                        "Athlete-ek száma: " +
                                athleteRepository.count()
                );

                System.out.println(
                        "BloodTest-ek száma: " +
                                bloodTestRepository.count()
                );

                System.out.println(
                        "Markerek száma: " +
                                markerRepository.count()
                );

                System.out.println(
                        "MarkerValue-k száma: " +
                                markerValueRepository.count()
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Hiba az Excel importálása közben:"
            );

            e.printStackTrace();
        }
    }


    private Athlete getOrCreateAthlete(
            String sheetName) {

        return athleteRepository
                .findByName(sheetName)
                .orElseGet(() -> {

                    Athlete athlete =
                            new Athlete();

                    athlete.setName(sheetName);

                    /*
                     * A sheet neve például:
                     *
                     * 2003_Male_Road
                     *
                     * vagy:
                     *
                     * 2006_Female_Road
                     */

                    String[] parts =
                            sheetName.split("_");

                    /*
                     * Születési év
                     */
                    if (parts.length > 0) {

                        try {

                            int birthYear =
                                    Integer.parseInt(
                                            parts[0]
                                    );

                            athlete.setBirthDate(
                                    LocalDate.of(
                                            birthYear,
                                            1,
                                            1
                                    )
                            );

                        } catch (Exception ignored) {
                            // Ha nincs értelmezhető év.
                        }
                    }

                    /*
                     * Nem
                     */
                    if (sheetName.toLowerCase()
                            .contains("male")) {

                        athlete.setGender(
                                Gender.MALE
                        );

                    } else if (sheetName.toLowerCase()
                            .contains("female")) {

                        athlete.setGender(
                                Gender.FEMALE
                        );
                    }

                    return athleteRepository.save(
                            athlete
                    );
                });
    }


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
                     * Az Excel jelenlegi importja
                     * még nem kezeli külön a mértékegységet.
                     */
                    marker.setUnit(null);

                    return markerRepository.save(
                            marker
                    );
                });
    }


    private List<ExcelMeasurement> readMeasurements(
            Sheet sheet) {

        List<ExcelMeasurement> measurements =
                new ArrayList<>();

        var headerRow =
                sheet.getRow(0);

        if (headerRow == null) {
            return measurements;
        }

        for (int columnIndex = 1;
             columnIndex < headerRow.getLastCellNum();
             columnIndex++) {

            var dateCell =
                    headerRow.getCell(columnIndex);

            if (dateCell == null ||
                    dateCell.toString().isBlank()) {

                continue;
            }

            LocalDate date =
                    parseDate(
                            dateCell.toString()
                    );

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

                var valueCell =
                        row.getCell(columnIndex);

                if (markerCell == null ||
                        markerCell.toString().isBlank()) {

                    continue;
                }

                if (valueCell == null ||
                        valueCell.toString().isBlank()) {

                    continue;
                }

                String markerName =
                        markerCell.toString().trim();

                String value =
                        valueCell.toString().trim();

                /*
                 * Meghatározzuk, hogy milyen típusú
                 * mintából származik az adat.
                 */
                SampleType sampleType =
                        detectSampleType(markerName);

                measurements.add(
                        new ExcelMeasurement(
                                date,
                                markerName,
                                value,
                                sampleType
                        )
                );
            }
        }

        return measurements;
    }


    private SampleType detectSampleType(
            String markerName) {

        String marker =
                markerName.trim().toLowerCase();

        if (marker.equals("urine")) {

            return SampleType.URINE;
        }

        if (marker.equals("urine sediment")) {

            return SampleType.URINE_SEDIMENT;
        }

        /*
         * Egyelőre minden más értéket
         * vérvizsgálatnak tekintünk.
         *
         * A következő lépésben ezt fogjuk
         * szekcióalapú felismerésre módosítani.
         */
        return SampleType.BLOOD;
    }


    private LocalDate parseDate(
            String dateText) {

        dateText =
                dateText.trim();

        int parenthesisIndex =
                dateText.indexOf("(");

        if (parenthesisIndex != -1) {

            dateText =
                    dateText.substring(
                            0,
                            parenthesisIndex
                    ).trim();
        }

        if (dateText.matches(
                "\\d{4}\\.\\d{2}\\.\\d{2}-\\d{2}")) {

            dateText =
                    dateText.substring(0, 10);
        }

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
                // Következő formátum.
            }
        }

        throw new IllegalArgumentException(
                "Ismeretlen dátumformátum: " +
                        dateText
        );
    }
}