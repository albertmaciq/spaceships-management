package com.w2m.spaceships.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Table(name = "spaceships")
public class Spaceship {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "_id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "series_movie")
  private String seriesOrMovie;

  @Column(name = "ship_type")
  private String shipType;

  @Column(name = "capacity")
  private Integer capacity;

  @Column(name = "features")
  private String features;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
