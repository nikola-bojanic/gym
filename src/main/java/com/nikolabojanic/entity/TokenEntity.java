package com.nikolabojanic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "token")
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String data;
    @Enumerated(EnumType.STRING)
    private TokenType type;
    private boolean expired;
    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public TokenEntity(String data, TokenType type, boolean expired, boolean revoked, UserEntity user) {
        this.data = data;
        this.type = type;
        this.expired = expired;
        this.revoked = revoked;
        this.user = user;
    }
}
