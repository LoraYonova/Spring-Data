package com.example.spring_data_auto_mapping_lab.service;

import com.example.spring_data_auto_mapping_lab.model.dto.ManagerDTO;
import com.example.spring_data_auto_mapping_lab.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper mapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper mapper) {
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
    }

    @Override
    public ManagerDTO findOne(Long id) {

        return mapper.map(employeeRepository.findById(id).orElseThrow(),
                ManagerDTO.class);

    }
}
