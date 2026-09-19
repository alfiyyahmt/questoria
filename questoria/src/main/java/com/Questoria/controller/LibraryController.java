package com.Questoria.controller;

import com.Questoria.model.BacklogItem;
import com.Questoria.model.Player;
import com.Questoria.service.BacklogService;
import com.Questoria.service.PlayerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LibraryController {

    private final BacklogService backlogService;
    private final PlayerService playerService;

    public LibraryController(
            BacklogService backlogService,
            PlayerService playerService) {
        this.backlogService = backlogService;
        this.playerService = playerService;
    }

    @GetMapping("/library")
    public String library(
            Model model,
            HttpSession session) {

        Long playerId = (Long) session.getAttribute("playerId");

        if (playerId == null) {
            return "redirect:/auth/steam";
        }

        Player player = playerService.getPlayerById(playerId);

        if (player == null) {
            return "redirect:/auth/steam";
        }

        List<BacklogItem> backlogItems =
                backlogService.getBacklogByPlayer(player);

        model.addAttribute("backlogItems", backlogItems);
        model.addAttribute("player", player);

        return "library";
    }
}