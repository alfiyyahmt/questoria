package com.Questoria.service;

import com.Questoria.model.Player;
import com.Questoria.model.SteamAccount;
import com.Questoria.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    public Player getPlayerBySteamAccount(SteamAccount steamAccount) {
        return playerRepository.findBySteamAccount(steamAccount).orElse(null);
    }
}