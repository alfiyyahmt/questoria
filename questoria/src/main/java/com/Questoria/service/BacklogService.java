package com.Questoria.service;

import com.Questoria.model.BacklogItem;
import com.Questoria.model.Game;
import com.Questoria.model.Player;
import com.Questoria.model.Quest;
import com.Questoria.model.QuestPlayer;
import com.Questoria.repository.BacklogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BacklogService {

    private final BacklogRepository backlogRepository;
    private final QuestService questService;
    private final QuestPlayerService questPlayerService;

    public BacklogService(
            BacklogRepository backlogRepository,
            QuestService questService,
            QuestPlayerService questPlayerService) {

        this.backlogRepository = backlogRepository;
        this.questService = questService;
        this.questPlayerService = questPlayerService;
    }

    public List<BacklogItem> getAllBacklogItems() {

        return backlogRepository.findAll();
    }

    public List<BacklogItem> getBacklogByPlayer(
            Player player) {

        return backlogRepository.findByPlayer(player);
    }

    public List<BacklogItem> getBacklogByPlayerAndStatus(
            Player player,
            String status) {

        return backlogRepository.findByPlayerAndStatus(
                player,
                status
        );
    }

    public BacklogItem getBacklogItemByIdAndPlayer(
            Long id,
            Player player) {

        return backlogRepository
                .findByIdAndPlayer(id, player)
                .orElse(null);
    }

    public BacklogItem saveBacklogItem(
            BacklogItem backlogItem) {

        BacklogItem saved =
                backlogRepository.save(backlogItem);

        if (backlogItem.getPlayer() != null) {

            syncQuestProgress(
                    backlogItem.getPlayer()
            );
        }

        return saved;
    }

    public BacklogItem getBacklogItem(
            Player player,
            Game game) {

        return backlogRepository
                .findByPlayerAndGame(player, game)
                .orElse(null);
    }

    public void deleteBacklogItem(
            BacklogItem backlogItem) {

        Player player =
                backlogItem.getPlayer();

        backlogRepository.delete(backlogItem);

        if (player != null) {

            syncQuestProgress(player);
        }
    }

    public void syncQuestProgress(
            Player player) {

        if (player == null) {
            return;
        }

        List<BacklogItem> backlogItems =
                backlogRepository.findByPlayer(player);

        int completedGames = 0;

        if (backlogItems != null) {

            for (BacklogItem item : backlogItems) {

                if (item == null) {
                    continue;
                }

                String status =
                        item.getStatus();

                if (status != null
                        && "COMPLETED".equalsIgnoreCase(status)) {

                    completedGames++;
                }
            }
        }

        List<Quest> activeQuests =
                questService.getActiveQuests();

        if (activeQuests == null) {
            return;
        }

        for (Quest quest : activeQuests) {

            if (quest == null) {
                continue;
            }

            QuestPlayer questPlayer =
                    questPlayerService.getQuestPlayer(
                            player,
                            quest
                    );

            if (questPlayer == null) {
                continue;
            }

            questPlayerService.updateProgress(
                    player,
                    quest,
                    completedGames
            );
        }
    }
}