package com.github.coldab.server;

import com.github.coldab.server.dal.FileStore;
import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.shared.project.Project;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = { JacksonAutoConfiguration.class })
@EntityScan("com.github.coldab")
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Bean
  public CommandLineRunner demo(ProjectStore projectStore, FileStore fileStore) {
    return args -> {
      Project project = new Project("TestProject");
      projectStore.save(project);
    };
  }
}
