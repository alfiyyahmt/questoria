package com.Questoria.controller;

import com.Questoria.model.BacklogItem;
import com.Questoria.model.Game;
import com.Questoria.model.Player;
import com.Questoria.model.Review;
import com.Questoria.service.BacklogService;
import com.Questoria.service.PlayerService;
import com.Questoria.service.ReviewService;
import com.Questoria.service.SteamGameService;
import com.Questoria.service.SteamStoreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LibraryController {

    private final BacklogService backlogService;
    private final PlayerService playerService;
    private final SteamGameService steamGameService;
    private final SteamStoreService steamStoreService;
    private final ReviewService reviewService;

    public LibraryController(
            BacklogService backlogService,
            PlayerService playerService,
            SteamGameService steamGameService,
            SteamStoreService steamStoreService,
            ReviewService reviewService) {

        this.backlogService = backlogService;
        this.playerService = playerService;
        this.steamGameService = steamGameService;
        this.steamStoreService = steamStoreService;
        this.reviewService = reviewService;
    }

    @GetMapping("/library")
    public String library(Model model, HttpSession session) {

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

        updateGameCovers(backlogItems);

        Map<Long, Review> userReviews = new HashMap<>();

        for (BacklogItem item : backlogItems) {

            if (item == null || item.getGame() == null) {
                continue;
            }

            Review review =
                    reviewService.getReviewByPlayerAndGame(
                            player,
                            item.getGame()
                    );

            if (review != null) {
                userReviews.put(
                        item.getGame().getId(),
                        review
                );
            }
        }

        model.addAttribute("backlogItems", backlogItems);
        model.addAttribute("player", player);
        model.addAttribute("userReviews", userReviews);
        model.addAttribute("isLoggedIn", true);

        return "library";
    }

    private void updateGameCovers(List<BacklogItem> backlogItems) {

        if (backlogItems == null || backlogItems.isEmpty()) {
            return;
        }

        for (BacklogItem item : backlogItems) {

            if (item == null) {
                continue;
            }

            Game game = item.getGame();

            if (game == null) {
                continue;
            }

            Long steamAppId = game.getSteamAppId();

            if (steamAppId == null || steamAppId == 0) {
                continue;
            }

            try {

                String steamImage =
                        getSteamHeaderImage(steamAppId);

                if (steamImage != null && !steamImage.isBlank()) {
                    game.setCover(steamImage);
                }

            } catch (Exception e) {

                System.out.println(
                        "Gagal memperbarui cover game: "
                                + game.getTitle()
                );

            }
        }
    }

    private String getSteamHeaderImage(Long steamAppId) {

        try {

            var gameDetail =
                    steamStoreService.getGameDetails(steamAppId);

            if (gameDetail != null) {

                String headerImage =
                        gameDetail.getHeaderImage();

                if (headerImage != null && !headerImage.isBlank()) {
                    return headerImage;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Gagal mengambil gambar Steam untuk AppID: "
                            + steamAppId
            );

        }

        return "https://shared.akamai.steamstatic.com/steam/apps/"
                + steamAppId
                + "/header.jpg";
    }

    @PostMapping("/library/import")
    public String importLibrary(HttpSession session) {

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
            return "redirect:/library";
        }

        String steamId =
                player.getSteamAccount().getSteamId();

        steamGameService.importGames(steamId);

        return "redirect:/library";
    }
}