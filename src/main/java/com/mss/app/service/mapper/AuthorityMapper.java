package com.mss.app.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import com.mss.app.domain.Authority;
import com.mss.app.service.dto.AuthorityDTO;

@Mapper(componentModel = "spring")
public interface AuthorityMapper extends EntityMapper<AuthorityDTO, Authority> {
    AuthorityDTO toDto(Authority authority);

    Authority toEntity(AuthorityDTO authority);

    List<AuthorityDTO> toDto(List<Authority> entityList);

    List<Authority> toEntity(List<AuthorityDTO> dtoList);

}