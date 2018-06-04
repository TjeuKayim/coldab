package com.github.coldab.server;

import com.github.coldab.server.dal.FileStore;
import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
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
  public CommandLineRunner demo(ProjectStore projectStore, FileStore fileStore) {
    return args -> {
      Project project = new Project("TestProject");
      TextFile textFile = new TextFile(0, "index.html");
      Account piet = new Account("Piet Hein", "piet@hein.email");
      textFile.addEdit(new Addition(0, piet, null, "Hello World from database"));
      project.getAdmins().add(piet);
      project.getFiles().add(textFile);
      projectStore.save(project);
    };
  }
}
