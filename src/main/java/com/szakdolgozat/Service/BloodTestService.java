package com.szakdolgozat.Service;

import com.szakdolgozat.Model.BloodTest;
import com.szakdolgozat.Repository.BloodTestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodTestService {

    private final BloodTestRepository bloodTestRepository;

    public BloodTestService(BloodTestRepository bloodTestRepository) {
        this.bloodTestRepository = bloodTestRepository;
    }

    public List<BloodTest> getAllBloodTests() {
        return bloodTestRepository.findAll();
    }

    public Optional<BloodTest> getBloodTestById(Long id) {
        return bloodTestRepository.findById(id);
    }

    public BloodTest saveBloodTest(BloodTest bloodTest) {
        return bloodTestRepository.save(bloodTest);
    }

    public BloodTest updateBloodTestById(Long id, BloodTest updatedBloodTest) {
        BloodTest bloodTest = bloodTestRepository.findById(id)
                .orElseThrow();

        bloodTest.setAthlete(updatedBloodTest.getAthlete());
        bloodTest.setDate(updatedBloodTest.getDate());
        bloodTest.setLaboratoryName(updatedBloodTest.getLaboratoryName());

        return bloodTestRepository.save(bloodTest);
    }

    public void deleteBloodTestById(Long id) {
        bloodTestRepository.deleteById(id);
    }
}