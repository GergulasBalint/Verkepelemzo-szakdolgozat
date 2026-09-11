package com.szakdolgozat.Repository;

import com.szakdolgozat.Model.Athlete;
import com.szakdolgozat.Model.BloodTest;
import com.szakdolgozat.Model.Marker;
import com.szakdolgozat.Model.MarkerValue;
import com.szakdolgozat.Model.SampleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarkerValueRepository
        extends JpaRepository<MarkerValue, Long> {

    Optional<MarkerValue> findByBloodTestAndMarkerAndSampleType(
            BloodTest bloodTest,
            Marker marker,
            SampleType sampleType
    );

    List<MarkerValue> findByBloodTest_AthleteAndMarkerAndSampleTypeOrderByBloodTest_DateAsc(
            Athlete athlete,
            Marker marker,
            SampleType sampleType
    );

    List<MarkerValue> findByBloodTest_AthleteAndSampleTypeOrderByBloodTest_DateAsc(
            Athlete athlete,
            SampleType sampleType
    );

    List<MarkerValue> findByBloodTest_AthleteAndSampleType(
            Athlete athlete,
            SampleType sampleType
    );

    List<MarkerValue> findByBloodTest_Athlete(Athlete athlete);
}