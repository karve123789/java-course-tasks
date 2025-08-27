package com.example.company.repository;
import com.example.company.model.Employee;
import com.example.company.projection.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<EmployeeProjection> findAllProjectedBy();
}