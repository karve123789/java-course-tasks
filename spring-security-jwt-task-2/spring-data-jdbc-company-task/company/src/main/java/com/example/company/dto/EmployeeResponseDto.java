package com.example.company.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String position;
    private double salary;
    private String departmentName;


    public EmployeeResponseDto(Long id, String firstName, String lastName, String position, double salary, String departmentName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.salary = salary;
        this.departmentName = departmentName;
    }
}