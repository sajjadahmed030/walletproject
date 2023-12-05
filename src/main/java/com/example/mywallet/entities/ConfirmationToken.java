package com.example.mywallet.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Token")
@Setter
@Getter
public class ConfirmationToken extends AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;


    public ConfirmationToken() {}

    public ConfirmationToken(User user) {
        this.user = user;
        createdAt = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }

}
