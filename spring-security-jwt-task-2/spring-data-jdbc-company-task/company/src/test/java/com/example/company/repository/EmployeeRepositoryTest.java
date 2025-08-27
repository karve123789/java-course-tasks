package com.example.company.repository;

import com.example.company.model.Department;
import com.example.company.model.Employee;
import com.example.company.projection.EmployeeProjection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void whenFindAllProjectedBy_thenReturnsProjections() {
        Department devDepartment = new Department(null, "Development");
        entityManager.persist(devDepartment);

        Employee employee = new Employee(null, "Иван", "Иванов", "Разработчик", 150000, devDepartment);
        entityManager.persist(employee);
        entityManager.flush();

        List<EmployeeProjection> projections = employeeRepository.findAllProjectedBy();

        assertThat(projections).hasSize(1);
        EmployeeProjection projection = projections.get(0);
        assertThat(projection.getFullName()).isEqualTo("Иван Иванов");
        assertThat(projection.getPosition()).isEqualTo("Разработчик");
        assertThat(projection.getDepartmentName()).isEqualTo("Development");
    }
}