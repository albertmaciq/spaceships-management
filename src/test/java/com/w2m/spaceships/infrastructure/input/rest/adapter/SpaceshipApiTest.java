package com.w2m.spaceships.infrastructure.input.rest.adapter;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import com.w2m.spaceships.application.dto.SpaceshipDto;
import com.w2m.spaceships.infrastructure.input.rest.handler.exceptions.BadRequestSpaceshipException;
import com.w2m.spaceships.infrastructure.input.rest.handler.exceptions.NotFoundSpaceshipException;
import com.w2m.spaceships.infrastructure.input.rest.port.SpaceshipInputPort;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SpaceshipApiTest {

  @InjectMocks private SpaceshipApi spaceshipApi;

  @Mock private SpaceshipInputPort spaceshipInputPort;

  @Test
  void getAllSpaceships_returnsSpaceshipsPage_whenPageableIsProvided() {
    Pageable pageable = Pageable.unpaged();
    Page<SpaceshipDto> expectedPage = Page.empty();
    when(spaceshipInputPort.getSpaceships(pageable)).thenReturn(expectedPage);

    ResponseEntity<Page<SpaceshipDto>> response = spaceshipApi.getAllSpaceships(pageable);

    then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    then(response.getBody()).isEqualTo(expectedPage);
  }

  @Test
  void getSpaceshipById_returnsSpaceshipDto_whenValidIdIsProvided() {
    Long id = 1L;
    SpaceshipDto expectedDto = new SpaceshipDto();
    when(spaceshipInputPort.getSpaceshipById(id)).thenReturn(expectedDto);

    ResponseEntity<SpaceshipDto> response = spaceshipApi.getSpaceshipById(id);

    then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    then(response.getBody()).isEqualTo(expectedDto);
  }

  @Test
  void getSpaceshipById_returnsNotFound_whenSpaceshipDtoIsNull() {
    Long id = 1L;
    when(spaceshipInputPort.getSpaceshipById(id)).thenReturn(null);

    ResponseEntity<SpaceshipDto> response = spaceshipApi.getSpaceshipById(id);

    then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    then(response.getBody()).isNull();
  }

  @Test
  void getSpaceshipsBySubName_returnsSpaceshipDtoList_whenNameIsProvided() {
    String subName = "xyz";
    List<SpaceshipDto> expectedDtoList = List.of(new SpaceshipDto(), new SpaceshipDto());
    when(spaceshipInputPort.getSpaceshipByNameContaining(subName)).thenReturn(expectedDtoList);

    ResponseEntity<List<SpaceshipDto>> response = spaceshipApi.getSpaceshipsByName(subName);

    then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    then(response.getBody()).isEqualTo(expectedDtoList);
  }

  @Test
  void createSpaceship_returnsCreated_whenSpaceshipDtoIsProvided()
      throws BadRequestSpaceshipException {
    SpaceshipDto spaceshipDto = new SpaceshipDto();

    ResponseEntity<Void> response = spaceshipApi.createSpaceship(spaceshipDto);

    then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }

  @Test
  void createSpaceship_returnsBadRequest_whenSpaceshipDtoIsNull()
      throws BadRequestSpaceshipException {
    ResponseEntity<Void> response = spaceshipApi.createSpaceship(null);

    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void updateSpaceship_returnsNoContent_whenValidIdAndSpaceshipDtoAreProvided() throws Exception {
    Long id = 1L;
    SpaceshipDto newSpaceshipDto = new SpaceshipDto();

    ResponseEntity<Void> response = spaceshipApi.updateSpaceship(id, newSpaceshipDto);

    then(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void updateSpaceship_ReturnsBadRequest_WhenSpaceshipDtoIsNull() throws Exception {
    Long id = 1L;

    ResponseEntity<Void> response = spaceshipApi.updateSpaceship(id, null);

    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void deleteSpaceship_ReturnsNoContent_WhenValidIdIsProvided() throws NotFoundSpaceshipException {
    Long id = 1L;

    ResponseEntity<Void> response = spaceshipApi.deleteSpaceship(id);

    then(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }
}
