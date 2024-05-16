package com.w2m.spaceships.infrastructure.input.rest.adapter;

import com.w2m.spaceships.application.dto.SpaceshipDto;
import com.w2m.spaceships.infrastructure.input.rest.handler.exceptions.BadRequestSpaceshipException;
import com.w2m.spaceships.infrastructure.input.rest.handler.exceptions.NotFoundSpaceshipException;
import com.w2m.spaceships.infrastructure.input.rest.port.SpaceshipInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/spaceships")
public class SpaceshipApi {

  private SpaceshipInputPort spaceShipsInputPort;

  @Operation(
      summary = "Get all spaceships",
      description = "Get a list of all spaceships with pagination")
  @GetMapping
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  ResponseEntity<Page<SpaceshipDto>> getAllSpaceships(
      @Parameter(hidden = true) final Pageable pageable) {
    Page<SpaceshipDto> spaceshipDtoS = spaceShipsInputPort.getSpaceships(pageable);
    return ResponseEntity.ok(spaceshipDtoS);
  }

  @Operation(
      summary = "Get a spaceship by ID",
      description = "Get a spaceship by its unique identifier")
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  ResponseEntity<SpaceshipDto> getSpaceshipById(@PathVariable("id") final Long id) {
    SpaceshipDto spaceshipDto = spaceShipsInputPort.getSpaceshipById(id);
    return (spaceshipDto != null)
        ? ResponseEntity.ok(spaceshipDto)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @Operation(
      summary = "Search spaceships by name",
      description = "Search for spaceships that contain a specified substring in their name")
  @GetMapping("/search")
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  ResponseEntity<List<SpaceshipDto>> getSpaceshipsByName(
      @RequestParam(value = "name") final String name) {
    return ResponseEntity.ok(spaceShipsInputPort.getSpaceshipByNameContaining(name));
  }

  @Operation(summary = "Create a new spaceship", description = "Create a new spaceship")
  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN')")
  ResponseEntity<Void> createSpaceship(@RequestBody final SpaceshipDto spaceshipDto)
      throws BadRequestSpaceshipException {
    if (spaceshipDto == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    spaceShipsInputPort.createSpaceship(spaceshipDto);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Operation(summary = "Update a spaceship", description = "Update an existing spaceship")
  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  ResponseEntity<Void> updateSpaceship(
      @PathVariable final Long id, @RequestBody final SpaceshipDto newSpaceshipDto)
      throws Exception {
    if (newSpaceshipDto == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    spaceShipsInputPort.updateSpaceship(id, newSpaceshipDto);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(
      summary = "Delete a spaceship",
      description = "Delete a spaceship by its unique identifier")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  ResponseEntity<Void> deleteSpaceship(@PathVariable("id") final Long id)
      throws NotFoundSpaceshipException {
    spaceShipsInputPort.deleteSpaceship(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
