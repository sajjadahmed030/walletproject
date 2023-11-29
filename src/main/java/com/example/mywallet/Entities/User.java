package com.example.mywallet.Entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "_users")
@Getter
@Setter
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Integer Id;

    private String firstName;

    private String lastName;
    private String email;
    private boolean isEnabled;

}
