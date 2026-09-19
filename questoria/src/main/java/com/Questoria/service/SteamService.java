package com.Questoria.service;

import com.Questoria.integration.SteamWebAPI;
import org.springframework.stereotype.Service;

@Service
public class SteamService {

    private final SteamWebAPI steamWebAPI;

    public SteamService(SteamWebAPI steamWebAPI) {
        this.steamWebAPI = steamWebAPI;
    }

    public String getOwnedGames(String steamId) {
        return steamWebAPI.getOwnedGames(steamId);
    }

    public String getPlayerSummary(String steamId) {
        return steamWebAPI.getPlayerSummary(steamId);
    }
}