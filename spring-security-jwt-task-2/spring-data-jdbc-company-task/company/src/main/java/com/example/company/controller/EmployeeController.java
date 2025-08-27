package com.example.company.controller;

import com.example.company.dto.EmployeeRequestDto;
import com.example.company.dto.EmployeeResponseDto;
import com.example.company.model.Employee;
import com.example.company.projection.EmployeeProjection;
import com.example.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<EmployeeResponseDto> getAllEmployees() {
        return companyService.getAllEmployees();
    }

    @GetMapping("/projected")
    @PreAuthorize("isAuthenticated()")
    public List<EmployeeProjection> getAllEmployeesProjected() {
        return companyService.getAllEmployeesProjected();
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Employee createEmployee(@RequestBody EmployeeRequestDto employeeDto) {
        return companyService.createEmployee(employeeDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'SUPER_ADMIN')")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        companyService.deleteEmployee(id);
        return ResponseEntity.ok("Сотрудник с ID " + id + " успешно удален.");
    }
}