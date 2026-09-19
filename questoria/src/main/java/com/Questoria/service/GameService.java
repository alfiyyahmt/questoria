package com.Questoria.service;

import com.Questoria.model.Game;
import com.Questoria.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGameBySteamAppId(Long steamAppId) {
        return gameRepository.findBySteamAppId(steamAppId).orElse(null);
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }
}