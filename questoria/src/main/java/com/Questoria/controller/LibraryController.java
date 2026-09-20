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
import com.Questoria.service.GameService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class LibraryController {

    private final PlayerService playerService;
    private final SteamGameService steamGameService;
    private final SteamStoreService steamStoreService;
    private final ReviewService reviewService;
    private final BacklogService backlogService;
    private final GameService gameService;

    public LibraryController(
            PlayerService playerService,
            SteamGameService steamGameService,
            SteamStoreService steamStoreService,
            ReviewService reviewService,
            BacklogService backlogService,
            GameService gameService) {

        this.playerService = playerService;
        this.steamGameService = steamGameService;
        this.steamStoreService = steamStoreService;
        this.reviewService = reviewService;
        this.backlogService = backlogService;
        this.gameService = gameService;
    }

    @GetMapping("/library")
    public String library(
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

        List<Game> libraryGames =
                player.getLibraryGames();

        if (libraryGames == null) {
            libraryGames = new ArrayList<>();
        }

        updateGameCovers(libraryGames);

        Map<Long, Review> userReviews =
                new HashMap<>();

        for (Game game : libraryGames) {

            if (game == null) {
                continue;
            }

            Review review =
                    reviewService.getReviewByPlayerAndGame(
                            player,
                            game
                    );

            if (review != null) {

                userReviews.put(
                        game.getId(),
                        review
                );
            }
        }

        Set<Long> backlogGameIds =
                new HashSet<>();

        for (Game game : libraryGames) {

            if (game == null) {
                continue;
            }

            BacklogItem backlogItem =
                    backlogService.getBacklogItem(
                            player,
                            game
                    );

            if (backlogItem != null) {

                backlogGameIds.add(
                        game.getId()
                );
            }
        }

        model.addAttribute(
                "libraryGames",
                libraryGames
        );

        model.addAttribute(
                "player",
                player
        );

        model.addAttribute(
                "userReviews",
                userReviews
        );

        model.addAttribute(
                "backlogGameIds",
                backlogGameIds
        );

        model.addAttribute(
                "isLoggedIn",
                true
        );

        return "library";
    }

    private void updateGameCovers(
            List<Game> libraryGames) {

        if (libraryGames == null
                || libraryGames.isEmpty()) {

            return;
        }

        for (Game game : libraryGames) {

            if (game == null) {
                continue;
            }

            Long steamAppId =
                    game.getSteamAppId();

            if (steamAppId == null
                    || steamAppId == 0) {

                continue;
            }

            try {

                String steamImage =
                        getSteamHeaderImage(
                                steamAppId
                        );

                if (steamImage != null
                        && !steamImage.isBlank()) {

                    game.setCover(
                            steamImage
                    );
                }

            } catch (Exception e) {

                System.out.println(
                        "Gagal memperbarui cover game: "
                                + game.getTitle()
                );
            }
        }
    }

    private String getSteamHeaderImage(
            Long steamAppId) {

        try {

            var gameDetail =
                    steamStoreService.getGameDetails(
                            steamAppId
                    );

            if (gameDetail != null) {

                String headerImage =
                        gameDetail.getHeaderImage();

                if (headerImage != null
                        && !headerImage.isBlank()) {

                    return headerImage;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Gagal mengambil gambar Steam untuk AppID: "
                            + steamAppId
            );
        }

        return "https://cdn.akamai.steamstatic.com/steam/apps/"
                + steamAppId
                + "/header.jpg";
    }

    @PostMapping("/library/import")
    public String importLibrary(
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
            return "redirect:/library";
        }

        String steamId =
                player.getSteamAccount().getSteamId();

        steamGameService.importGames(
                steamId
        );

        return "redirect:/library";
    }

    @PostMapping("/library/add-to-backlog")
    public String addToBacklog(
            @RequestParam Long gameId,
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

        Game game =
                gameService
                        .getGameBySteamAppId(gameId);

        if (game == null) {

            game =
                    gameService
                            .getAllGames()
                            .stream()
                            .filter(g ->
                                    g.getId() != null
                                            && g.getId().equals(gameId)
                            )
                            .findFirst()
                            .orElse(null);
        }

        if (game == null) {
            return "redirect:/library";
        }

        if (player.getLibraryGames() == null
                || !player.getLibraryGames().contains(game)) {

            return "redirect:/library";
        }

        BacklogItem existing =
                backlogService.getBacklogItem(
                        player,
                        game
                );

        if (existing == null) {

            BacklogItem backlogItem =
                    new BacklogItem();

            backlogItem.setPlayer(
                    player
            );

            backlogItem.setGame(
                    game
            );

            backlogItem.setStatus(
                    "PLAYING"
            );

            backlogItem.setProgress(
                    0
            );

            backlogItem.setNotes(
                    ""
            );

            backlogService.saveBacklogItem(
                    backlogItem
            );
        }

        return "redirect:/library";
    }
}