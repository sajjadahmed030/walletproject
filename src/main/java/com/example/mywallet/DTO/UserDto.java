package com.example.mywallet.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {


    private Integer Id;


    @NotBlank(message = "firstname missing")
    private String firstName;

    @NotBlank(message = "lastname missing")
    private String lastName;
    @Email(message = "invalid email")
    private String email;
    private boolean isEnabled;

}
