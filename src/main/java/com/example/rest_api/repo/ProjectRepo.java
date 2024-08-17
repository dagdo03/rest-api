package com.example.rest_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rest_api.model.Project;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Integer>{

}
