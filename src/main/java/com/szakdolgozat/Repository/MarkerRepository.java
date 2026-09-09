package com.szakdolgozat.Repository;

import com.szakdolgozat.Model.Marker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarkerRepository extends JpaRepository<Marker, Long> {

    Optional<Marker> findByName(String name);
}