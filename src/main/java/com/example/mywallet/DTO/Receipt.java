package com.example.mywallet.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Receipt {
    private String title;
    private String firstName;
    private String lastName;
    private double currentBalance;
    private String accountId;
}



