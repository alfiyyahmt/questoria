package com.Questoria.controller;

import com.Questoria.model.BacklogItem;
import com.Questoria.model.Game;
import com.Questoria.model.Player;
import com.Questoria.service.BacklogService;
import com.Questoria.service.GameService;
import com.Questoria.service.PlayerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BacklogController {

    private final BacklogService backlogService;
    private final GameService gameService;
    private final PlayerService playerService;

    public BacklogController(
            BacklogService backlogService,
            GameService gameService,
            PlayerService playerService) {
        this.backlogService = backlogService;
        this.gameService = gameService;
        this.playerService = playerService;
    }

    @GetMapping("/backlog")
    public String backlog(
            @RequestParam(required = false) String status,
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

        if (status == null || status.isBlank()) {
            model.addAttribute(
                    "backlogItems",
                    backlogService.getBacklogByPlayer(player)
            );
        } else {
            model.addAttribute(
                    "backlogItems",
                    backlogService.getBacklogByPlayerAndStatus(
                            player,
                            status.toUpperCase()
                    )
            );
        }

        model.addAttribute("selectedStatus", status);

        return "backlog";
    }

    @PostMapping("/backlog/status")
    public String updateStatus(
            @RequestParam Long id,
            @RequestParam String status,
            HttpSession session) {

        Long playerId = (Long) session.getAttribute("playerId");

        if (playerId == null) {
            return "redirect:/auth/steam";
        }

        Player player = playerService.getPlayerById(playerId);

        if (player == null) {
            return "redirect:/auth/steam";
        }

        BacklogItem backlogItem =
                backlogService.getBacklogItemByIdAndPlayer(id, player);

        if (backlogItem != null) {
            backlogItem.setStatus(status.toUpperCase());
            backlogService.saveBacklogItem(backlogItem);
        }

        return "redirect:/backlog";
    }

    @PostMapping("/backlog/progress")
    public String updateProgress(
            @RequestParam Long id,
            @RequestParam int progress,
            HttpSession session) {

        Long playerId = (Long) session.getAttribute("playerId");

        if (playerId == null) {
            return "redirect:/auth/steam";
        }

        Player player = playerService.getPlayerById(playerId);

        if (player == null) {
            return "redirect:/auth/steam";
        }

        BacklogItem backlogItem =
                backlogService.getBacklogItemByIdAndPlayer(id, player);

        if (backlogItem != null) {

            if (progress < 0) {
                progress = 0;
            }

            if (progress > 100) {
                progress = 100;
            }

            backlogItem.setProgress(progress);
            backlogService.saveBacklogItem(backlogItem);
        }

        return "redirect:/backlog";
    }

    @PostMapping("/backlog/notes")
    public String updateNotes(
            @RequestParam Long id,
            @RequestParam String notes,
            HttpSession session) {

        Long playerId = (Long) session.getAttribute("playerId");

        if (playerId == null) {
            return "redirect:/auth/steam";
        }

        Player player = playerService.getPlayerById(playerId);

        if (player == null) {
            return "redirect:/auth/steam";
        }

        BacklogItem backlogItem =
                backlogService.getBacklogItemByIdAndPlayer(id, player);

        if (backlogItem != null) {
            backlogItem.setNotes(notes);
            backlogService.saveBacklogItem(backlogItem);
        }

        return "redirect:/backlog";
    }
}