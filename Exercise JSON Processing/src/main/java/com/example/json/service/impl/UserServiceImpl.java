package com.example.json.service.impl;

import com.example.json.model.dto.*;
import com.example.json.model.entity.User;
import com.example.json.repository.UserRepository;
import com.example.json.service.UserService;
import com.example.json.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.json.constants.GlobalConstants.RESOURCE_FILE_PATH;
import static java.nio.file.Files.*;

@Service
public class UserServiceImpl implements UserService {

    private static final String USERS_FILE_PATH = "users.json";

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserRepository userRepository;
    private final Gson gson;

    public UserServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, UserRepository userRepository, Gson gson) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userRepository = userRepository;
        this.gson = gson;
    }


    @Override
    public void seedUsers() throws IOException {

        if (userRepository.count() > 0) {
            return;
        }

//        Arrays.stream(gson.fromJson(readString(Path.of(RESOURCE_FILE_PATH + USERS_FILE_PATH)),
//                UserSeedDTO[].class))
//                .filter(validationUtil::isValid)
//                .map(userSeedDTO -> modelMapper.map(userSeedDTO, User.class))
//                .forEach(userRepository::save);


        String fileContent = readString(Path.of(RESOURCE_FILE_PATH + USERS_FILE_PATH));

        UserSeedDTO[] userSeedDTOS = gson.fromJson(fileContent, UserSeedDTO[].class);

        Arrays.stream(userSeedDTOS).filter(validationUtil::isValid)
                .map(userSeedDTO -> modelMapper.map(userSeedDTO, User.class))
                .forEach(userRepository::save);

    }

    @Override
    public User findRandomUser() {
        long randomId = ThreadLocalRandom.current()
                .nextLong(1, userRepository.count() + 1);

        return userRepository.findById(randomId)
                .orElse(null);
    }

    @Override
    public List<UserSoldDTO> findAllUsersWithMoreThanOneSoldProducts() {

        return  userRepository.findAllUsersWithMoreThanOneSoldProductsOrderByLastNameAndFirstName()
                .stream().map(user -> modelMapper.map(user, UserSoldDTO.class))
                .collect(Collectors.toList());

    }
}
