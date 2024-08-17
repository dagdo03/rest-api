package com.example.rest_api.controller;

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
import com.example.rest_api.model.Project;
import com.example.rest_api.repo.LocationRepo;
import com.example.rest_api.repo.ProjectRepo;

@RestController
public class ProjectController {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private LocationRepo locationRepo;

    @GetMapping("/getAllProjects")
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            List<Project> projectList = projectRepo.findAll();
            if (projectList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projectList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Integer id) {
        Optional<Project> projectData = projectRepo.findById(id);
        return projectData.map(project -> new ResponseEntity<>(project, HttpStatus.OK))
                          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/addProject")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        try {
          
            if (project.getLocations() != null && !project.getLocations().isEmpty()) {
                Optional<Location> locationData = locationRepo.findById(project.getLocations().iterator().next().getId());
                locationData.ifPresent(loc -> {
                    project.getLocations().clear();
                    project.getLocations().add(loc);
                });
            }

            // Save the project with associated location
            Project savedProject = projectRepo.save(project);
            return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateProject/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Project> updateProjectById(@PathVariable Integer id, @RequestBody Project newProjectData) {
        Optional<Project> oldProjectDataOpt = projectRepo.findById(id);

        if (oldProjectDataOpt.isPresent()) {
            Project oldProjectData = oldProjectDataOpt.get();

            // Update fields only if they are not null in newProjectData
            if (newProjectData.getName() != null) {
                oldProjectData.setName(newProjectData.getName());
            }
            if (newProjectData.getClient() != null) {
                oldProjectData.setClient(newProjectData.getClient());
            }
            if (newProjectData.getEndTime() != null) {
                oldProjectData.setEndTime(newProjectData.getEndTime());
            }
            if (newProjectData.getNote() != null) {
                oldProjectData.setNote(newProjectData.getNote());
            }
            if (newProjectData.getProjectLead() != null) {
                oldProjectData.setProjectLead(newProjectData.getProjectLead());
            }
            if (newProjectData.getStartTime() != null) {
                oldProjectData.setStartTime(newProjectData.getStartTime());
            }

            // Update the location if provided
            if (newProjectData.getLocations() != null && !newProjectData.getLocations().isEmpty()) {
                Optional<Location> locationData = locationRepo.findById(newProjectData.getLocations().iterator().next().getId());
                locationData.ifPresent(loc -> {
                    oldProjectData.getLocations().clear(); 
                    oldProjectData.getLocations().add(loc);
                });
            }

            projectRepo.save(oldProjectData);
            return new ResponseEntity<>(oldProjectData, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteProject/{id}")
    public ResponseEntity<HttpStatus> deleteProjectById(@PathVariable Integer id) {
        try {
            projectRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAllProject")
    public ResponseEntity<HttpStatus> deleteAllProjects() {
        try {
            projectRepo.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
