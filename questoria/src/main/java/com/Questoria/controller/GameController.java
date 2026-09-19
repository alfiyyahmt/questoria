package com.Questoria.controller;

import com.Questoria.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/sync-games")
    @ResponseBody
    public String syncGames() {
        return gameService.testSteamAppList();
    }
}