package com.Questoria.service;

import com.Questoria.model.SteamAccount;
import com.Questoria.repository.SteamAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class SteamAccountService {

    private final SteamAccountRepository steamAccountRepository;

    public SteamAccountService(SteamAccountRepository steamAccountRepository) {
        this.steamAccountRepository = steamAccountRepository;
    }

    public SteamAccount getSteamAccountBySteamId(String steamId) {
        return steamAccountRepository.findBySteamId(steamId);
    }

    public SteamAccount saveSteamAccount(SteamAccount steamAccount) {
        return steamAccountRepository.save(steamAccount);
    }

    public SteamAccount getSteamAccountById(Long id) {
        return steamAccountRepository.findById(id).orElse(null);
    }
}