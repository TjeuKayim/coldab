package com.github.coldab.server.rest;

import com.github.coldab.shared.project.Project;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account/project")
public class ProjectController {

  /**
   * Create a new project.
   * @param input contains the project name
   * @return a empty project with the current user as admin
   */
  @PostMapping
  ResponseEntity<?> create(@RequestBody Project input) {
    return ResponseEntity.noContent().build();
  }

  /**
   * Get all projects the current user collaborates on.
   */
  @GetMapping
  Collection<Project> get() {
    return null;
  }
}