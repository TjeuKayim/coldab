package com.github.coldab.server.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestServer {

  @RequestMapping("/")
  String home() {
    return "Hello World!";
  }
}
