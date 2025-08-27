package com.example.company.dto;

import lombok.Data;

@Data
public class EmployeeRequestDto {
    private String firstName;
    private String lastName;
    private String position;
    private double salary;
    private Long departmentId;
}