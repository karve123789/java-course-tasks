package com.example.company;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Публичный эндпоинт /api/departments должен быть доступен без аутентификации")
    void departmentsEndpoint_shouldBePublic() throws Exception {
        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Защищенный эндпоинт /api/employees должен возвращать 401 Unauthorized для анонимного API-запроса")
    void employeesEndpoint_shouldReturnUnauthorizedForAnonymousApiCall() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Защищенный эндпоинт /api/employees должен быть доступен для аутентифицированного пользователя")
    void employeesEndpoint_shouldBeAccessibleForAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk());
    }
}