package com.Questoria.repository;

import com.Questoria.model.BacklogItem;
import com.Questoria.model.Game;
import com.Questoria.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BacklogRepository extends JpaRepository<BacklogItem, Long> {

    Optional<BacklogItem> findByPlayerAndGame(Player player, Game game);

    List<BacklogItem> findByPlayer(Player player);

    List<BacklogItem> findByPlayerAndStatus(Player player, String status);

    Optional<BacklogItem> findByIdAndPlayer(Long id, Player player);
}