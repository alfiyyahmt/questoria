package com.Questoria.repository;

import com.Questoria.model.Player;
import com.Questoria.model.SteamAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findBySteamAccount(SteamAccount steamAccount);
}