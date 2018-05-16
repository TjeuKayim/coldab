package com.github.coldab.shared;

import java.time.Clock;
import java.time.LocalDateTime;

public abstract class TimeProvider {
  private static TimeProvider instance = new TimeProvider() {
    @Override
    public LocalDateTime now() {
      return LocalDateTime.now(Clock.systemUTC());
    }
  };

  public static TimeProvider getInstance() {
    return instance;
  }

  public static void setInstance(TimeProvider timeProvider) {
    TimeProvider.instance = timeProvider;
  }

  public abstract LocalDateTime now();
}
