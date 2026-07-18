package ru.xromza.user.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.xromza.user.dto.AuthRequestDto;
import ru.xromza.user.dto.AuthResult;
import ru.xromza.user.dto.UserRegistrationDto;
import ru.xromza.user.dto.IndividualDetailsRequestDto;
import ru.xromza.user.dto.LegalDetailsRequestDto;
import ru.xromza.user.dto.StatusDto;
import ru.xromza.user.exceptions.AuthException;
import ru.xromza.user.exceptions.NewPasswordMatchesOldException;
import ru.xromza.user.interfaces.DetailsRequestInterface;
import ru.xromza.user.mapper.IndividualDetailsRequestMapper;
import ru.xromza.user.mapper.LegalDetailsRequestMapper;
import ru.xromza.user.model.IndividualDetails;
import ru.xromza.user.model.LegalDetails;
import ru.xromza.user.model.User;
import ru.xromza.user.utils.ClientType;
import ru.xromza.user.utils.UserRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final IndividualDetailsRequestMapper individualDetailsRequestMapper;
    private final LegalDetailsRequestMapper legalDetailsRequestMapper;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @Transactional
    public AuthResult register(UserRegistrationDto request) {
        User user = User.builder()
                .login(request.getLogin())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.CLIENT)
                .createdAt(LocalDateTime.now())
                .build();
        DetailsRequestInterface details = request.getDetails();
        if (details instanceof IndividualDetailsRequestDto indDto) {
            IndividualDetails individualEntity = individualDetailsRequestMapper.toEntity(indDto);

            individualEntity.setUser(user);

            user.setIndividualDetails(individualEntity);
            user.setClientType(ClientType.INDIVIDUAL);
            user.setIsApproved(true);
        } else if (details instanceof LegalDetailsRequestDto legalDto) {

            LegalDetails legalEntity = legalDetailsRequestMapper.toEntity(legalDto);

            legalEntity.setUser(user);

            user.setLegalDetails(legalEntity);
            user.setClientType(ClientType.LEGAL);
            user.setIsApproved(false);
        } else {
            throw new AuthException("Неизвестный тип клиента");
        }
        User savedUser = userService.saveNew(user);
        String jwtToken = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);
        refreshTokenService.saveToken(savedUser.getId(), refreshToken);
        return AuthResult.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .role(savedUser.getRole())
                .description("Успешная регистрация")
                .build();

    }

    public AuthResult refreshAccessToken(String refreshToken) {
        Long userId = refreshTokenService.findUserByToken(refreshToken)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by credentials"));
        User user = userService.findById(userId);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        refreshTokenService.revokeToken(refreshToken);
        refreshTokenService.saveToken(userId, newRefreshToken);
        String newAccessToken = jwtService.generateToken(user);
        return AuthResult.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .role(user.getRole())
                .description("Token refreshed")
                .build();
    }

    @Transactional
    public AuthResult authenticate(AuthRequestDto request) {
        User user = userService.findByLogin(request.getLogin());

        if (!user.getIsApproved())
            throw new DisabledException("Ваш аккаунт ожидает подтверждения администратором");
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash()))
            throw new BadCredentialsException("Неверный логин или пароль");

        refreshTokenService.deleteTokenByUserId(user.getId());

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        refreshTokenService.saveToken(user.getId(), refreshToken);
        return AuthResult
                .builder()
                .accessToken(jwtToken)
                .description("Успешный вход")
                .role(user.getRole())
                .refreshToken(refreshToken)
                .build();
    }

    public StatusDto logout(String refreshToken) {
        refreshTokenService.revokeToken(refreshToken);
        return StatusDto.builder()
                .status("Ok")
                .description("Токен отозван")
                .build();
    }

    @Transactional
    public StatusDto changePassword(UserDetails userDetails, String password) {
        User user = userService.getApprovedUserByLogin(userDetails.getUsername());
        if (passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new NewPasswordMatchesOldException("Новый пароль не должен совпадать со старым");
        }
        user.setPasswordHash(passwordEncoder.encode(password));
        refreshTokenService.deleteTokenByUserId(user.getId());
        userService.update(user);
        return StatusDto.builder()
                .description("Пароль обновлён")
                .status("Успешно")
                .build();
    }

}
