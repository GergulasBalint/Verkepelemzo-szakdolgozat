package com.szakdolgozat.Controller;

import com.szakdolgozat.Service.ExcelImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/import")
public class ExcelImportController {

    private final ExcelImportService excelImportService;

    public ExcelImportController(
            ExcelImportService excelImportService) {

        this.excelImportService =
                excelImportService;
    }

    /*
     * ==========================================
     * EXCEL IMPORT
     * ==========================================
     *
     * POST:
     *
     * /import/excel
     *
     * A fájl neve:
     *
     * file
     */
    @PostMapping("/excel")
    public ResponseEntity<String> importExcel(
            @RequestParam("file")
            MultipartFile file) {

        /*
         * Ellenőrizzük, hogy
         * egyáltalán érkezett-e fájl.
         */
        if (file.isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            "A feltöltött fájl üres."
                    );
        }

        /*
         * Ellenőrizzük a fájl kiterjesztését.
         */
        String fileName =
                file.getOriginalFilename();

        if (fileName == null ||
                (!fileName
                        .toLowerCase()
                        .endsWith(".xlsx") &&
                        !fileName
                                .toLowerCase()
                                .endsWith(".xls"))) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            "Csak Excel fájl tölthető fel "
                                    + "(.xlsx vagy .xls)."
                    );
        }

        try {

            /*
             * A MultipartFile InputStreamjét
             * közvetlenül átadjuk az importernek.
             *
             * Nem kell ideiglenes fájlt létrehozni.
             */
            excelImportService.importExcel(
                    file.getInputStream()
            );

            return ResponseEntity.ok(
                    "Az Excel import sikeresen lefutott."
            );

        } catch (IOException e) {

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Hiba az Excel feldolgozása közben: "
                                    + e.getMessage()
                    );
        }
    }
}