package com.github.coldab.client.gui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
  @Bean
  public MainController mainController() {
    return new MainController();
  }

  @Bean
  public Greeter greeter() {
    return new Greeter() {
      @Override
      public String sayHello() {
        return "My greeter implementation";
      }
    };
  }
}
