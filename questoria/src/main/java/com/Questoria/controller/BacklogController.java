package com.Questoria.controller;

import com.Questoria.model.BacklogItem;
import com.Questoria.model.Game;
import com.Questoria.model.Player;
import com.Questoria.service.BacklogService;
import com.Questoria.service.GameService;
import com.Questoria.service.PlayerService;
import com.Questoria.service.SteamStoreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BacklogController {

    private final BacklogService backlogService;
    private final GameService gameService;
    private final PlayerService playerService;
    private final SteamStoreService steamStoreService;

    public BacklogController(
            BacklogService backlogService,
            GameService gameService,
            PlayerService playerService,
            SteamStoreService steamStoreService) {

        this.backlogService = backlogService;
        this.gameService = gameService;
        this.playerService = playerService;
        this.steamStoreService = steamStoreService;
    }

    @GetMapping("/backlog")
    public String backlog(
            @RequestParam(required = false) String status,
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

        List<BacklogItem> backlogItems;

        if (status == null || status.isBlank()) {

            backlogItems =
                    backlogService.getBacklogByPlayer(
                            player
                    );

        } else {

            backlogItems =
                    backlogService.getBacklogByPlayerAndStatus(
                            player,
                            status.toUpperCase()
                    );
        }

        updateGameCovers(backlogItems);

        model.addAttribute(
                "backlogItems",
                backlogItems
        );

        model.addAttribute(
                "selectedStatus",
                status
        );

        model.addAttribute(
                "isLoggedIn",
                true
        );

        return "backlog";
    }

    private void updateGameCovers(
            List<BacklogItem> backlogItems) {

        if (backlogItems == null
                || backlogItems.isEmpty()) {

            return;
        }

        for (BacklogItem item : backlogItems) {

            if (item == null) {
                continue;
            }

            Game game =
                    item.getGame();

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

                if (steamImage == null
                        || steamImage.isBlank()) {

                    continue;
                }

                game.setCover(
                        steamImage
                );

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

        return "https://shared.akamai.steamstatic.com/steam/apps/"
                + steamAppId
                + "/header.jpg";
    }

    @PostMapping("/backlog/status")
    public String updateStatus(
            @RequestParam Long id,
            @RequestParam String status,
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

        BacklogItem backlogItem =
                backlogService.getBacklogItemByIdAndPlayer(
                        id,
                        player
                );

        if (backlogItem != null) {

            backlogItem.setStatus(
                    status.toUpperCase()
            );

            backlogService.saveBacklogItem(
                    backlogItem
            );
        }

        return "redirect:/backlog";
    }

    @PostMapping("/backlog/notes")
    public String updateNotes(
            @RequestParam Long id,
            @RequestParam String notes,
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

        BacklogItem backlogItem =
                backlogService.getBacklogItemByIdAndPlayer(
                        id,
                        player
                );

        if (backlogItem != null) {

            backlogItem.setNotes(notes);

            backlogService.saveBacklogItem(
                    backlogItem
            );
        }

        return "redirect:/backlog";
    }

    @PostMapping("/backlog/delete")
    public String deleteFromBacklog(
            @RequestParam Long id,
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

        BacklogItem backlogItem =
                backlogService.getBacklogItemByIdAndPlayer(
                        id,
                        player
                );

        if (backlogItem != null) {

            backlogService.deleteBacklogItem(
                    backlogItem
            );
        }

        return "redirect:/backlog";
    }
}