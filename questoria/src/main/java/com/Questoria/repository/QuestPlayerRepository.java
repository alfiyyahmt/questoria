package com.Questoria.repository;

import com.Questoria.model.Player;
import com.Questoria.model.Quest;
import com.Questoria.model.QuestPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestPlayerRepository extends JpaRepository<QuestPlayer, Long> {

    Optional<QuestPlayer> findByPlayerAndQuest(Player player, Quest quest);
}