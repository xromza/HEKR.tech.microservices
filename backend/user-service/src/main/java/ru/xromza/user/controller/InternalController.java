package ru.xromza.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.user.service.UserService;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/internal/user")
@RequiredArgsConstructor
@Slf4j
public class InternalController {
    private final UserService userService;

    @GetMapping("/name")
    public ResponseEntity<Map<Long, String>> getUsernames(@RequestParam List<Long> ids) {
        log.info("Получен запрос на имена пользователей под id = {}", ids.toString());
        return ResponseEntity.ok(userService.findLoginsByUserIds(ids));
    }

}
