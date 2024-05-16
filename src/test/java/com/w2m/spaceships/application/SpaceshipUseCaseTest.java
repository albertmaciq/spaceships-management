package com.w2m.spaceships.application;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.w2m.spaceships.application.dto.SpaceshipDto;
import com.w2m.spaceships.application.mapper.SpaceshipMapper;
import com.w2m.spaceships.domain.model.Spaceship;
import com.w2m.spaceships.infrastructure.input.rest.handler.exceptions.BadRequestSpaceshipException;
import com.w2m.spaceships.infrastructure.input.rest.handler.exceptions.NotFoundSpaceshipException;
import com.w2m.spaceships.infrastructure.output.port.repository.SpaceshipRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class SpaceshipUseCaseTest {

  @InjectMocks private SpaceshipUseCase spaceshipUseCase;

  @Mock private SpaceshipRepository spaceshipRepository;

  @Test
  void getSpaceships_returnsSpaceshipsPage_whenPageableIsProvided() {
    Pageable pageable = Pageable.unpaged();
    Page<Spaceship> spaceshipPage = Page.empty();
    when(spaceshipRepository.findAll(pageable)).thenReturn(spaceshipPage);

    Page<SpaceshipDto> result = spaceshipUseCase.getSpaceships(pageable);

    then(result).isNotNull();
    then(result).isEqualTo(spaceshipPage.map(SpaceshipMapper.INSTANCE::toSpaceshipDto));
  }

  @Test
  void getSpaceshipById_returnsSpaceshipDto_whenValidIdIsProvided() {
    Long id = 1L;
    Spaceship expectedSpaceship = new Spaceship();
    when(spaceshipRepository.findById(id)).thenReturn(Optional.of(expectedSpaceship));

    SpaceshipDto result = spaceshipUseCase.getSpaceshipById(id);

    then(result).isNotNull();
    then(result).isEqualTo(SpaceshipMapper.INSTANCE.toSpaceshipDto(expectedSpaceship));
  }

  @Test
  void getSpaceshipById_returnsNull_whenInvalidIdIsProvided() {
    Long id = 1L;
    when(spaceshipRepository.findById(id)).thenReturn(Optional.empty());

    SpaceshipDto result = spaceshipUseCase.getSpaceshipById(id);

    then(result).isNull();
  }

  @Test
  void getSpaceshipBySubNameContains_ReturnsSpaceshipDtoList_whenSubNameIsProvided() {
    String subName = "abc";
    List<Spaceship> expectedSpaceships = List.of(new Spaceship(), new Spaceship());
    when(spaceshipRepository.findByNameContaining(subName)).thenReturn(expectedSpaceships);

    List<SpaceshipDto> result = spaceshipUseCase.getSpaceshipByNameContaining(subName);

    then(result).isNotNull();
    then(result).isEqualTo(SpaceshipMapper.INSTANCE.toSpaceshipDtoList(expectedSpaceships));
  }

  @Test
  void createSpaceship_throwsBadRequestSpaceshipException_whenRepositorySaveFails() {
    SpaceshipDto spaceshipDto = new SpaceshipDto();
    doThrow(RuntimeException.class).when(spaceshipRepository).save(any(Spaceship.class));

    Throwable thrown = catchThrowable(() -> spaceshipUseCase.createSpaceship(spaceshipDto));

    then(thrown).isNotNull().isInstanceOf(BadRequestSpaceshipException.class);
  }

  @Test
  void createSpaceship_callsRepositorySaveMethod_whenValidDtoIsProvided()
      throws BadRequestSpaceshipException {
    SpaceshipDto spaceshipDto = new SpaceshipDto();

    spaceshipUseCase.createSpaceship(spaceshipDto);

    verify(spaceshipRepository, atMostOnce()).save(any(Spaceship.class));
  }

  @Test
  void updateSpaceship_throwsNotFoundSpaceshipException_whenSpaceshipDoesNotExist() {
    Long id = 1L;
    SpaceshipDto newSpaceshipDto = new SpaceshipDto();
    when(spaceshipRepository.findById(id)).thenReturn(Optional.empty());

    Throwable thrown = catchThrowable(() -> spaceshipUseCase.updateSpaceship(id, newSpaceshipDto));
    then(thrown).isNotNull().isInstanceOf(NotFoundSpaceshipException.class);
    then(thrown.getMessage()).isEqualTo("The spaceship to be modified does not exist");
  }

  @Test
  void updateSpaceship_throwsBadRequestSpaceshipException_whenRepositorySaveFails() {
    Long id = 1L;
    SpaceshipDto newSpaceshipDto = new SpaceshipDto();
    when(spaceshipRepository.findById(id)).thenReturn(Optional.of(new Spaceship()));
    doThrow(RuntimeException.class).when(spaceshipRepository).save(any(Spaceship.class));

    Throwable thrown = catchThrowable(() -> spaceshipUseCase.updateSpaceship(id, newSpaceshipDto));
    then(thrown).isNotNull().isInstanceOf(BadRequestSpaceshipException.class);
    then(thrown.getMessage()).isEqualTo("Error updating spaceship");
  }

  @Test
  void updateSpaceship_callsRepositorySaveMethod_whenValidDtoIsProvided() throws Exception {
    Long id = 1L;
    SpaceshipDto newSpaceshipDto = new SpaceshipDto();
    when(spaceshipRepository.findById(id)).thenReturn(Optional.of(new Spaceship()));

    spaceshipUseCase.updateSpaceship(id, newSpaceshipDto);

    verify(spaceshipRepository, atMostOnce()).save(any(Spaceship.class));
  }

  @Test
  void deleteSpaceship_throwsNotFoundSpaceshipException_whenSpaceshipDoesNotExist() {
    Long id = 1L;
    when(spaceshipRepository.findById(id)).thenReturn(Optional.empty());

    Throwable thrown = catchThrowable(() -> spaceshipUseCase.deleteSpaceship(id));
    then(thrown).isNotNull().isInstanceOf(NotFoundSpaceshipException.class);
    then(thrown.getMessage()).isEqualTo("The spaceship to be deleted does not exist");
  }

  @Test
  void deleteSpaceship_callsRepositoryDeleteByIdMethod_whenValidIdIsProvided()
      throws NotFoundSpaceshipException {
    Long id = 1L;
    when(spaceshipRepository.findById(id)).thenReturn(Optional.of(new Spaceship()));

    spaceshipUseCase.deleteSpaceship(id);

    verify(spaceshipRepository, atMostOnce()).deleteById(id);
  }
}
