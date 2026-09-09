package com.szakdolgozat.Controller;

import com.szakdolgozat.Model.MarkerValue;
import com.szakdolgozat.Service.MarkerValueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MarkerValueController {

    private final MarkerValueService markerValueService;

    public MarkerValueController(MarkerValueService markerValueService) {
        this.markerValueService = markerValueService;
    }

    @GetMapping("/markervalues")
    public List<MarkerValue> getAllMarkerValues() {
        return markerValueService.getAllMarkerValues();
    }

    @GetMapping("/markervalues/{id}")
    public Optional<MarkerValue> getMarkerValueById(@PathVariable Long id) {
        return markerValueService.getMarkerValueById(id);
    }

    @PostMapping("/markervalues")
    public MarkerValue saveMarkerValue(@RequestBody MarkerValue markerValue) {
        return markerValueService.saveMarkerValue(markerValue);
    }

    @PutMapping("/markervalues/{id}")
    public MarkerValue updateMarkerValueById(
            @PathVariable Long id,
            @RequestBody MarkerValue markerValue) {

        return markerValueService.updateMarkerValueById(id, markerValue);
    }

    @DeleteMapping("/markervalues/{id}")
    public void deleteMarkerValueById(@PathVariable Long id) {
        markerValueService.deleteMarkerValueById(id);
    }
}