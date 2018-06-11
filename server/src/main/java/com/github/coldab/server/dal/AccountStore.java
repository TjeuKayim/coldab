package com.github.coldab.server.dal;

import com.github.coldab.shared.account.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountStore extends CrudRepository<Account, Integer> {

  /**
   * Finds the account associated with the given emailadress.
   *
   * @return the account associated with the given emailadress. If no account is found, this methode
   * returns NULL.
   */
  Account findAccountByEmail(String email);

  /**
   * Finds the account associated with the given nickName.
   *
   * @return the account associated with the given nickName. If no account is found, this methode
   * returns NULL.
   */
  Account findAccountBynickName(String nickName);


}
