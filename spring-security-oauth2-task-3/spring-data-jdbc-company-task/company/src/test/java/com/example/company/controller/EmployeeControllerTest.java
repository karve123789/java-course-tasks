package com.example.company.controller;

import com.example.company.config.SecurityConfig;
import com.example.company.dto.EmployeeRequestDto;
import com.example.company.model.Department;
import com.example.company.model.Employee;
import com.example.company.service.CompanyService;
import com.example.company.service.CustomOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(SecurityConfig.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompanyService companyService;

    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;



    @Test
    @WithMockUser(roles = "ADMIN")
    void createEmployee_asAdmin_shouldReturnCreatedEmployee() throws Exception {
        EmployeeRequestDto requestDto = new EmployeeRequestDto();
        requestDto.setFirstName("Сергей");
        requestDto.setLastName("Сергеев");
        requestDto.setDepartmentId(1L);

        Employee returnedEmployee = new Employee(1L, "Сергей", "Сергеев", null, 0, new Department(1L, "Test"));

        when(companyService.createEmployee(any(EmployeeRequestDto.class))).thenReturn(returnedEmployee);

        // Act & Assert
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Сергей"))
                .andExpect(jsonPath("$.id").value(1L));
    }




    @Test
    @WithMockUser(roles = "USER")
    void createEmployee_asUser_shouldReturnForbidden() throws Exception {
        // Arrange
        EmployeeRequestDto requestDto = new EmployeeRequestDto();
        requestDto.setFirstName("Сергей");

        // Act & Assert
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllEmployees_asUser_shouldReturnOk() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk());
    }

    @Test
    void createEmployee_asAnonymous_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().is3xxRedirection());
    }
}