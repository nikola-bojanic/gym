package com.nikolabojanic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    /**
     * Constructs an instance of {@link TokenEntity}.
     *
     * @param data    {@link String} Token content.
     * @param type    {@link TokenType} Type of the token.
     * @param expired {@link Boolean} representing the token expired state
     * @param revoked {@link Boolean} representing the token revoked state
     * @param user    {@link UserEntity} for which the token was issued.
     */
    public TokenEntity(String data, TokenType type, boolean expired, boolean revoked, UserEntity user) {
        this.data = data;
        this.type = type;
        this.expired = expired;
        this.revoked = revoked;
        this.user = user;
    }
}
