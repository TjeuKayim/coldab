package com.github.coldab.client.rest;

import com.github.coldab.client.Main;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.rest.AccountServer;
import com.github.coldab.shared.rest.Credentials;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

public class RestClient implements AccountServer {

  private RestTemplate restTemplate = new RestTemplate();
  private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());
  private String sessionId;

  public RestClient() {
    restTemplate.setErrorHandler(new ErrorHandler());
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(Main.getRestEndpoint()));
    restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
      request.getHeaders().add("Session", sessionId);
      return execution.execute(request, body);
    }));
  }

  private void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  @Override
  public List<Project> getProjects() {
    ResponseEntity<Project[]> entity = restTemplate
        .getForEntity("/account/project", Project[].class);
    return Arrays.asList(entity.getBody());
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
    setSessionId(account.getSessionId());
    return account;
  }

  @Override
  public Account login(Credentials credentials) {
    Account account = restTemplate
        .postForObject("/account/login", credentials, Account.class);
    setSessionId(account.getSessionId());
    return account;
  }

  @Override
  public void logout(String sessionId) {
    restTemplate
        .postForEntity("/account/logout", sessionId, Boolean.TYPE);
  }

  private class ErrorHandler implements ResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
      LOGGER.severe("HTTP error");
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
      return false;
    }
  }
}
