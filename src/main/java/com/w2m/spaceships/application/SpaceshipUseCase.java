package com.w2m.spaceships.application;

import com.w2m.spaceships.application.dto.SpaceshipDto;
import com.w2m.spaceships.application.mapper.SpaceshipMapper;
import com.w2m.spaceships.domain.model.Spaceship;
import com.w2m.spaceships.infrastructure.input.rest.handler.exceptions.BadRequestSpaceshipException;
import com.w2m.spaceships.infrastructure.input.rest.handler.exceptions.NotFoundSpaceshipException;
import com.w2m.spaceships.infrastructure.input.rest.port.SpaceshipInputPort;
import com.w2m.spaceships.infrastructure.output.port.repository.SpaceshipRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SpaceshipUseCase implements SpaceshipInputPort {

  private SpaceshipRepository spaceshipRepository;

  @Override
  @Cacheable("spaceships")
  public Page<SpaceshipDto> getSpaceships(final Pageable pageable) {
    Page<Spaceship> spaceshipPage = spaceshipRepository.findAll(pageable);
    return spaceshipPage.map(SpaceshipMapper.INSTANCE::toSpaceshipDto);
  }

  @Override
  @Cacheable(value = "spaceships", key = "#id")
  public SpaceshipDto getSpaceshipById(final Long id) {
    Optional<Spaceship> spaceship = spaceshipRepository.findById(id);
    return spaceship.map(SpaceshipMapper.INSTANCE::toSpaceshipDto).orElse(null);
  }

  @Override
  @Cacheable(value = "spaceshipsByName", key = "#name")
  public List<SpaceshipDto> getSpaceshipByNameContaining(final String name) {
    return SpaceshipMapper.INSTANCE.toSpaceshipDtoList(
        spaceshipRepository.findByNameContaining(name));
  }

  @Override
  @CacheEvict(
      value = {"spaceships", "spaceshipsByName"},
      allEntries = true)
  public void createSpaceship(final SpaceshipDto spaceshipDto) throws BadRequestSpaceshipException {
    spaceshipDto.setUpdatedAt(OffsetDateTime.now());
    try {
      spaceshipRepository.save(SpaceshipMapper.INSTANCE.toSpaceship(spaceshipDto));
    } catch (Exception ex) {
      log.error("Create Spaceship exception message: {}", ex.getMessage());
      throw new BadRequestSpaceshipException("Error creating spaceship");
    }
  }

  @Override
  @CacheEvict(
      value = {"spaceships", "spaceshipsByName"},
      allEntries = true)
  public void updateSpaceship(final Long id, final SpaceshipDto newSpaceshipDto) throws Exception {
    SpaceshipDto spaceshipDto = getSpaceshipById(id);
    if (spaceshipDto == null) {
      throw new NotFoundSpaceshipException("The spaceship to be modified does not exist");
    }

    newSpaceshipDto.setId(id);
    newSpaceshipDto.setUpdatedAt(OffsetDateTime.now());
    try {
      spaceshipRepository.save(SpaceshipMapper.INSTANCE.toSpaceship(newSpaceshipDto));
    } catch (Exception ex) {
      log.error("Update spaceship exception message: {}", ex.getMessage());
      throw new BadRequestSpaceshipException("Error updating spaceship");
    }
  }

  @Override
  @CacheEvict(
      value = {"spaceships", "spaceshipsByName"},
      allEntries = true)
  public void deleteSpaceship(final Long id) throws NotFoundSpaceshipException {
    SpaceshipDto spaceshipDto = getSpaceshipById(id);
    if (spaceshipDto == null) {
      throw new NotFoundSpaceshipException("The spaceship to be deleted does not exist");
    }
    spaceshipRepository.deleteById(id);
  }
}
