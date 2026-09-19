package com.Questoria.repository;

import com.Questoria.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findBySteamAppId(Long steamAppId);
}