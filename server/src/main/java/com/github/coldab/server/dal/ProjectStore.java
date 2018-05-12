package com.github.coldab.server.dal;

import com.github.coldab.shared.project.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectStore extends CrudRepository<Project, Integer>  {
}
