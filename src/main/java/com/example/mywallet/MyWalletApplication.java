package com.example.mywallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MyWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyWalletApplication.class, args);
    }

}
