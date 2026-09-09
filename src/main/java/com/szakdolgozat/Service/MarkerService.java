package com.szakdolgozat.Service;

import com.szakdolgozat.Model.Marker;
import com.szakdolgozat.Repository.MarkerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarkerService {

    private final MarkerRepository markerRepository;

    public MarkerService(MarkerRepository markerRepository) {
        this.markerRepository = markerRepository;
    }

    public List<Marker> getAllMarkers() {
        return markerRepository.findAll();
    }

    public Optional<Marker> getMarkerById(Long id) {
        return markerRepository.findById(id);
    }

    public Marker saveMarker(Marker marker) {
        return markerRepository.save(marker);
    }

    public Marker updateMarkerById(Long id, Marker updatedMarker) {
        Marker marker = markerRepository.findById(id)
                .orElseThrow();

        marker.setName(updatedMarker.getName());
        marker.setUnit(updatedMarker.getUnit());

        return markerRepository.save(marker);
    }

    public void deleteMarkerById(Long id) {
        markerRepository.deleteById(id);
    }
}