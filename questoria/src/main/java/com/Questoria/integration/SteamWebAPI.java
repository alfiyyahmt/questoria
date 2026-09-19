package com.Questoria.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SteamWebAPI {

    @Value("${steam.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getOwnedGames(String steamId) {

        String url = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v1/"
                + "?key=" + apiKey
                + "&steamid=" + steamId
                + "&include_appinfo=true"
                + "&include_played_free_games=true";

        return restTemplate.getForObject(url, String.class);
    }

    public String getPlayerSummary(String steamId) {

        String url = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/"
                + "?key=" + apiKey
                + "&steamids=" + steamId;

        return restTemplate.getForObject(url, String.class);
    }
    public String getOwnedGamesJson(String steamId) {

        String url = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v1/"
                + "?key=" + apiKey
                + "&steamid=" + steamId
                + "&include_appinfo=true"
                + "&include_played_free_games=true";

        return restTemplate.getForObject(url, String.class);
    }
}