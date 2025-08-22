package com.example.company.controller;

import com.example.company.model.Department;
import com.example.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public List<Department> getAllDepartments() {
        return companyService.getAllDepartments();
    }

    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        return companyService.createDepartment(department);
    }
}