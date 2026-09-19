package com.Questoria.controller;

import com.Questoria.model.BacklogItem;
import com.Questoria.model.Game;
import com.Questoria.model.Player;
import com.Questoria.repository.BacklogRepository;
import com.Questoria.service.GameService;
import com.Questoria.service.PlayerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BacklogAddController {

    private final BacklogRepository backlogRepository;
    private final GameService gameService;
    private final PlayerService playerService;

    public BacklogAddController(
            BacklogRepository backlogRepository,
            GameService gameService,
            PlayerService playerService) {

        this.backlogRepository = backlogRepository;
        this.gameService = gameService;
        this.playerService = playerService;
    }

    @PostMapping("/backlog/add")
    public String addToBacklog(
            @RequestParam("appId") Long appId,
            @RequestParam("name") String name,
            @RequestParam(value = "headerImage", required = false) String headerImage,
            @RequestParam(value = "developer", required = false) String developer,
            @RequestParam(value = "genre", required = false) String genre,
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
                gameService.getGameBySteamAppId(appId);

        if (game == null) {

            game = new Game();

            game.setSteamAppId(appId);

            game.setTitle(name);

            game.setDeveloper(
                    developer != null
                            ? developer
                            : ""
            );

            game.setGenre(
                    genre != null
                            ? genre
                            : ""
            );

            game.setCover(
                    headerImage != null
                            && !headerImage.isBlank()
                            ? headerImage
                            : "https://cdn.akamai.steamstatic.com/steam/apps/"
                              + appId
                              + "/header.jpg"
            );

            gameService.saveGame(game);
        }

        BacklogItem existing =
                backlogRepository
                        .findByPlayerAndGame(
                                player,
                                game
                        )
                        .orElse(null);

        if (existing == null) {

            BacklogItem backlogItem =
                    new BacklogItem();

            backlogItem.setPlayer(player);

            backlogItem.setGame(game);

            backlogItem.setStatus("BACKLOG");

            backlogItem.setProgress(0);

            backlogItem.setNotes("");

            backlogRepository.save(backlogItem);
        }

        return "redirect:/backlog";
    }
}