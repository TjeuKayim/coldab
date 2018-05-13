package com.github.coldab.server;

import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.shared.project.Project;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan("com.github.coldab")
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Bean
  public CommandLineRunner demo(ProjectStore projectStore) {
    return args -> {
      projectStore.save(new Project("TestProject"));
    };
  }
}
