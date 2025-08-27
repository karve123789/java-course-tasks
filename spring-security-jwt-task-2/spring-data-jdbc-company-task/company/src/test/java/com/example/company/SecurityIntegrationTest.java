package com.example.company;


import com.example.company.dto.EmployeeRequestDto;
import com.example.company.model.Department;
import com.example.company.repository.DepartmentRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll(); // Очищаем на всякий случай
        testDepartment = new Department(null, "Test Department");
        departmentRepository.save(testDepartment);
    }

    @Test
    @DisplayName("Анонимный пользователь НЕ МОЖЕТ получить доступ к защищенному GET /api/employees")
    void anonymousUser_cannotAccess_protectedGetEndpoints() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("USER МОЖЕТ просматривать сотрудников, но НЕ МОЖЕТ их создавать или удалять")
    void userRole_canRead_butCannotWrite() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk());

        EmployeeRequestDto newEmployee = new EmployeeRequestDto();
        newEmployee.setFirstName("test");
        newEmployee.setDepartmentId(testDepartment.getId());

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "moderator", roles = "MODERATOR")
    @DisplayName("MODERATOR МОЖЕТ удалять, но НЕ МОЖЕТ создавать сотрудников")
    void moderatorRole_canDelete_butCannotCreate() throws Exception {
        EmployeeRequestDto newEmployee = new EmployeeRequestDto();
        newEmployee.setFirstName("test");
        newEmployee.setDepartmentId(testDepartment.getId());

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/employees/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "superadmin", roles = "SUPER_ADMIN")
    @DisplayName("SUPER_ADMIN МОЖЕТ и создавать, и удалять сотрудников")
    void superAdminRole_canDoEverything() throws Exception {
        EmployeeRequestDto newEmployee = new EmployeeRequestDto();
        newEmployee.setFirstName("test");
        newEmployee.setLastName("test");
        newEmployee.setDepartmentId(testDepartment.getId());

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/employees/999"))
                .andExpect(status().isNotFound());
    }
}