package com.mss.app.service.mapper;

import com.mss.app.domain.User;
import com.mss.app.service.dto.UserDTO;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "active", ignore = true)
    User toEntity(UserDTO dto);

    List<UserDTO> toDTOList(List<User> entity);
}
