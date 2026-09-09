package com.szakdolgozat.Controller;

import com.szakdolgozat.Model.Athlete;
import com.szakdolgozat.Service.AthleteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AthleteController {

    private final AthleteService athleteService;

    public AthleteController(AthleteService athleteService) {

        this.athleteService = athleteService;
    }

    @GetMapping("/athletes")
    public List<Athlete> getAllAthletes() {
        return athleteService.getAllAthletes();
    }

    @GetMapping("/athletes/{id}")
    public Optional<Athlete> getAthletesById(@PathVariable Long id) {
        return athleteService.getAthleteById(id);
    }

    @DeleteMapping("/athletes/{id}")
    public void deleteAthleteById(@PathVariable Long id) {
        athleteService.deleteAthleteById(id);
    }

    @PutMapping("/athletes/{id}")
    public Athlete updateAthleteById(
            @PathVariable Long id,
            @RequestBody Athlete athlete) {

        return athleteService.updateAthleteById(id, athlete);
    }

    @PostMapping("/athletes")
    public Athlete saveAthlete(@RequestBody Athlete athlete) {
        return athleteService.saveAthlete(athlete);
    }
}
