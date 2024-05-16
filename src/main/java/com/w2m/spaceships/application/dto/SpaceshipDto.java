package com.w2m.spaceships.application.dto;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class SpaceshipDto {

  private Long id;
  private String name;
  private String seriesOrMovie;
  private String shipType;
  private Integer capacity;
  private String features;
  private OffsetDateTime updatedAt;
}
