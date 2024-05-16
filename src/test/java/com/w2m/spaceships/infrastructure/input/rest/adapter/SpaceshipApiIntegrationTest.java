package com.w2m.spaceships.infrastructure.input.rest.adapter;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w2m.spaceships.application.dto.SpaceshipDto;
import com.w2m.spaceships.infrastructure.input.rest.config.SecurityConfig;
import com.w2m.spaceships.infrastructure.input.rest.port.SpaceshipInputPort;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SpaceshipApi.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
public class SpaceshipApiIntegrationTest {

  @MockBean private SpaceshipInputPort spaceshipInputPort;

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockUser(roles = {"USER", "ADMIN"})
  public void getAllSpaceships() throws Exception {
    List<SpaceshipDto> spaceships = new ArrayList<>();
    Page<SpaceshipDto> spaceshipPage = new PageImpl<>(spaceships);
    when(spaceshipInputPort.getSpaceships(Pageable.unpaged())).thenReturn(spaceshipPage);

    mockMvc
        .perform(get("/api/v1/spaceships").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @WithAnonymousUser
  public void getAllSpaceships_then_return_unauthorized() throws Exception {
    List<SpaceshipDto> spaceships = new ArrayList<>();
    Page<SpaceshipDto> spaceshipPage = new PageImpl<>(spaceships);
    when(spaceshipInputPort.getSpaceships(Pageable.unpaged())).thenReturn(spaceshipPage);

    mockMvc
        .perform(get("/api/v1/spaceships").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = {"USER", "ADMIN"})
  public void getSpaceshipById() throws Exception {
    SpaceshipDto spaceshipDto = new SpaceshipDto();
    spaceshipDto.setId(1L);
    when(spaceshipInputPort.getSpaceshipById(1L)).thenReturn(spaceshipDto);

    mockMvc
        .perform(get("/api/v1/spaceships/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(spaceshipDto.getId()));
  }

  @Test
  @WithAnonymousUser
  public void getSpaceshipById_then_return_unauthorized() throws Exception {
    mockMvc
        .perform(get("/api/v1/spaceships/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = {"USER", "ADMIN"})
  public void getSpaceshipById_then_not_found() throws Exception {
    when(spaceshipInputPort.getSpaceshipById(1L)).thenReturn(null);

    mockMvc
        .perform(get("/api/v1/spaceships/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(roles = {"USER", "ADMIN"})
  public void getSpaceshipsByName() throws Exception {
    List<SpaceshipDto> spaceships = Collections.emptyList();
    when(spaceshipInputPort.getSpaceshipByNameContaining("name")).thenReturn(spaceships);

    mockMvc
        .perform(get("/api/v1/spaceships/search?name=name").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  @WithAnonymousUser
  public void getSpaceshipsByName_then_return_unauthorized() throws Exception {
    mockMvc
        .perform(get("/api/v1/spaceships/search?name=name").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = "USER")
  public void createSpaceship_then_return_forbidden() throws Exception {
    SpaceshipDto spaceshipDto = new SpaceshipDto();

    mockMvc
        .perform(
            post("/api/v1/spaceships")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spaceshipDto)))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithAnonymousUser
  public void createSpaceship_then_return_unauthorized() throws Exception {
    SpaceshipDto spaceshipDto = new SpaceshipDto();

    mockMvc
        .perform(
            post("/api/v1/spaceships")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spaceshipDto)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void createSpaceship_then_return_not_allowed() throws Exception {
    SpaceshipDto spaceshipDto = new SpaceshipDto();
    String endpoint = String.format("/api/v1/spaceships/%s", 1);

    mockMvc
        .perform(
            post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spaceshipDto)))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @WithAnonymousUser
  public void updateSpaceship_then_return_unauthorized() throws Exception {
    SpaceshipDto spaceshipDto = new SpaceshipDto();

    mockMvc
        .perform(
            put("/api/v1/spaceships/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spaceshipDto)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void updateSpaceship() throws Exception {
    SpaceshipDto spaceshipDto = new SpaceshipDto();

    mockMvc
        .perform(
            put("/api/v1/spaceships/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spaceshipDto)))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(roles = "USER")
  public void deleteSpaceship_then_return_forbidden() throws Exception {
    mockMvc
        .perform(delete("/api/v1/spaceships/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithAnonymousUser
  public void deleteSpaceship_then_return_unauthorized() throws Exception {
    mockMvc
        .perform(delete("/api/v1/spaceships/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void deleteSpaceship() throws Exception {
    mockMvc
        .perform(delete("/api/v1/spaceships/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }
}
