package com.w2m.spaceships.infrastructure.output.port.repository;

import com.w2m.spaceships.domain.model.Spaceship;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceshipRepository extends JpaRepository<Spaceship, Long> {

  List<Spaceship> findByNameContaining(String name);
}
