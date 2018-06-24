package com.github.coldab.server.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.github.coldab.server.dal.AccountStore;
import com.github.coldab.server.dal.FileStore;
import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.ws.ChatClient;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.ServerEndpoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ConnectionManagerTest {

  @Autowired
  private ProjectStore projectStore;
  @Autowired
  private FileStore fileStore;
  @Autowired
  private AccountStore accountStore;
  private ConnectionManager connectionManager;
  private Project project;

  @Before
  public void setUp() throws Exception {
    project = projectStore.save(new Project("MyProject"));
    connectionManager = new ConnectionManager(projectStore, fileStore, accountStore);
  }

  @Test
  public void is_no_member() {
    // If account isn't a member, it shouldn't be allowed to connect
    Account account = new Account("noMember", "noMember", "noMember");
    Project result = connectionManager.getProject(this.project.getId(), account);
    assertNull(result);
  }

  @Test
  public void project_doesnt_exist() {
    Account account = new Account("noMember", "noMember", "noMember");
    Project result = connectionManager.getProject(12345, account);
    assertNull(result);
  }

  @Test
  public void valid_account() {
    Account john = new AccountBuilder("John").makeAdminOf(project).build();
    Project result = connectionManager.getProject(this.project.getId(), john);
    assertEquals(project, result);
  }

  @Test
  public void connect_and_message() {
    Account john = new AccountBuilder("John").makeAdminOf(project).build();
    Connection connection = new Connection(john);
    ChatMessage chatMessage = new ChatMessage("Hello World", john);
    connection.server.chat().message(chatMessage);
    verify(connection.chat()).message(chatMessage);
  }

  @Test
  public void share_project() {
    // Bob shares the project with alice
    Account bob = new AccountBuilder("John").makeAdminOf(project).build();
    Account alice = new AccountBuilder("Alice").build();
    Connection bobCon = new Connection(bob);
    // Bob shares the project with alice
    bobCon.server.project().share(alice.getEmail(), true);
    Connection aliceCon = new Connection(alice);
  }

  private class Connection implements ClientEndpoint {

    final ServerEndpoint server;
    final ProjectClient projectClientMock = mock(ProjectClient.class);
    final ChatClient chatClientMock = mock(ChatClient.class);

    Connection(Account account) {
      Project connectionProject = connectionManager.getProject(project.getId(), account);
      assertNotNull(connectionProject);
      server = connectionManager.connect(connectionProject, account, this);
    }

    @Override
    public ProjectClient project() {
      return projectClientMock;
    }

    @Override
    public ChatClient chat() {
      return chatClientMock;
    }
  }

  private class AccountBuilder {

    final Account account;

    AccountBuilder(String name) {
      this.account = accountStore.save(new Account(name, name, name));
    }

    AccountBuilder makeAdminOf(Project project) {
      project.getAdmins().add(account);
      projectStore.save(project);
      return this;
    }

    Account build() {
      return account;
    }
  }
}