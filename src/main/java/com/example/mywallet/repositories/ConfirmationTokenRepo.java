package com.example.mywallet.repositories;


import com.example.mywallet.entities.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepo extends CrudRepository<ConfirmationToken,Integer> {


    Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);
}
