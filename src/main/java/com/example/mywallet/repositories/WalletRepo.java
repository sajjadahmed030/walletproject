package com.example.mywallet.repositories;

import com.example.mywallet.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface WalletRepo extends JpaRepository<Wallet,Integer> {

    Optional<Wallet> findByUserEmail(String email);

}
