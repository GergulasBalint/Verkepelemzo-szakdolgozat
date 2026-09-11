package com.szakdolgozat.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szakdolgozat.Model.Athlete;
import com.szakdolgozat.Repository.AthleteRepository;
import com.szakdolgozat.dto.MlAnomalyResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Service
public class MlAnalysisService {

    private final AthleteRepository athleteRepository;

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    // --------------------------------------------------
    // Python beállítások
    // --------------------------------------------------

    private static final String PYTHON_PATH =
            "C:\\Users\\gergu\\IdeaProjects\\szakdolgozat\\python-analysis\\.venv\\Scripts\\python.exe";

    private static final String SCRIPT_PATH =
            "C:\\Users\\gergu\\IdeaProjects\\szakdolgozat\\python-analysis\\anomaly_detection.py";

    private static final String RESULT_DIRECTORY =
            "C:\\Users\\gergu\\IdeaProjects\\szakdolgozat\\python-analysis";


    // --------------------------------------------------
    // Konstruktor
    // --------------------------------------------------

    public MlAnalysisService(
            AthleteRepository athleteRepository) {

        this.athleteRepository =
                athleteRepository;
    }


    // --------------------------------------------------
    // ML elemzés
    // --------------------------------------------------
    //
    // synchronized:
    // egyszerre csak egy Python ML folyamat futhat.
    // Ez megakadályozza, hogy két párhuzamos kérés
    // ugyanazt az eredményfájlt írja/olvassa.
    //
    // --------------------------------------------------

    public synchronized MlAnomalyResponse analyzeAthlete(
            Long athleteId) {

        // ---------------------------------------------
        // Sportoló ellenőrzése
        // ---------------------------------------------

        Athlete athlete =
                athleteRepository.findById(athleteId)
                        .orElseThrow(() ->
                                new MlAnalysisException(
                                        "A sportoló nem található: "
                                                + athleteId
                                )
                        );


        // ---------------------------------------------
        // Python és script útvonalak
        // ---------------------------------------------

        Path pythonPath =
                Path.of(PYTHON_PATH);

        Path scriptPath =
                Path.of(SCRIPT_PATH);


        // ---------------------------------------------
        // Sportolónként külön eredményfájl
        // ---------------------------------------------

        String resultFileName =
                "anomaly_results_"
                        + athleteId
                        + ".json";

        Path resultPath =
                Path.of(
                        RESULT_DIRECTORY,
                        resultFileName
                );


        // ---------------------------------------------
        // Python interpreter ellenőrzése
        // ---------------------------------------------

        if (!Files.exists(pythonPath)) {

            throw new MlAnalysisException(
                    "A Python interpreter nem található: "
                            + PYTHON_PATH
            );
        }


        // ---------------------------------------------
        // Python script ellenőrzése
        // ---------------------------------------------

        if (!Files.exists(scriptPath)) {

            throw new MlAnalysisException(
                    "Az ML Python script nem található: "
                            + SCRIPT_PATH
            );
        }


        try {

            // -----------------------------------------
            // Korábbi eredmény törlése
            // -----------------------------------------

            Files.deleteIfExists(
                    resultPath
            );


            // -----------------------------------------
            // Python folyamat indítása
            // -----------------------------------------

            ProcessBuilder processBuilder =
                    new ProcessBuilder(
                            PYTHON_PATH,
                            SCRIPT_PATH,
                            athlete.getId().toString()
                    );

            processBuilder.directory(
                    Path.of(RESULT_DIRECTORY).toFile()
            );

            processBuilder.redirectErrorStream(true);


            Process process =
                    processBuilder.start();


            // -----------------------------------------
            // Maximum 30 másodpercet várunk
            // -----------------------------------------

            boolean finished =
                    process.waitFor(
                            30,
                            TimeUnit.SECONDS
                    );


            // -----------------------------------------
            // Timeout
            // -----------------------------------------

            if (!finished) {

                process.destroyForcibly();

                throw new MlAnalysisException(
                        "A Python ML elemzés túl sokáig "
                                + "futott (30 másodperc után "
                                + "leállítva)."
                );
            }


            // -----------------------------------------
            // Python kimenetének kiolvasása
            // -----------------------------------------

            String output =
                    new String(
                            process.getInputStream()
                                    .readAllBytes(),
                            StandardCharsets.UTF_8
                    );


            int exitCode =
                    process.exitValue();


            // -----------------------------------------
            // Python hibával állt le
            // -----------------------------------------

            if (exitCode != 0) {

                throw new MlAnalysisException(
                        "A Python ML elemzés hibával "
                                + "állt le.\n"
                                + output
                );
            }


            // -----------------------------------------
            // Eredményfájl ellenőrzése
            // -----------------------------------------

            if (!Files.exists(resultPath)) {

                throw new MlAnalysisException(
                        "A Python elemzés sikeresen lefutott, "
                                + "de nem jött létre az "
                                + "eredményfájl: "
                                + resultPath
                );
            }


            // -----------------------------------------
            // JSON beolvasása
            // -----------------------------------------

            String json =
                    Files.readString(
                            resultPath,
                            StandardCharsets.UTF_8
                    );


            // -----------------------------------------
            // Üres JSON ellenőrzése
            // -----------------------------------------

            if (json.isBlank()) {

                throw new MlAnalysisException(
                        "A Python eredményfájl üres."
                );
            }


            // -----------------------------------------
            // JSON → Java DTO
            // -----------------------------------------

            return objectMapper.readValue(
                    json,
                    MlAnomalyResponse.class
            );


        } catch (MlAnalysisException e) {

            throw e;


        } catch (IOException e) {

            throw new MlAnalysisException(
                    "Hiba történt a Python ML folyamat "
                            + "végrehajtása közben.",
                    e
            );


        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new MlAnalysisException(
                    "A Python ML folyamat megszakadt.",
                    e
            );


        } catch (Exception e) {

            throw new MlAnalysisException(
                    "Ismeretlen hiba történt az ML "
                            + "elemzés során.",
                    e
            );
        }
    }
}