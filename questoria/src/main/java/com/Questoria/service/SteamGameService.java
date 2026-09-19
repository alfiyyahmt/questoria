package com.Questoria.service;

import com.Questoria.integration.SteamWebAPI;
import com.Questoria.model.BacklogItem;
import com.Questoria.model.Game;
import com.Questoria.model.Player;
import com.Questoria.repository.BacklogRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class SteamGameService {

    private final SteamWebAPI steamWebAPI;
    private final GameService gameService;
    private final BacklogRepository backlogRepository;
    private final PlayerService playerService;

    public SteamGameService(
            SteamWebAPI steamWebAPI,
            GameService gameService,
            BacklogRepository backlogRepository,
            PlayerService playerService) {

        this.steamWebAPI = steamWebAPI;
        this.gameService = gameService;
        this.backlogRepository = backlogRepository;
        this.playerService = playerService;
    }

    public void importGames(String steamId) {

        try {
            String json =
                    steamWebAPI.getOwnedGamesJson(steamId);

            ObjectMapper objectMapper =
                    new ObjectMapper();

            JsonNode root =
                    objectMapper.readTree(json);

            JsonNode games =
                    root.path("response").path("games");

            if (!games.isArray()) {
                System.out.println(
                        "Tidak ada game yang dapat diambil dari Steam."
                );
                return;
            }

            Player player =
                    playerService
                            .getAllPlayers()
                            .stream()
                            .filter(p -> p.getSteamAccount() != null)
                            .filter(p ->
                                    steamId.equals(
                                            p.getSteamAccount()
                                                    .getSteamId()
                                    )
                            )
                            .findFirst()
                            .orElse(null);

            if (player == null) {
                System.out.println(
                        "Player tidak ditemukan untuk Steam ID: "
                                + steamId
                );
                return;
            }

            int importedGames = 0;
            int existingGames = 0;
            int newBacklogItems = 0;

            for (JsonNode gameNode : games) {

                if (!gameNode.has("appid")) {
                    continue;
                }

                Long steamAppId =
                        gameNode.path("appid").asLong();

                String title =
                        gameNode.path("name").asText();

                if (steamAppId == 0 || title.isBlank()) {
                    continue;
                }

                Game game =
                        gameService.getGameBySteamAppId(
                                steamAppId
                        );

                if (game == null) {

                    game = new Game();

                    game.setSteamAppId(
                            steamAppId
                    );

                    game.setTitle(title);

                    game.setCover(
                            "https://cdn.akamai.steamstatic.com/steam/apps/"
                                    + steamAppId
                                    + "/header.jpg"
                    );

                    gameService.saveGame(game);

                    importedGames++;

                } else {

                    game.setTitle(title);

                    if (game.getCover() == null
                            || game.getCover().isBlank()) {

                        game.setCover(
                                "https://cdn.akamai.steamstatic.com/steam/apps/"
                                        + steamAppId
                                        + "/header.jpg"
                        );
                    }

                    gameService.saveGame(game);

                    existingGames++;
                }

                BacklogItem backlogItem =
                        backlogRepository
                                .findByPlayerAndGame(
                                        player,
                                        game
                                )
                                .orElse(null);

                if (backlogItem == null) {

                    backlogItem =
                            new BacklogItem();

                    backlogItem.setPlayer(player);
                    backlogItem.setGame(game);
                    backlogItem.setStatus("BACKLOG");
                    backlogItem.setProgress(0);
                    backlogItem.setNotes("");

                    backlogRepository.save(
                            backlogItem
                    );

                    newBacklogItems++;
                }
            }

            System.out.println(
                    "Import Steam selesai."
            );

            System.out.println(
                    "Game baru: " + importedGames
            );

            System.out.println(
                    "Game sudah ada: " + existingGames
            );

            System.out.println(
                    "Backlog baru: " + newBacklogItems
            );

        } catch (Exception e) {

            System.out.println(
                    "Gagal import game Steam."
            );

            e.printStackTrace();
        }
    }
}