package com.github.coldab.server;

import com.github.coldab.server.dal.AccountStore;
import com.github.coldab.shared.account.Account;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AppAccountDetailsService implements UserDetailsService {

  private AccountStore accountStore;

  @Override
  public UserDetails loadUserByUsername(String nickName) throws UsernameNotFoundException {
    final Account account = accountStore.findAccountBynickName(nickName);

    if (account == null) {
      throw new UsernameNotFoundException(String.format("The nickName %s doesn't exist", nickName));
    }
    /**
     * authorities is a workaround list
     * todo find a way to fix this
     */
    final List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(""));

    return new org.springframework.security.core.userdetails.User(account.getNickName(),
        account.getPassword(), authorities);
  }
}
