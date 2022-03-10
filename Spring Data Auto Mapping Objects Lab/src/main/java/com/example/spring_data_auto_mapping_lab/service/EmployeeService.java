package com.example.spring_data_auto_mapping_lab.service;

import com.example.spring_data_auto_mapping_lab.model.dto.EmployeeDTO;
import com.example.spring_data_auto_mapping_lab.model.dto.ManagerDTO;

public interface EmployeeService {

    ManagerDTO findOne(Long id);
}
