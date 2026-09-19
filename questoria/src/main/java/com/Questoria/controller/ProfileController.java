package com.Questoria.controller;

import com.Questoria.model.Player;
import com.Questoria.service.PlayerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    private final PlayerService playerService;

    public ProfileController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/profile")
    public String profile(
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

        model.addAttribute("player", player);

        return "profile";
    }
}