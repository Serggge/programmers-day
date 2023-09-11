package ru.practicum.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.gateway.dto.EncodedDto;
import ru.practicum.gateway.dto.RegisterDto;
import ru.practicum.gateway.service.DevDayService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor__ = {@Autowired})
public class DevDayController {

    private final DevDayService devDayService;

    @PostMapping("/dev-day/register")
    public ResponseEntity<String> send(@RequestBody @Valid RegisterDto dto,
                                       HttpServletRequest servletRequest) {
        return devDayService.sendRequest(dto, servletRequest);
    }

    @GetMapping("/dev-day/task/2")
    public ResponseEntity<String> task2(HttpServletRequest servletRequest) {
        return devDayService.sendRequestTask2(servletRequest);
    }
}
