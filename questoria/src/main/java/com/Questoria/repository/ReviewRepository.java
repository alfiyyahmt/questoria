package com.Questoria.repository;

import com.Questoria.model.Game;
import com.Questoria.model.Player;
import com.Questoria.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByOrderByCreatedAtDesc();

    List<Review> findByPlayerOrderByCreatedAtDesc(Player player);

    List<Review> findByGameOrderByCreatedAtDesc(Game game);

    Optional<Review> findByPlayerAndGame(Player player, Game game);
}