package com.github.coldab.shared.ws;

import com.github.coldab.shared.account.Account;
import java.util.List;

public interface ProjectClient extends UpdateSender {


  void collaborators(List<Account> admins, List<Account> collaborators);
}
