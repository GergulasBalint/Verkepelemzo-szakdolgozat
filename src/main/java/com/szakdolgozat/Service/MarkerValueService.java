package com.szakdolgozat.Service;

import com.szakdolgozat.Model.MarkerValue;
import com.szakdolgozat.Repository.MarkerValueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarkerValueService {

    private final MarkerValueRepository markerValueRepository;

    public MarkerValueService(MarkerValueRepository markerValueRepository) {
        this.markerValueRepository = markerValueRepository;
    }

    public List<MarkerValue> getAllMarkerValues() {
        return markerValueRepository.findAll();
    }

    public Optional<MarkerValue> getMarkerValueById(Long id) {
        return markerValueRepository.findById(id);
    }

    public MarkerValue saveMarkerValue(MarkerValue markerValue) {
        return markerValueRepository.save(markerValue);
    }

    public MarkerValue updateMarkerValueById(Long id, MarkerValue updatedMarkerValue) {
        MarkerValue markerValue = markerValueRepository.findById(id)
                .orElseThrow();

        markerValue.setBloodTest(updatedMarkerValue.getBloodTest());
        markerValue.setMarker(updatedMarkerValue.getMarker());
        markerValue.setValue(updatedMarkerValue.getValue());

        return markerValueRepository.save(markerValue);
    }

    public void deleteMarkerValueById(Long id) {
        markerValueRepository.deleteById(id);
    }
}