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
public class DashboardController {

    private final BacklogService backlogService;
    private final PlayerService playerService;

    public DashboardController(
            BacklogService backlogService,
            PlayerService playerService) {
        this.backlogService = backlogService;
        this.playerService = playerService;
    }

    @GetMapping("/")
    public String dashboard(
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

        long totalGames = backlogItems.size();

        long playingCount = backlogItems.stream()
                .filter(item -> "PLAYING".equalsIgnoreCase(item.getStatus()))
                .count();

        long completedCount = backlogItems.stream()
                .filter(item -> "COMPLETED".equalsIgnoreCase(item.getStatus()))
                .count();

        long backlogCount = backlogItems.stream()
                .filter(item ->
                        !"PLAYING".equalsIgnoreCase(item.getStatus())
                                && !"COMPLETED".equalsIgnoreCase(item.getStatus())
                                && !"ON HOLD".equalsIgnoreCase(item.getStatus())
                                && !"DROPPED".equalsIgnoreCase(item.getStatus())
                )
                .count();

        model.addAttribute("player", player);
        model.addAttribute("totalGames", totalGames);
        model.addAttribute("backlogCount", backlogCount);
        model.addAttribute("playingCount", playingCount);
        model.addAttribute("completedCount", completedCount);

        model.addAttribute(
                "recentGames",
                backlogItems.stream()
                        .limit(5)
                        .toList()
        );

        return "dashboard";
    }
}