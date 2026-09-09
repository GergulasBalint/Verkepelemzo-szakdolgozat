package com.szakdolgozat.Controller;

import com.szakdolgozat.Model.BloodTest;
import com.szakdolgozat.Service.BloodTestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BloodTestController {

    private final BloodTestService bloodTestService;

    public BloodTestController(BloodTestService bloodTestService) {
        this.bloodTestService = bloodTestService;
    }

    @GetMapping("/bloodtests")
    public List<BloodTest> getAllBloodTests() {
        return bloodTestService.getAllBloodTests();
    }

    @GetMapping("/bloodtests/{id}")
    public Optional<BloodTest> getBloodTestById(@PathVariable Long id) {
        return bloodTestService.getBloodTestById(id);
    }

    @PostMapping("/bloodtests")
    public BloodTest saveBloodTest(@RequestBody BloodTest bloodTest) {
        return bloodTestService.saveBloodTest(bloodTest);
    }

    @PutMapping("/bloodtests/{id}")
    public BloodTest updateBloodTestById(
            @PathVariable Long id,
            @RequestBody BloodTest bloodTest) {

        return bloodTestService.updateBloodTestById(id, bloodTest);
    }

    @DeleteMapping("/bloodtests/{id}")
    public void deleteBloodTestById(@PathVariable Long id) {
        bloodTestService.deleteBloodTestById(id);
    }
}