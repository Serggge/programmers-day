package ru.practicum.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.gateway.dto.RegisterDto;

@Component
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class DtoMapperImpl implements DtoMapper {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public RegisterDto mapToDto(String json) {
        return objectMapper.readValue(json, RegisterDto.class);
    }

    @Override
    @SneakyThrows
    public String mapToJson(RegisterDto dto) {
        return objectMapper.writeValueAsString(dto);
    }

}
