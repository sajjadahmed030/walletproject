package com.example.mywallet.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "_users")
@Getter
@Setter

public class User extends AuditLog {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    private String firstName;

    private String lastName;
    private String email;
    private boolean isEnabled;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;



}
