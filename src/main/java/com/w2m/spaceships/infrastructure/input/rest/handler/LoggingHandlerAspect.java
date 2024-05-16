package com.w2m.spaceships.infrastructure.input.rest.handler;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingHandlerAspect {

  @Before(
      "execution(* com.w2m.spaceships.application.SpaceshipUseCase.getSpaceshipById(..)) && args(id)")
  public void logBeforeFindById(final Long id) {
    if (id < 0) {
      log.warn("A spaceship with negative id: {} has been requested", id);
    }
  }
}
