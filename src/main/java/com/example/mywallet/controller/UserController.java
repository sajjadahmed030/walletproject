package com.example.mywallet.controller;


import com.example.mywallet.DTO.AuthenticationResponse;
import com.example.mywallet.DTO.LoginDto;
import com.example.mywallet.DTO.UserDto;
import com.example.mywallet.entities.User;
import com.example.mywallet.repositories.UserRepo;
import com.example.mywallet.service.UserService;
import com.example.mywallet.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    private final UserRepo userRepo;


    @PostMapping(value="/auth/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto) {

        userService.saveUser(userDto);
        return ResponseEntity.ok().body("Click Activation link sent to your email to Activate your Account");
    }
    @PostMapping(value="/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody LoginDto loginDto) {

      AuthenticationResponse authenticationResponse= userService.authenticate(loginDto);
        return ResponseEntity.ok().body(authenticationResponse);
    }


    @RequestMapping(value="/auth/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token") String confirmationToken){


        userService.confirmUser(confirmationToken);
        return ResponseEntity.ok().body("User can login");
    }
    @GetMapping("/getallUser/{sortingElement}")
    public ResponseEntity<List<UserDto>> users(@PathVariable String sortingElement)
    {
        System.out.println(sortingElement);
        List<UserDto> userDto=userService.getAllUsers(sortingElement);


        return  ResponseEntity.ok().body(userDto);
    }
    @PatchMapping("/update/{id}/{firstname}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer id,@PathVariable String firstname)
    {
        UserDto userDto=userService.updateUser( id,firstname);

        return  ResponseEntity.ok().body(userDto);

    }

}
