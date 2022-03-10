package com.example.spring_data_auto_mapping_lab.model.dto;

import java.math.BigDecimal;

public class EmployeeDTO extends BasicEmployeeDTO {

    private BigDecimal salary;

    public EmployeeDTO() {}


    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
