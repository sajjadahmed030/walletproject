package com.example.mywallet.service.impl;

import com.example.mywallet.DTO.Receipt;
import com.example.mywallet.entities.Wallet;
import com.example.mywallet.exceptions.Exception;
import com.example.mywallet.mapper.Mapper;
import com.example.mywallet.repositories.UserRepo;
import com.example.mywallet.repositories.WalletRepo;
import com.example.mywallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {


    @Autowired
    private Mapper mapper;

    @Autowired
    private  WalletRepo walletRepo;
    @Override
    public Receipt deposit(double amount) {
        Optional<Wallet> wallet=walletRepo.findByUserLastName("Nakaam123");

        if(wallet.isPresent())
        {
            Wallet wallet1= wallet.get();
            wallet1.setBalance((wallet1.getBalance()+amount));
            walletRepo.save(wallet1);
            Receipt receipt= mapper.walletToReceipt(wallet1);
            receipt.setTitle("Deposited amount:"+amount);

            return receipt;

        }
        throw new Exception("No wallet found ", HttpStatus.NOT_FOUND);



    }

    @Override
    public Receipt withdraw(double amount) {
        Optional<Wallet> wallet=walletRepo.findByUserLastName("Nakaam123");

        if(wallet.isPresent())
        {
            Wallet wallet1= wallet.get();
            if(wallet1.getBalance()<amount)
            {
                throw new Exception("low balance cant withdraw",HttpStatus.BAD_REQUEST);
            }
            wallet1.setBalance((wallet1.getBalance()-amount));
            walletRepo.save(wallet1);
            Receipt receipt= mapper.walletToReceipt(wallet1);
            receipt.setTitle("Withdrawed amount:"+amount);

            return receipt;

        }
        throw new Exception("No wallet found ", HttpStatus.NOT_FOUND);
    }

    @Override
    public Receipt checkBalance() {
        Optional<Wallet> wallet=walletRepo.findByUserLastName("Nakaam123");

        if(wallet.isPresent())
        {
            Wallet wallet1= wallet.get();
            Receipt receipt= mapper.walletToReceipt(wallet1);
            receipt.setTitle("Total Balance");
            return receipt;

        }
        throw new Exception("No wallet found ", HttpStatus.NOT_FOUND);
    }
}
