package com.Questoria.controller;

import com.Questoria.model.Player;
import com.Questoria.model.SteamAccount;
import com.Questoria.service.AuthenticationService;
import com.Questoria.service.PlayerService;
import com.Questoria.service.SteamAccountService;
import com.Questoria.service.SteamGameService;
import com.Questoria.service.SteamService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final SteamAccountService steamAccountService;
    private final PlayerService playerService;
    private final SteamService steamService;
    private final SteamGameService steamGameService;

    public AuthenticationController(
            AuthenticationService authenticationService,
            SteamAccountService steamAccountService,
            PlayerService playerService,
            SteamService steamService,
            SteamGameService steamGameService) {

        this.authenticationService = authenticationService;
        this.steamAccountService = steamAccountService;
        this.playerService = playerService;
        this.steamService = steamService;
        this.steamGameService = steamGameService;
    }

    @GetMapping("/auth/steam")
    public RedirectView steamLogin() {
        return new RedirectView(
                authenticationService.getSteamLoginUrl()
        );
    }

    @GetMapping("/auth/steam/callback")
    public String steamCallback(
            @RequestParam Map<String, String> parameters,
            HttpSession session) {

        boolean valid =
                authenticationService.validateSteamLogin(parameters);

        if (!valid) {
            System.out.println("Steam OpenID validation gagal.");
            return "redirect:/auth/steam";
        }

        String claimedId =
                parameters.get("openid.claimed_id");

        if (claimedId == null || claimedId.isBlank()) {
            return "redirect:/auth/steam";
        }

        String steamId =
                claimedId.substring(
                        claimedId.lastIndexOf("/") + 1
                );

        SteamAccount steamAccount =
                steamAccountService.getSteamAccountBySteamId(steamId);

        if (steamAccount == null) {
            steamAccount = new SteamAccount();
            steamAccount.setSteamId(steamId);
        }

        try {
            String profileJson =
                    steamService.getPlayerSummary(steamId);

            ObjectMapper objectMapper =
                    new ObjectMapper();

            JsonNode root =
                    objectMapper.readTree(profileJson);

            JsonNode steamPlayer =
                    root.path("response")
                            .path("players")
                            .path(0);

            if (!steamPlayer.isMissingNode()) {

                steamAccount.setUsername(
                        steamPlayer
                                .path("personaname")
                                .asText()
                );

                steamAccount.setProfileUrl(
                        steamPlayer
                                .path("profileurl")
                                .asText()
                );
                steamAccount.setAvatarUrl(
                        steamPlayer
                                .path("avatarfull")
                                .asText()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        steamAccountService.saveSteamAccount(
                steamAccount
        );

        Player player =
                playerService
                        .getPlayerBySteamAccount(
                                steamAccount
                        );

        if (player == null) {
            player = new Player();
            player.setSteamAccount(steamAccount);
            player.setRole("USER");
        }

        player.setDisplayName(
                steamAccount.getUsername()
        );

        playerService.savePlayer(player);


        session.setAttribute(
                "steamId",
                steamId
        );

        session.setAttribute(
                "playerId",
                player.getId()
        );

        System.out.println(
                "STEAM ID: " + steamId
        );

        System.out.println(
                "USERNAME: " +
                        steamAccount.getUsername()
        );

        System.out.println(
                "PLAYER ID: " +
                        player.getId()
        );

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}