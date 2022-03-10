package com.example.spring_data_auto_mapping_lab.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationBeanModelMapper {

    @Bean
    public ModelMapper modelMapper() {

        return new ModelMapper();
    }
}

