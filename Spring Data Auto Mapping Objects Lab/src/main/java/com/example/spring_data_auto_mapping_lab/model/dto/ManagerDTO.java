package com.example.spring_data_auto_mapping_lab.model.dto;

import java.util.List;

public class ManagerDTO extends BasicEmployeeDTO {

    private List<EmployeeDTO> subordinates;



    public List<EmployeeDTO> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(List<EmployeeDTO> subordinates) {
        this.subordinates = subordinates;
    }
}
