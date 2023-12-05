package com.example.mywallet.service;

import com.example.mywallet.DTO.Receipt;

public  interface WalletService {

    public Receipt deposit(double amount);

    public Receipt withdraw(double amount);

    public Receipt checkBalance();
}
