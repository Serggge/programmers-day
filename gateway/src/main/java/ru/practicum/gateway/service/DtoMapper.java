package ru.practicum.gateway.service;

import ru.practicum.gateway.dto.RegisterDto;

public interface DtoMapper {

    RegisterDto mapToDto(String json);

    String mapToJson(RegisterDto dto);

}
