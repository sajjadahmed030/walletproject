package com.example.mywallet.controller;


import com.example.mywallet.DTO.UserDto;
import com.example.mywallet.entities.User;
import com.example.mywallet.repositories.UserRepo;
import com.example.mywallet.service.UserService;
import com.example.mywallet.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;


    @PostMapping(value="/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto) {

        userService.saveUser(userDto);
        return ResponseEntity.ok().body("Click Activation link sent to your email to Activate your Account");
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token") String confirmationToken){


        userService.confirmUser(confirmationToken);
        return ResponseEntity.ok().body("User can login");
    }
    @GetMapping("/getallUser/{sortingElement}")
    public ResponseEntity<List<User>> users(@PathVariable String sortingElement)
    {
        System.out.println(sortingElement);
        userService.getAllUsers(sortingElement);

        return  ResponseEntity.ok().body(userRepo.findAll(Sort.by(sortingElement)));
    }


}
