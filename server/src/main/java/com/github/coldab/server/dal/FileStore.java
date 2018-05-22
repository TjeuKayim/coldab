package com.github.coldab.server.dal;

import com.github.coldab.shared.project.File;
import org.springframework.data.repository.CrudRepository;

public interface FileStore extends CrudRepository<File, Integer> {
}
