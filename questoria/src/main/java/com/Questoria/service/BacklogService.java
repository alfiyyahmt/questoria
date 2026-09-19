package com.Questoria.service;

import com.Questoria.model.BacklogItem;
import com.Questoria.model.Game;
import com.Questoria.model.Player;
import com.Questoria.repository.BacklogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BacklogService {

    private final BacklogRepository backlogRepository;

    public BacklogService(BacklogRepository backlogRepository) {
        this.backlogRepository = backlogRepository;
    }

    public List<BacklogItem> getAllBacklogItems() {
        return backlogRepository.findAll();
    }

    public List<BacklogItem> getBacklogByPlayer(Player player) {
        return backlogRepository.findByPlayer(player);
    }

    public List<BacklogItem> getBacklogByPlayerAndStatus(Player player, String status) {
        return backlogRepository.findByPlayerAndStatus(player, status);
    }

    public BacklogItem getBacklogItemByIdAndPlayer(Long id, Player player) {
        return backlogRepository.findByIdAndPlayer(id, player).orElse(null);
    }

    public BacklogItem saveBacklogItem(BacklogItem backlogItem) {
        return backlogRepository.save(backlogItem);
    }

    public BacklogItem getBacklogItem(Player player, Game game) {
        return backlogRepository.findByPlayerAndGame(player, game).orElse(null);
    }
}