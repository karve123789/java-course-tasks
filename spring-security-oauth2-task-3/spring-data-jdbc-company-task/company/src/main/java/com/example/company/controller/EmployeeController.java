package com.example.company.controller;

import com.example.company.dto.EmployeeRequestDto;
import com.example.company.dto.EmployeeResponseDto;
import com.example.company.model.Employee;
import com.example.company.projection.EmployeeProjection;
import com.example.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public List<EmployeeResponseDto> getAllEmployees() {
        return companyService.getAllEmployees();
    }

    @GetMapping("/projected")
    public List<EmployeeProjection> getAllEmployeesProjected() {
        return companyService.getAllEmployeesProjected();
    }

    @PostMapping
    public Employee createEmployee(@RequestBody EmployeeRequestDto employeeDto) {
        return companyService.createEmployee(employeeDto);
    }
}