package com.example.mywallet.service;

import com.example.mywallet.DTO.UserDto;
import com.example.mywallet.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {


    public void saveUser(UserDto user);

    public void confirmUser(String confirmationToken);

    public List<UserDto> getAllUsers(String sortingElement);





}
