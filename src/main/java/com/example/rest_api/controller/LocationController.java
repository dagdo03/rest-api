package com.example.rest_api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.rest_api.model.Location;
import com.example.rest_api.repo.LocationRepo;


@RestController
public class LocationController {

    @Autowired
    private LocationRepo locationRepo;

    @GetMapping("/getAllLocations")
    public ResponseEntity<List<Location>> getAllLocation(){
        try {
            List<Location> locationList = new ArrayList<>();
            locationRepo.findAll().forEach(locationList::add);
            if(locationList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(locationList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/location/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Integer id) {
        Optional<Location> locationData = locationRepo.findById(id);
        return locationData.map(location -> new ResponseEntity<>(location, HttpStatus.OK))
                          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/addLocation")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Location> addLocation(@RequestBody Location location){
        try {
            Location locationObj = locationRepo.save(location);
            return new ResponseEntity<>(locationObj, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/editLocation/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Location> editLocationById(@PathVariable Integer id, @RequestBody Location newLocationData) {
        Optional<Location> oldLocationDataOpt = locationRepo.findById(id);

        if (oldLocationDataOpt.isPresent()) {
            Location oldLocationData = oldLocationDataOpt.get();

            // Update fields only if they are not null
            if (newLocationData.getLocationName() != null) {
                oldLocationData.setLocationName(newLocationData.getLocationName());
            }
            if (newLocationData.getCity() != null) {
                oldLocationData.setCity(newLocationData.getCity());;
            }
            if (newLocationData.getCountry() != null) {
                oldLocationData.setCountry(newLocationData.getCountry());;
            }
            if (newLocationData.getProvince() != null) {
                oldLocationData.setProvince(newLocationData.getProvince());;
            }
            locationRepo.save(oldLocationData);
            return new ResponseEntity<>(oldLocationData, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/deleteLocation/{id}")
    public ResponseEntity<HttpStatus> deleteLocationById(@PathVariable Integer id) {
        try {
            locationRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAllLocations")
    public ResponseEntity<HttpStatus> deleteAllProjects() {
        try {
            locationRepo.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
