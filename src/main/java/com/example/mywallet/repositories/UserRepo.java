package com.example.mywallet.repositories;


import com.example.mywallet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    User findByEmailIgnoreCase(String email);
    Optional<User> findByEmail(String email);



}
