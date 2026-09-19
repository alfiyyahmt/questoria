package com.Questoria.controller;

import com.Questoria.dto.SteamGameDetail;
import com.Questoria.service.SteamStoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class SteamStoreController {

    private final SteamStoreService steamStoreService;

    public SteamStoreController(
            SteamStoreService steamStoreService) {

        this.steamStoreService =
                steamStoreService;
    }

    @GetMapping("/steam/search")
    @ResponseBody
    public List<Map<String, Object>> searchGames(
            @RequestParam String query) {

        return steamStoreService.searchGames(query);
    }

    @GetMapping("/steam/game")
    public String gameDetails(
            @RequestParam Long appId,
            Model model) {

        SteamGameDetail game =
                steamStoreService.getGameDetails(appId);

        model.addAttribute(
                "game",
                game
        );

        return "game-detail";
    }
}