package com.Questoria.controller;

import com.Questoria.model.Player;
import com.Questoria.service.PlayerService;
import com.Questoria.service.SteamFriendService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FriendController {

    private final PlayerService playerService;
    private final SteamFriendService steamFriendService;

    public FriendController(
            PlayerService playerService,
            SteamFriendService steamFriendService) {

        this.playerService = playerService;
        this.steamFriendService = steamFriendService;
    }

    @GetMapping("/friends")
    public String friends(
            Model model,
            HttpSession session) {

        Long playerId =
                (Long) session.getAttribute("playerId");

        if (playerId == null) {
            return "redirect:/auth/steam";
        }

        Player player =
                playerService.getPlayerById(playerId);

        if (player == null) {
            return "redirect:/auth/steam";
        }

        if (player.getSteamAccount() == null) {
            return "redirect:/profile";
        }

        String steamId =
                player.getSteamAccount().getSteamId();

        model.addAttribute(
                "friends",
                steamFriendService.getFriends(steamId)
        );

        model.addAttribute(
                "player",
                player
        );
        model.addAttribute(
                "activePage", "friends"

        );

        return "friends";
    }
}