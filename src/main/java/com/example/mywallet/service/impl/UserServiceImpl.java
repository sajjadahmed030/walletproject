package com.example.mywallet.service.impl;


import com.example.mywallet.DTO.AuthenticationResponse;
import com.example.mywallet.DTO.LoginDto;
import com.example.mywallet.DTO.UserDto;
import com.example.mywallet.entities.*;
import com.example.mywallet.exceptions.Exception;
import com.example.mywallet.mapper.Mapper;
import com.example.mywallet.repositories.TokenRepo;
import com.example.mywallet.repositories.UserRepo;
import com.example.mywallet.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;


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
    @Autowired
    private TokenRepo tokenRepo;

    @Transactional
    public void saveUser(UserDto userDto)
    {
//        UserDto savedUser=save(userDto);
        var optionalUser=userRepo.findByEmail(userDto.getEmail());
        if(optionalUser.isPresent()){
            throw  new Exception("account with given email already exist",HttpStatus.BAD_REQUEST);
        }

            User user=mapper.Dto_To_User(userDto);
            user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            user.setRole(Role.USER);
            user.setEnabled(true);//todo make true to false to male email work
            Wallet wallet=new Wallet();
            wallet.setUser(user);
            wallet.setBalance(1000);
            wallet.setAccountId(user.getLastName());
            user.setWallet(wallet);
            User savedUser=userRepo.save(user);
            User UserDetails=User.builder()
                    .email("activation"+savedUser.getEmail())
                    .build();
            var activationToken=jwtService.generateToken(UserDetails);
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            String email=savedUser.getEmail();
            emailServiceImpl.sendEmail(mailMessage,email,activationToken);
            saveAuthToken(activationToken,savedUser,TokenType.ACTIVATION);
            var jwtToken=jwtService.generateToken(savedUser);
            saveAuthToken(jwtToken,savedUser,TokenType.BEARER);


    }
    @Override
    public AuthenticationResponse authenticate(LoginDto loginDto) {
        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        var user=userRepo.findByEmail(loginDto.getEmail())
                .orElseThrow(()->new Exception("Wrong credential",HttpStatus.BAD_REQUEST));

        String Token=jwtService.generateToken(user);
        revokeAllUserToken(user);
        saveAuthToken(Token,user,TokenType.BEARER);

        return AuthenticationResponse
                .builder()
                .token(Token)
                .build();

    }
    public void confirmUser(String confirmationToken)
    {

        System.out.println(confirmationToken);
        String fakeEmail= jwtService.extractUsername(confirmationToken);
        System.out.println(fakeEmail);
        String realEmail=fakeEmail.substring(10);
        System.out.println(realEmail);
        var user= userRepo.findByEmail(realEmail).orElseThrow(() -> new Exception("Invalid token", HttpStatus.BAD_REQUEST));
        List<Token> token=tokenRepo.findAllValidTokensByUser(user.getId());
        if(token.isEmpty())
        {
            throw new Exception("Expired Token",HttpStatus.BAD_REQUEST);
        }
        Token token1=token.get(0);
        if(token1.getToken().equals(confirmationToken))
        {
            token1.setExpired(true);
            token1.setRevoked(true);
            tokenRepo.save(token1);
        }
        user.setEnabled(true);
        userRepo.save(user);
//        ConfirmationToken optionalToken=confirmationTokenRepo.findByConfirmationToken(confirmationToken).orElseThrow(()->new Exception("Illegal Token", HttpStatus.NOT_FOUND));
//        User user=optionalToken.getUser();
//        user.setEnabled(true);
//        userRepo.save(user);
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



    public void saveAuthToken(String token,User user,TokenType tokenType)
    {
        Token authenticationToken= Token.builder()
                .token(token)
                .user(user)
                .tokenType(tokenType)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepo.save(authenticationToken);
    }
    private void revokeAllUserToken(User user)
    {
        var validToken=tokenRepo.findAllValidTokensByUser(user.getId());
        if(validToken.isEmpty())
        {
            return;
        }
        validToken.forEach(t->{
            t.setExpired(true);
            t.setRevoked(true);
        });
       tokenRepo.saveAll(validToken);
    }

}
