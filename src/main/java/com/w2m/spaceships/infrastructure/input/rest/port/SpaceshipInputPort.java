package com.w2m.spaceships.infrastructure.input.rest.port;

import com.w2m.spaceships.application.dto.SpaceshipDto;
import com.w2m.spaceships.infrastructure.input.rest.handler.exceptions.BadRequestSpaceshipException;
import com.w2m.spaceships.infrastructure.input.rest.handler.exceptions.NotFoundSpaceshipException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SpaceshipInputPort {

  Page<SpaceshipDto> getSpaceships(Pageable pageable);

  SpaceshipDto getSpaceshipById(Long id);

  List<SpaceshipDto> getSpaceshipByNameContaining(String name);

  void createSpaceship(SpaceshipDto spaceshipDto) throws BadRequestSpaceshipException;

  void updateSpaceship(Long id, SpaceshipDto newSpaceshipDto) throws Exception;

  void deleteSpaceship(Long id) throws NotFoundSpaceshipException;
}
