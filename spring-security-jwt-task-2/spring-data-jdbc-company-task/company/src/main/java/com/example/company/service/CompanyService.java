package com.example.company.service;

import com.example.company.dto.EmployeeRequestDto;
import com.example.company.dto.EmployeeResponseDto;
import com.example.company.model.Department;
import com.example.company.model.Employee;
import com.example.company.projection.EmployeeProjection;
import com.example.company.repository.DepartmentRepository;
import com.example.company.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employee -> new EmployeeResponseDto(
                        employee.getId(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getPosition(),
                        employee.getSalary(),
                        employee.getDepartment().getName()
                ))
                .collect(Collectors.toList());
    }

    public List<EmployeeProjection> getAllEmployeesProjected() {
        return employeeRepository.findAllProjectedBy();
    }

    public Employee createEmployee(EmployeeRequestDto dto) {
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + dto.getDepartmentId()));

        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPosition(dto.getPosition());
        employee.setSalary(dto.getSalary());
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Сотрудник с ID " + id + " не найден.");
        }
        employeeRepository.deleteById(id);
    }
}