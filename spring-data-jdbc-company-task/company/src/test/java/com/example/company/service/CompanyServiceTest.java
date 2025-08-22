package com.example.company.service;

import com.example.company.dto.EmployeeRequestDto;
import com.example.company.model.Department;
import com.example.company.model.Employee;
import com.example.company.repository.DepartmentRepository;
import com.example.company.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private CompanyService companyService;

    private Department department;
    private EmployeeRequestDto employeeRequestDto;

    @BeforeEach
    void setUp() {
        department = new Department(1L, "IT");
        employeeRequestDto = new EmployeeRequestDto();
        employeeRequestDto.setFirstName("Иван");
        employeeRequestDto.setLastName("Иванов");
        employeeRequestDto.setDepartmentId(1L);
    }

    @Test
    void createEmployee_whenDepartmentExists_shouldSaveAndReturnEmployee() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));


        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = companyService.createEmployee(employeeRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Иван");
        assertThat(result.getDepartment().getName()).isEqualTo("IT");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_whenDepartmentNotFound_shouldThrowException() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());
        employeeRequestDto.setDepartmentId(99L);

        assertThrows(EntityNotFoundException.class, () -> {
            companyService.createEmployee(employeeRequestDto);
        });

        verify(employeeRepository, never()).save(any(Employee.class));
    }
}