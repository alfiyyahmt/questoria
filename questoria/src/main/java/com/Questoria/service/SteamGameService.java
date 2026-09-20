package com.Questoria.service;

import com.Questoria.integration.SteamWebAPI;
import com.Questoria.model.Game;
import com.Questoria.model.Player;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SteamGameService {

    private final SteamWebAPI steamWebAPI;
    private final GameService gameService;
    private final PlayerService playerService;

    public SteamGameService(
            SteamWebAPI steamWebAPI,
            GameService gameService,
            PlayerService playerService) {

        this.steamWebAPI = steamWebAPI;
        this.gameService = gameService;
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

            if (player.getLibraryGames() == null) {

                player.setLibraryGames(
                        new ArrayList<>()
                );
            }

            int importedGames = 0;
            int existingGames = 0;
            int newLibraryGames = 0;

            for (JsonNode gameNode : games) {

                if (!gameNode.has("appid")) {
                    continue;
                }

                Long steamAppId =
                        gameNode.path("appid").asLong();

                String title =
                        gameNode.path("name").asText();

                if (steamAppId == 0
                        || title == null
                        || title.isBlank()) {
                    continue;
                }

                String steamCover =
                        "https://cdn.akamai.steamstatic.com/steam/apps/"
                                + steamAppId
                                + "/header.jpg";

                Game game =
                        gameService.getGameBySteamAppId(
                                steamAppId
                        );

                if (game == null) {

                    game = new Game();

                    game.setSteamAppId(
                            steamAppId
                    );

                    game.setTitle(
                            title
                    );

                    game.setCover(
                            steamCover
                    );

                    gameService.saveGame(
                            game
                    );

                    importedGames++;

                } else {

                    game.setTitle(
                            title
                    );

                    game.setCover(
                            steamCover
                    );

                    gameService.saveGame(
                            game
                    );

                    existingGames++;
                }

                if (!player.getLibraryGames().contains(game)) {

                    player.getLibraryGames().add(game);

                    newLibraryGames++;
                }
            }

            playerService.savePlayer(player);

            System.out.println(
                    "Import Steam selesai."
            );

            System.out.println(
                    "Game baru: "
                            + importedGames
            );

            System.out.println(
                    "Game sudah ada: "
                            + existingGames
            );

            System.out.println(
                    "Game baru masuk Library: "
                            + newLibraryGames
            );

        } catch (Exception e) {

            System.out.println(
                    "Gagal import game Steam."
            );

            e.printStackTrace();
        }
    }
}