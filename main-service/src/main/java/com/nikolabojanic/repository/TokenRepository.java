package com.nikolabojanic.repository;

import com.nikolabojanic.entity.TokenEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    @Query("SELECT t FROM TokenEntity t WHERE t.user.id = :id "
        + "AND (t.expired = false OR t.revoked = false)")
    List<TokenEntity> findValidTokensForUser(Long id);

    @Query("SELECT t FROM TokenEntity t WHERE t.data = :data")
    Optional<TokenEntity> findByData(String data);

    @Modifying
    @Query("DELETE FROM TokenEntity WHERE user.id = :id "
        + "AND data != :data")
    void deleteInvalidTokens(Long id, String data);

    @Modifying
    @Query("DELETE FROM TokenEntity WHERE user.id = :id")
    void deleteForUser(Long id);
}
