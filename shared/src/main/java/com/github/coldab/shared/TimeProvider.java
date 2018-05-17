package com.github.coldab.shared;

import java.time.Clock;
import java.time.LocalDateTime;

public abstract class TimeProvider {

  public abstract LocalDateTime now();

  private static TimeProvider instance = new TimeProvider() {
    @Override
    public LocalDateTime now() {
      return LocalDateTime.now(Clock.systemUTC());
    }
  };

  public static TimeProvider getInstance() {
    return instance;
  }

  private static final LocalDateTime FIXED_TIME = instance.now();

  public static void useMock() {
    instance = new TimeProvider() {
      @Override
      public LocalDateTime now() {
        return FIXED_TIME;
      }
    };
  }
}
