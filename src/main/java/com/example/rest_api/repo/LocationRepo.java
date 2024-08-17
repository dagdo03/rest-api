package com.example.rest_api.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rest_api.model.Location;

@Repository
public interface LocationRepo extends JpaRepository<Location, Integer> {
    Optional<Location> findByLocationName(String locationName);
}
