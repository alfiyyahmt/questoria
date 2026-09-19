package com.Questoria.service;

import com.Questoria.model.Player;
import com.Questoria.model.Quest;
import com.Questoria.model.QuestPlayer;
import com.Questoria.repository.QuestPlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestPlayerService {

    private final QuestPlayerRepository questPlayerRepository;

    public QuestPlayerService(
            QuestPlayerRepository questPlayerRepository) {

        this.questPlayerRepository = questPlayerRepository;
    }

    public List<QuestPlayer> getAllQuestPlayers() {

        return questPlayerRepository.findAll();
    }

    public QuestPlayer saveQuestPlayer(
            QuestPlayer questPlayer) {

        return questPlayerRepository.save(questPlayer);
    }

    public QuestPlayer getQuestPlayer(
            Player player,
            Quest quest) {

        return questPlayerRepository
                .findByPlayerAndQuest(player, quest)
                .orElse(null);
    }

    public QuestPlayer joinQuest(
            Player player,
            Quest quest) {

        QuestPlayer existing =
                getQuestPlayer(player, quest);

        if (existing != null) {
            return existing;
        }

        QuestPlayer questPlayer =
                new QuestPlayer();

        questPlayer.setPlayer(player);
        questPlayer.setQuest(quest);
        questPlayer.setProgress(0);
        questPlayer.setCompleted(false);

        return questPlayerRepository.save(
                questPlayer
        );
    }

    public QuestPlayer updateProgress(
            Player player,
            Quest quest,
            int progress) {

        QuestPlayer questPlayer =
                getQuestPlayer(player, quest);

        if (questPlayer == null) {
            return null;
        }

        if (progress < 0) {
            progress = 0;
        }

        if (quest.getTargetProgress() <= 0) {

            questPlayer.setProgress(0);
            questPlayer.setCompleted(false);

            return questPlayerRepository.save(
                    questPlayer
            );
        }

        if (progress > quest.getTargetProgress()) {
            progress = quest.getTargetProgress();
        }

        questPlayer.setProgress(progress);

        questPlayer.setCompleted(
                progress >= quest.getTargetProgress()
        );

        return questPlayerRepository.save(
                questPlayer
        );
    }
}