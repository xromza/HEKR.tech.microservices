package ru.xromza.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.xromza.user.dto.StatusDto;
import ru.xromza.user.dto.UpdatePasswordDto;
import ru.xromza.user.dto.UserEditDto;
import ru.xromza.user.dto.UserResponseDto;
import ru.xromza.user.service.AuthService;
import ru.xromza.user.service.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/user/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<UserResponseDto> getAccountDetails(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(profileService.getProfileData(userDetails));
    }

    @PatchMapping
    public ResponseEntity<UserResponseDto> updateAccountDetails(@AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserEditDto userEditDto) {
        return ResponseEntity.ok(profileService.updateProfileData(userDetails, userEditDto));
    }

    @PostMapping("/password")
    public ResponseEntity<StatusDto> updatePassword(@AuthenticationPrincipal UserDetails userDetails,
             @RequestBody UpdatePasswordDto dto) {
        return ResponseEntity.ok(authService.changePassword(userDetails, dto.getPassword()));
    }
}
