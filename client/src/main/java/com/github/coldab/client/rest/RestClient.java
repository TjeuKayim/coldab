package com.github.coldab.client.rest;

import com.github.coldab.client.Main;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.rest.AccountServer;
import com.github.coldab.shared.ws.MessageEncoder;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public class RestClient implements AccountServer {

  private RestTemplate restTemplate = new RestTemplate();
  private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());

  public RestClient() {
    restTemplate.setErrorHandler(new ErrorHandler());
    restTemplate.getMessageConverters().stream()
        .filter(c -> c instanceof GsonHttpMessageConverter)
        .map(c -> (GsonHttpMessageConverter) c)
        .findAny().orElseThrow(IllegalStateException::new)
        .setGson(MessageEncoder.getGson());
  }

  @Override
  public List<Project> getProjects() {
    ResponseEntity<Project[]> entity = restTemplate
        .getForEntity(url("/account/project"), Project[].class);
    return Arrays.asList(entity.getBody());
//    ResponseEntity<String> entity = restTemplate
//        .getForEntity(url("/account/project"), String.class);
  }

  @Override
  public boolean createProject(Project project) {
    return false;
  }

  @Override
  public Account register(String email, String password) {
    return null;
  }

  @Override
  public Account login(String email, String password) {
    return null;
  }

  @Override
  public void logout(String sessionId) {

  }

  public URI url(String path) {
    return URI.create(Main.getRestEndpoint() + path);
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
