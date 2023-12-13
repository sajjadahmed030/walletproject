package com.example.mywallet.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "wallet")
@Data
public class Wallet extends AuditLog{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private double balance;

    private String accountId;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "Id")
    private User user;
}
