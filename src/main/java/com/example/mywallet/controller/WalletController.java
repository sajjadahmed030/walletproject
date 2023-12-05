package com.example.mywallet.controller;

import com.example.mywallet.DTO.Receipt;
import com.example.mywallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService  walletService;

    @PostMapping("/deposit/{amount}")
    public ResponseEntity<Receipt> deposit(@PathVariable double amount)
    {
        Receipt receipt=walletService.deposit(amount);
        return ResponseEntity.ok().body(receipt);
    }
    @GetMapping("/withdraw/{amount}")
    public ResponseEntity<Receipt> withdraw(@PathVariable double amount)
    {
        Receipt receipt=walletService.withdraw(amount);
        return ResponseEntity.ok().body(receipt);
    }
    @GetMapping("/checkbalance")
    public ResponseEntity<Receipt> checkbalance()
    {
        Receipt receipt=walletService.checkBalance();
        return ResponseEntity.ok().body(receipt);
    }

}
