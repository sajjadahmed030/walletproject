package com.example.mywallet.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    private Integer Id;


    @NotBlank(message = "firstname missing")
    private String firstName;

    @NotBlank(message = "lastname missing")
    private String lastName;
    @Email(message = "invalid email")
    private String email;
    @NotBlank(message = "missing password")
    private String password;

}
