package com.github.coldab.client.rest;

import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.rest.AccountServer;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.web.client.RestTemplate;

public class RestClient implements AccountServer {

  private static final String ENDPOINT_URL = "localhost:8080";
  private RestTemplate restTemplate = new RestTemplate();
  private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());

  @Override
  public List<Project> getProjects(int accountId) {
    Project[] projects = restTemplate.getForObject(url("/account/project"), Project[].class);
    return Arrays.asList(projects);
  }

  @Override
  public boolean createProject(Project project) {
    return false;
  }

  public URI url(String path) {
    return URI.create(ENDPOINT_URL + path);
  }
}
