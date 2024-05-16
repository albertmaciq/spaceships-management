package com.w2m.spaceships.application.mapper;

import com.w2m.spaceships.application.dto.SpaceshipDto;
import com.w2m.spaceships.domain.model.Spaceship;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SpaceshipMapper {

  SpaceshipMapper INSTANCE = Mappers.getMapper(SpaceshipMapper.class);

  SpaceshipDto toSpaceshipDto(Spaceship spaceship);

  Spaceship toSpaceship(SpaceshipDto spaceshipDto);

  List<SpaceshipDto> toSpaceshipDtoList(List<Spaceship> spaceshipList);

  default LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime) {
    return offsetDateTime == null ? null : offsetDateTime.toLocalDateTime();
  }

  default OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
    return localDateTime == null ? null : localDateTime.atZone(ZoneOffset.UTC).toOffsetDateTime();
  }
}
