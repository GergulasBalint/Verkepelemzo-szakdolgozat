package com.szakdolgozat.Service;

import com.szakdolgozat.Model.Athlete;
import com.szakdolgozat.Repository.AthleteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class AthleteService {

    private final AthleteRepository athleteRepository;

    public AthleteService(AthleteRepository athleteRepository) {
        // itt kell eltárolnod
        this.athleteRepository = athleteRepository;
    }

    public List<Athlete> getAllAthletes() {
        // itt valamit vissza kell adni
        return athleteRepository.findAll();
    }

    public Optional<Athlete> getAthleteById(Long id) {
        return athleteRepository.findById(id);
    }

    public Athlete saveAthlete(Athlete athlete) {
        return athleteRepository.save(athlete);
    }

    public void deleteAthleteById(Long id) {
        athleteRepository.deleteById(id);
    }

    public Athlete updateAthleteById(Long id, Athlete updatedAthlete) {
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow();

        athlete.setName(updatedAthlete.getName());
        athlete.setBirthDate(updatedAthlete.getBirthDate());
        athlete.setGender(updatedAthlete.getGender());

        return athleteRepository.save(athlete);
    }
    }

