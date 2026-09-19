package com.Questoria.service;

import com.Questoria.integration.SteamWebAPI;
import com.Questoria.model.Game;
import com.Questoria.repository.GameRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final SteamWebAPI steamWebAPI;

    public GameService(
            GameRepository gameRepository,
            SteamWebAPI steamWebAPI) {

        this.gameRepository = gameRepository;
        this.steamWebAPI = steamWebAPI;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGameBySteamAppId(Long steamAppId) {
        return gameRepository
                .findBySteamAppId(steamAppId)
                .orElse(null);
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public int syncSteamGames() {

        int newGames = 0;

        try {

            String json =
                    steamWebAPI.getSteamAppList();

            ObjectMapper objectMapper =
                    new ObjectMapper();

            JsonNode root =
                    objectMapper.readTree(json);

            JsonNode apps =
                    root.path("applist")
                            .path("apps");

            if (!apps.isArray()) {
                System.out.println(
                        "Daftar game Steam tidak ditemukan."
                );
                return 0;
            }

            for (JsonNode app : apps) {

                if (!app.has("appid")
                        || !app.has("name")) {
                    continue;
                }

                Long steamAppId =
                        app.path("appid").asLong();

                String title =
                        app.path("name").asText();

                if (steamAppId == 0
                        || title == null
                        || title.isBlank()) {
                    continue;
                }

                Game existingGame =
                        getGameBySteamAppId(
                                steamAppId
                        );

                if (existingGame != null) {
                    continue;
                }

                Game game =
                        new Game();

                game.setSteamAppId(
                        steamAppId
                );

                game.setTitle(
                        title
                );

                game.setCover(
                        "https://cdn.akamai.steamstatic.com/steam/apps/"
                                + steamAppId
                                + "/header.jpg"
                );

                saveGame(game);

                newGames++;
            }

            System.out.println(
                    "Steam catalog sync selesai."
            );

            System.out.println(
                    "Game baru ditambahkan: "
                            + newGames
            );

        } catch (Exception e) {

            System.out.println(
                    "Gagal melakukan sync katalog Steam."
            );

            e.printStackTrace();
        }

        return newGames;
    }

    public String testSteamAppList() {

        try {

            String json =
                    steamWebAPI.getSteamAppList();

            System.out.println(
                    "PANJANG JSON: "
                            + json.length()
            );

            System.out.println(
                    "AWAL JSON: "
                            + json.substring(
                            0,
                            Math.min(
                                    500,
                                    json.length()
                            )
                    )
            );

            return json.substring(
                    0,
                    Math.min(
                            2000,
                            json.length()
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

            return "ERROR: "
                    + e.getMessage();
        }
    }
}