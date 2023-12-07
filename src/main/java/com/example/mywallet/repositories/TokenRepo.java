package com.example.mywallet.repositories;

import com.example.mywallet.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TokenRepo extends JpaRepository<Token,Integer> {

    @Query("""
    select t from Token t inner join User u on t.user.Id=u.Id
    where u.Id=:userId and (t.expired=false or t.revoked=false )
""")
    List<Token> findAllValidTokensByUser(Integer userId);

    Optional<Token> findByToken(String token);
}
