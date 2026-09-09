package com.szakdolgozat.Controller;

import com.szakdolgozat.Model.Marker;
import com.szakdolgozat.Service.MarkerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MarkerController {

    private final MarkerService markerService;

    public MarkerController(MarkerService markerService) {
        this.markerService = markerService;
    }

    @GetMapping("/markers")
    public List<Marker> getAllMarkers() {
        return markerService.getAllMarkers();
    }

    @GetMapping("/markers/{id}")
    public Optional<Marker> getMarkerById(@PathVariable Long id) {
        return markerService.getMarkerById(id);
    }

    @PostMapping("/markers")
    public Marker saveMarker(@RequestBody Marker marker) {
        return markerService.saveMarker(marker);
    }

    @PutMapping("/markers/{id}")
    public Marker updateMarkerById(
            @PathVariable Long id,
            @RequestBody Marker marker) {

        return markerService.updateMarkerById(id, marker);
    }

    @DeleteMapping("/markers/{id}")
    public void deleteMarkerById(@PathVariable Long id) {
        markerService.deleteMarkerById(id);
    }
}