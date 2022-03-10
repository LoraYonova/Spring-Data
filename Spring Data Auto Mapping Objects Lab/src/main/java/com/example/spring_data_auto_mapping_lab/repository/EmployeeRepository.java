package com.example.spring_data_auto_mapping_lab.repository;

import com.example.spring_data_auto_mapping_lab.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
