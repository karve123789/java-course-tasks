package com.example.company.controller;

import com.example.company.dto.EmployeeRequestDto;
import com.example.company.model.Department;
import com.example.company.model.Employee;
import com.example.company.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompanyService companyService;

    @Test
    void createEmployee_shouldReturnCreatedEmployee() throws Exception {
        // 1. Arrange (Подготовка)
        EmployeeRequestDto requestDto = new EmployeeRequestDto();
        requestDto.setFirstName("Сергей");
        requestDto.setLastName("Сергеев");
        requestDto.setDepartmentId(1L);

        Employee returnedEmployee = new Employee(1L, "Сергей", "Сергеев", null, 0, new Department(1L, "Test"));

        when(companyService.createEmployee(any(EmployeeRequestDto.class))).thenReturn(returnedEmployee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk()) // Ожидаем HTTP-статус 200 OK
                .andExpect(jsonPath("$.firstName").value("Сергей"))
                .andExpect(jsonPath("$.id").value(1L));
    }
}