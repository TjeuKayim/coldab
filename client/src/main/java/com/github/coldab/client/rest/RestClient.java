package com.github.coldab.client.rest;

import com.github.coldab.client.Main;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.rest.AccountServer;
import com.github.coldab.shared.rest.Credentials;
import com.github.coldab.shared.ws.MessageEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

public class RestClient implements AccountServer {

  private RestTemplate restTemplate = new RestTemplate();
  private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());
  private String sessionId;

  public RestClient() {
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(Main.getRestEndpoint()));
    restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
      request.getHeaders().add("Session", getSessionId());
      return execution.execute(request, body);
    }));
    restTemplate.setMessageConverters(Collections.singletonList(
        new GsonHttpMessageConverter(MessageEncoder.getGson())
    ));
  }

  public String getSessionId() {
    LOGGER.info(() -> "Get session " + sessionId);
    return sessionId;
  }

  private void setSessionId(String sessionId) {
    this.sessionId = sessionId;
    LOGGER.info(() -> "Set session " + sessionId);
  }

  @Override
  public List<Project> getProjects() {
    Project[] projects = restTemplate
        .getForObject("/account/project", Project[].class);
    if (projects != null) {
      return Arrays.asList(projects);
    } else {
      return null;
    }
  }

  @Override
  public boolean createProject(String projectName) {
    ResponseEntity<Project> entity = restTemplate
        .postForEntity("/account/project", projectName, Project.class);
    return entity.hasBody();
  }

  @Override
  public Account register(Credentials credentials) {
    Account account = restTemplate
        .postForObject("/account/register", credentials, Account.class);
    if (account == null) {
      return null;
    }
    setSessionId(account.getSessionId());
    return account;
  }

  @Override
  public Account login(Credentials credentials) {
    Account account = restTemplate
        .postForObject("/account/login", credentials, Account.class);
    if (account == null) {
      return null;
    }
    setSessionId(account.getSessionId());
    return account;
  }

  @Override
  public void logout(String sessionId) {
    restTemplate
        .postForEntity("/account/logout", sessionId, Boolean.TYPE);
  }

  @Override
  public boolean removeProject(Project project) {
    try {
      restTemplate.delete("/account/project/{id}", project.getId());
      return true;
    } catch (HttpServerErrorException e) {
      return false;
    }
  }
}
