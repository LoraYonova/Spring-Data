package com.example.json.service;

import com.example.json.model.dto.UserSoldDTO;
import com.example.json.model.entity.User;

import java.io.IOException;
import java.util.List;

public interface UserService {
    void seedUsers() throws IOException;

    User findRandomUser();

    List<UserSoldDTO> findAllUsersWithMoreThanOneSoldProducts();


}
