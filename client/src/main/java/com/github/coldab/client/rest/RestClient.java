package com.github.coldab.client.rest;

import com.github.coldab.client.Main;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.rest.AccountServer;
import com.github.coldab.shared.rest.Credentials;
import com.github.coldab.shared.ws.MessageEncoder;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

public class RestClient implements AccountServer {

  private RestTemplate restTemplate = new RestTemplate();
  private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());

  public RestClient() {
    restTemplate.setErrorHandler(new ErrorHandler());
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(Main.getRestEndpoint()));
    restTemplate.getMessageConverters().stream()
        .filter(c -> c instanceof GsonHttpMessageConverter)
        .map(c -> (GsonHttpMessageConverter) c)
        .findAny().orElseThrow(IllegalStateException::new)
        .setGson(MessageEncoder.getGson());
  }

  @Override
  public List<Project> getProjects() {
    ResponseEntity<Project[]> entity = restTemplate
        .getForEntity("/account/project", Project[].class);
    return Arrays.asList(entity.getBody());
  }

  @Override
  public boolean createProject(Project project) {
    ResponseEntity<Project> entity = restTemplate
        .postForEntity("/account/project", project, Project.class);
    return entity.hasBody();
  }

  @Override
  public Account register(Credentials credentials) {
    return restTemplate
        .postForObject("/account/register", credentials, Account.class);
  }

  @Override
  public Account login(Credentials credentials) {
    return restTemplate
        .postForObject("/account/login", credentials, Account.class);
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
