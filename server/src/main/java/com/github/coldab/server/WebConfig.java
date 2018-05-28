package com.github.coldab.server;

import com.github.coldab.shared.ws.MessageEncoder;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

@Configuration
public class WebConfig {

  @Bean
  public Gson gson() {
    return MessageEncoder.getGson();
  }

  @Bean
  public HttpMessageConverters customConverters() {
    GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
    gsonHttpMessageConverter.setGson(gson());
    gsonHttpMessageConverter
        .setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
    Collection<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
    messageConverters.add(gsonHttpMessageConverter);
    return new HttpMessageConverters(true, messageConverters);
  }
}