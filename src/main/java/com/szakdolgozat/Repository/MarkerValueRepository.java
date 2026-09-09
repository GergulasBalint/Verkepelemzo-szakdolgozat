package com.szakdolgozat.Repository;

import com.szakdolgozat.Model.BloodTest;
import com.szakdolgozat.Model.Marker;
import com.szakdolgozat.Model.MarkerValue;
import com.szakdolgozat.Model.SampleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarkerValueRepository
        extends JpaRepository<MarkerValue, Long> {

    Optional<MarkerValue>
    findByBloodTestAndMarkerAndSampleType(
            BloodTest bloodTest,
            Marker marker,
            SampleType sampleType
    );
}