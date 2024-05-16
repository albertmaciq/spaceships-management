package com.w2m.spaceships.infrastructure.input.rest.handler.exceptions;

import java.sql.SQLException;

public class NotFoundSpaceshipException extends SQLException {

  public NotFoundSpaceshipException(final String errorMessage) {
    super(errorMessage);
  }
}
