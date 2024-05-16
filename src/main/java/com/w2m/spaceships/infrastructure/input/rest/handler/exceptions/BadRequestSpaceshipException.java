package com.w2m.spaceships.infrastructure.input.rest.handler.exceptions;

import java.sql.SQLException;

public class BadRequestSpaceshipException extends SQLException {

  public BadRequestSpaceshipException(final String errorMessage) {
    super(errorMessage);
  }
}
