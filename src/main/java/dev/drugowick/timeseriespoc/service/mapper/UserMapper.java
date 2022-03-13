package dev.drugowick.timeseriespoc.service.mapper;

import dev.drugowick.timeseriespoc.domain.entity.User;
import dev.drugowick.timeseriespoc.service.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    User toEntity(UserDTO userDto);

}
