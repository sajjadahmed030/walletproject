package com.example.mywallet.service.impl;


import com.example.mywallet.DTO.AuthenticationResponse;
import com.example.mywallet.DTO.LoginDto;
import com.example.mywallet.DTO.UserDto;
import com.example.mywallet.entities.ConfirmationToken;
import com.example.mywallet.entities.Role;
import com.example.mywallet.entities.User;
import com.example.mywallet.entities.Wallet;
import com.example.mywallet.exceptions.Exception;
import com.example.mywallet.mapper.Mapper;
import com.example.mywallet.repositories.ConfirmationTokenRepo;
import com.example.mywallet.repositories.UserRepo;
import com.example.mywallet.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private JwtServiceImpl jwtService;

    @Transactional
    public void saveUser(UserDto userDto)
    {
//        UserDto savedUser=save(userDto);
        User user=mapper.Dto_To_User(userDto);
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(false);
        Wallet wallet=new Wallet();
        wallet.setUser(user);
        wallet.setBalance(1000);
        wallet.setAccountId(user.getLastName());
        user.setWallet(wallet);
        userRepo.save(user);
        ConfirmationToken confirmationToken=new ConfirmationToken(user);
        confirmationTokenRepo.save(confirmationToken);
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        String email=user.getEmail();
        String Token=confirmationToken.getConfirmationToken();
        emailServiceImpl.sendEmail(mailMessage,email,Token);


    }
    public void confirmUser(String confirmationToken)
    {
        ConfirmationToken optionalToken=confirmationTokenRepo.findByConfirmationToken(confirmationToken).orElseThrow(()->new Exception("Illegal Token", HttpStatus.NOT_FOUND));
        User user=optionalToken.getUser();
        user.setEnabled(true);
        userRepo.save(user);
//        save(mapper.USER_DTO(optionalToken.getUser()));
    }
    @org.springframework.transaction.annotation.Transactional
    public UserDto save(UserDto userDto)
    {
        User user=mapper.Dto_To_User(userDto);
        user.setCreatedAt(new Date());
        user=userRepo.save(user);
        return mapper.USER_DTO(user);
    }


    @org.springframework.transaction.annotation.Transactional(readOnly=true)
    public List<UserDto> getAllUsers(String sortingElement)
    {
        return mapper.USER_DTO_LIST(userRepo.findAll(Sort.by(sortingElement)));
    }

    @org.springframework.transaction.annotation.Transactional( rollbackFor = Exception.class)
    @Override
    public UserDto updateUser(Integer id, String firstname) {
        Optional<User> user= userRepo.findById(id);
        if(user.isPresent())
        {
            User userFromDb=user.get();
            userFromDb.setFirstName(firstname);
            UserDto userDto=mapper.USER_DTO(userFromDb);
            UserDto savedUser=save(userDto);
            return userDto;
        }
        throw new Exception("User Not found by id:"+id,HttpStatus.NOT_FOUND);
    }

    @Override
    public AuthenticationResponse authenticate(LoginDto loginDto) {
        authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(
                     loginDto.getEmail(),
                     loginDto.getPassword()
             )
        );
        var user=userRepo.findByEmail(loginDto.getEmail())
                .orElseThrow(()->new Exception("Wrong credential",HttpStatus.BAD_REQUEST));

        String Token=jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(Token)
                .build();
    }

}
