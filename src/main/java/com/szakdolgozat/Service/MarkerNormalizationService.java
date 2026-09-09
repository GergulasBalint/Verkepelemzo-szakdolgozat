package com.szakdolgozat.Service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MarkerNormalizationService {

    private final Map<String, String> markerNames = new HashMap<>();

    public MarkerNormalizationService() {

        // Hemoglobin
        markerNames.put("hemoglobin", "Hemoglobin");
        markerNames.put("haemoglobin", "Hemoglobin");
        markerNames.put("hgb", "Hemoglobin");

        // Vörösvérsejtszám
        markerNames.put("red blood cells", "Red blood cells");
        markerNames.put("red blood cell count", "Red blood cells");
        markerNames.put("rbc", "Red blood cells");

        // Fehérvérsejtszám
        markerNames.put("white blood cells", "White blood cells");
        markerNames.put("white blood cell count", "White blood cells");
        markerNames.put("wbc", "White blood cells");

        // Hematokrit
        markerNames.put("hematocrit", "Hematocrit");
        markerNames.put("haematocrit", "Hematocrit");
        markerNames.put("hct", "Hematocrit");

        // Vérlemezkék
        markerNames.put("platelet count", "Platelet count");
        markerNames.put("platelets", "Platelet count");
        markerNames.put("plt", "Platelet count");

        // Kreatinin
        markerNames.put("creatinine", "Creatinine");

        // Karbamid
        markerNames.put("urea", "Urea");

        // Ferritin
        markerNames.put("ferritin", "Ferritin");

        // Vas
        markerNames.put("iron", "Iron");

        // C-reaktív protein
        markerNames.put("crp", "CRP");
        markerNames.put("c-reactive protein", "CRP");

        // Kreatin-kináz
        markerNames.put("ck", "CK");
        markerNames.put("creatine kinase", "CK");

        // Glükóz
        markerNames.put("glucose", "Glucose");

        // Nátrium
        markerNames.put("sodium", "Sodium");
        markerNames.put("na", "Sodium");

        // Kálium
        markerNames.put("potassium", "Potassium");
        markerNames.put("k", "Potassium");

        // Klorid
        markerNames.put("chloride", "Chloride");
        markerNames.put("cl", "Chloride");

        // Kalcium
        markerNames.put("calcium", "Calcium");
        markerNames.put("ca", "Calcium");

        // Magnézium
        markerNames.put("magnesium", "Magnesium");
        markerNames.put("mg", "Magnesium");

        // TSH
        markerNames.put("tsh", "TSH");

        // D-vitamin
        markerNames.put("vitamin d", "Vitamin D");
        markerNames.put("vit d", "Vitamin D");

        // AST
        markerNames.put("ast", "AST");
        markerNames.put("asat", "AST");

        // Gamma-GT
        markerNames.put("gamma gt", "Gamma GT");
        markerNames.put("gamma-gt", "Gamma GT");
        markerNames.put("ggt", "Gamma GT");

        // eGFR
        markerNames.put("egfr", "eGFR");
        markerNames.put("egfr-epi", "eGFR-EPI");

        // Immunoglobulinok
        markerNames.put("igg", "IgG");
        markerNames.put("iga", "IgA");
        markerNames.put("igm", "IgM");

        // Neutrofilek
        markerNames.put("neutrophils", "Neutrophils");
        markerNames.put("neutrophils #", "Neutrophils #");
        markerNames.put("neutrophils %", "Neutrophils %");

        // Limfociták
        markerNames.put("lymphocytes", "Lymphocytes");
        markerNames.put("lymphocytes #", "Lymphocytes #");
        markerNames.put("lymphocytes %", "Lymphocytes %");
    }

    public String normalize(String markerName) {

        if (markerName == null) {
            return null;
        }

        String cleanedName =
                markerName.trim().toLowerCase();

        return markerNames.getOrDefault(
                cleanedName,
                markerName.trim()
        );
    }
}