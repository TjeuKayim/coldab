package com.github.coldab.server.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.github.coldab.server.Main;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.rest.Credentials;
import com.github.coldab.shared.ws.MessageEncoder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "dev")
public class AccountControllerTest {

  @LocalServerPort
  private int port;

  private RestTemplate restTemplate;


  @Before
  public void setUp() throws Exception {
    restTemplate = new RestTemplate();
    restTemplate.setUriTemplateHandler(
        new DefaultUriBuilderFactory("http://localhost:" + port));
    restTemplate.getMessageConverters()
        .add(new GsonHttpMessageConverter(MessageEncoder.getGson()));
  }

  @Test
  public void login() {
    Credentials credentials = new Credentials("henk2@email", "myPass");
    restTemplate.postForObject("/account/register", credentials, Account.class);
    Account account = restTemplate
        .postForObject("/account/login", credentials, Account.class);
    assertEquals("henk2@email", account.getEmail());
    assertTrue(account.getSessionId().length() > 10);
  }

  @Test
  public void register() {
    Credentials credentials = new Credentials("henk@email", "myPass");
    Account account = restTemplate
        .postForObject("/account/register", credentials, Account.class);
    assertEquals("henk@email", account.getEmail());
    assertTrue(account.getSessionId().length() > 10);
  }

  @Test
  public void loginInvalid() {
    Credentials credentials = new Credentials("invalid", "wrong");
    Account account = restTemplate
        .postForObject("/account/login", credentials, Account.class);
    assertNull(account);
  }
}