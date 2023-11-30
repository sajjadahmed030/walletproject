package com.example.mywallet.service.impl;


import com.example.mywallet.DTO.UserDto;
import com.example.mywallet.entities.ConfirmationToken;
import com.example.mywallet.entities.User;
import com.example.mywallet.exceptions.Exception;
import com.example.mywallet.mapper.Mapper;
import com.example.mywallet.repositories.ConfirmationTokenRepo;
import com.example.mywallet.repositories.UserRepo;
import com.example.mywallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ConfirmationTokenRepo confirmationTokenRepo;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private Mapper mapper;

    public void saveUser(UserDto userDto)
    {
        UserDto savedUser=save(userDto);
        User user=mapper.Dto_To_User(savedUser);
        ConfirmationToken confirmationToken=new ConfirmationToken(user);
        confirmationTokenRepo.save(confirmationToken);
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        String email=savedUser.getEmail();
        String Token=confirmationToken.getConfirmationToken();
        emailServiceImpl.sendEmail(mailMessage,email,Token);


    }
    public void confirmUser(String confirmationToken)
    {
        ConfirmationToken optionalToken=confirmationTokenRepo.findByConfirmationToken(confirmationToken).orElseThrow(()->new Exception("Illegal Token", HttpStatus.NOT_FOUND));
        optionalToken.getUser().setEnabled(true);
        userRepo.save(optionalToken.getUser());
    }
    public UserDto save(UserDto userDto)
    {
        User user=mapper.Dto_To_User(userDto);
        user=userRepo.save(user);
        return mapper.USER_DTO(user);
    }

    public List<UserDto> getAllUsers(String sortingElement)
    {
        return mapper.USER_DTO_LIST(userRepo.findAll(Sort.by(sortingElement)));
    }

}
