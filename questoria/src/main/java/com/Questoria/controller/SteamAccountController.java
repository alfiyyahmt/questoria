package com.Questoria.controller;

import com.Questoria.model.SteamAccount;
import com.Questoria.service.SteamAccountService;
import com.Questoria.service.SteamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SteamAccountController {

    private final SteamAccountService steamAccountService;
    private final SteamService steamService;

    public SteamAccountController(SteamAccountService steamAccountService, SteamService steamService) {
        this.steamAccountService = steamAccountService;
        this.steamService = steamService;
    }

    @GetMapping("/steam-accounts/{id}")
    public String steamAccount(@PathVariable Long id, Model model) {
        SteamAccount steamAccount = steamAccountService.getSteamAccountById(id);
        model.addAttribute("steamAccount", steamAccount);

        return "steam-account";
    }

    @GetMapping("/steam/test/{steamId}")
    @ResponseBody
    public String testSteamApi(@PathVariable String steamId) {
        return steamService.getOwnedGames(steamId);
    }
    @GetMapping("/steam/test-profile/{steamId}")
    @ResponseBody
    public String testSteamProfile(@PathVariable String steamId) {
        return steamService.getPlayerSummary(steamId);
    }
}