package com.example.company.controller;

import com.example.company.dto.SignInRequest;
import com.example.company.dto.SignUpRequest;
import com.example.company.model.Role;
import com.example.company.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Успешная регистрация нового пользователя")
    void signUp_shouldCreateUser_successfully() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setPassword("password");
        signUpRequest.setRole(Role.USER);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("Успешный вход пользователя и получение токена")
    void signIn_shouldReturnTokens_forValidCredentials() throws Exception {
        signUp_shouldCreateUser_successfully();

        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername("testuser");
        signInRequest.setPassword("password");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()));
    }

    @Test
    @DisplayName("Блокировка аккаунта после 5 неудачных попыток входа")
    void signIn_shouldLockAccount_after5FailedAttempts() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("locktest");
        signUpRequest.setPassword("correctpassword");
        signUpRequest.setRole(Role.USER);
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)));

        SignInRequest badLoginRequest = new SignInRequest();
        badLoginRequest.setUsername("locktest");
        badLoginRequest.setPassword("wrongpassword");

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(badLoginRequest)))
                    .andExpect(status().isForbidden());
        }

        var lockedUser = userRepository.findByUsername("locktest").get();
        assertFalse(lockedUser.isAccountNonLocked(), "Аккаунт должен быть заблокирован");

        SignInRequest goodLoginRequest = new SignInRequest();
        goodLoginRequest.setUsername("locktest");
        goodLoginRequest.setPassword("correctpassword");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goodLoginRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Разблокировка аккаунта администратором")
    @WithMockUser(username = "superadmin", roles = "SUPER_ADMIN")
    void unlockUser_shouldUnlockAccount_forAdmin() throws Exception {
        var userToLock = new com.example.company.model.User();
        userToLock.setUsername("lockeduser");
        userToLock.setPassword("password");
        userToLock.setRole(Role.USER);
        userToLock.setAccountNonLocked(false);
        userRepository.save(userToLock);

        mockMvc.perform(post("/unlock/lockeduser"))
                .andExpect(status().isOk());

        var unlockedUser = userRepository.findByUsername("lockeduser").get();
        assertTrue(unlockedUser.isAccountNonLocked(), "Аккаунт должен быть разблокирован");
    }
}