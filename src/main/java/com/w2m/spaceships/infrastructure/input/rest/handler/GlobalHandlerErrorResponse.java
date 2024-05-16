package com.w2m.spaceships.infrastructure.input.rest.handler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GlobalHandlerErrorResponse {

  private String code;
  private String level;
  private String message;
  private String description;
}
