package ru.practicum.gateway.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.gateway.dto.EncodedDto;
import ru.practicum.gateway.dto.RegisterDto;
import javax.servlet.http.HttpServletRequest;

public interface DevDayService {

    ResponseEntity<String> sendRequest(RegisterDto dto, HttpServletRequest servletRequest);

    ResponseEntity<String> sendRequestTask2(HttpServletRequest servletRequest);
}
