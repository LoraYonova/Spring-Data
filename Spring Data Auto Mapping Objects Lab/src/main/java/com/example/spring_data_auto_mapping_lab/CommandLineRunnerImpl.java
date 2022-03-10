package com.example.spring_data_auto_mapping_lab;

import com.example.spring_data_auto_mapping_lab.model.dto.EmployeeDTO;
import com.example.spring_data_auto_mapping_lab.model.dto.ManagerDTO;
import com.example.spring_data_auto_mapping_lab.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final EmployeeService employeeService;

    @Autowired
    public CommandLineRunnerImpl(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @Override
    public void run(String... args) throws Exception {

        ManagerDTO managerDTO = employeeService.findOne(1L);
        System.out.println(managerDTO.getFirstName() + " "
                + managerDTO.getLastName());
        managerDTO.getSubordinates().forEach(employeeDTO ->
                System.out.println("\t" + employeeDTO.getFirstName() + " " + employeeDTO.getLastName() + " " + employeeDTO.getSalary()));

    }
}
