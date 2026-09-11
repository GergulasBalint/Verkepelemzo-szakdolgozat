package com.szakdolgozat.Repository;

import com.szakdolgozat.Model.Athlete;
import com.szakdolgozat.Model.BloodTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BloodTestRepository
        extends JpaRepository<BloodTest, Long> {

    Optional<BloodTest> findByAthleteAndDate(
            Athlete athlete,
            LocalDate date
    );

    List<BloodTest> findByAthleteOrderByDateAsc(
            Athlete athlete
    );
}