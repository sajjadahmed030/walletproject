package com.example.mywallet.DTO;

import com.example.mywallet.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class TokenDto {

    private Integer id;
    private String confirmationToken;
    private Date createdAt;
    private User user;

}
