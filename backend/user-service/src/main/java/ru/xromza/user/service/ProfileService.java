package ru.xromza.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.xromza.user.dto.UserEditDto;
import ru.xromza.user.dto.UserResponseDto;
import ru.xromza.user.mapper.UserEditMapper;
import ru.xromza.user.mapper.UserMapper;
import ru.xromza.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserMapper userMapper;
    private final UserEditMapper userEditMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    public UserResponseDto getProfileData(UserDetails userDetails) {
        User user = userService.getApprovedUserByLogin(userDetails.getUsername());
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponseDto updateProfileData(UserDetails userDetails, UserEditDto userEditDto) {
        User user = userService.getApprovedUserByLogin(userDetails.getUsername());
        User mapped = userEditMapper.updateEntity(userEditDto, user);

        //User saved = userService.update(mapped);
        return userMapper.toResponse(mapped);

    }
}
