package com.example.company.service;

import com.example.company.dto.JwtAuthenticationResponse;
import com.example.company.dto.SignInRequest;
import com.example.company.dto.SignUpRequest;
import com.example.company.model.User;
import com.example.company.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    private static final int MAX_FAILED_ATTEMPTS = 5;

    public User signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(signUpRequest.getRole());
        User savedUser = userRepository.save(user);

        logger.info("Пользователь успешно зарегистрирован: {}", savedUser.getUsername());
        return savedUser;
    }

    @Transactional
    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        String username = signInRequest.getUsername();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, signInRequest.getPassword()));

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalStateException("Пользователь исчез после аутентификации"));
            user.setFailedLoginAttempts(0);
            userRepository.save(user);

            logger.info("Пользователь успешно вошел в систему: {}", username);

            var accessToken = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(user);

            JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse();
            jwtResponse.setAccessToken(accessToken);
            jwtResponse.setRefreshToken(refreshToken);

            return jwtResponse;

        } catch (AuthenticationException e) {
            logger.warn("Неудачная попытка входа для пользователя: {}", username);

            userRepository.findByUsername(username).ifPresent(user -> {
                int attempts = user.getFailedLoginAttempts() + 1;
                user.setFailedLoginAttempts(attempts);
                if (attempts >= MAX_FAILED_ATTEMPTS) {
                    user.setAccountNonLocked(false);
                    logger.warn("Аккаунт пользователя {} заблокирован из-за превышения лимита попыток входа.", username);
                }
                userRepository.save(user);
            });

            throw new BadCredentialsException("Неверный пароль или имя пользователя");
        }
    }

    @Transactional
    public void unlockUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь " + username + " не найден"));

        user.setAccountNonLocked(true);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);

        logger.info("Аккаунт пользователя {} успешно разблокирован администратором.", username);
    }
}