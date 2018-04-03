package com.github.coldab.client.gui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.github.coldab.client")
public class AppConfig {
  /**
   * Test dependency injection.
   * @return a Greeter implementation
   */
  @Bean
  public Greeter greeter() {
    return () -> "My greeter implementation";
  }
}
