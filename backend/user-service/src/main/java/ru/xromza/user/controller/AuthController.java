package ru.xromza.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.xromza.user.dto.AuthRequestDto;
import ru.xromza.user.dto.AuthResult;
import ru.xromza.user.dto.AuthResultDto;
import ru.xromza.user.dto.UserRegistrationDto;
import ru.xromza.user.mapper.AuthResultMapper;
import ru.xromza.user.dto.StatusDto;
import ru.xromza.user.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/user/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthResultMapper authResultMapper;

    @PostMapping("/login")
    public ResponseEntity<AuthResultDto> login(@Valid @RequestBody AuthRequestDto request) {
        AuthResult result = authService.authenticate(request);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", result.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/user/auth/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Lax")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResultMapper.toDto(result));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResultDto> register(@Valid @RequestBody UserRegistrationDto request) {

        AuthResult result = authService.register(request);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", result.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/user/auth/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Lax")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResultMapper.toDto(result));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResultDto> refresh(@NonNull HttpServletRequest request) {
        String refreshToken = extractValue(request, "refreshToken");

        if (refreshToken == null) {
            throw new BadCredentialsException("Отсутствует токен");
        }
        AuthResult result = authService.refreshAccessToken(refreshToken);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", result.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/user/auth/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Lax")
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResultMapper.toDto(result));
    }

    @PostMapping("/logout")
    public ResponseEntity<StatusDto> logout(@NonNull HttpServletRequest request) {
        String refreshToken = extractValue(request, "refreshToken");

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/api/v1/user/refresh")
                .maxAge(0)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authService.logout(refreshToken));
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verifyLogin() {
        return ResponseEntity.noContent().build();
    }

    private String extractValue(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        String res = null;

        if (cookies != null) {
            res = Arrays
                    .stream(cookies)
                    .filter(cookie -> key.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        return res;
    }

}
