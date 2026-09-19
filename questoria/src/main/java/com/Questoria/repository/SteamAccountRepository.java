package com.Questoria.repository;

import com.Questoria.model.SteamAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SteamAccountRepository extends JpaRepository<SteamAccount, Long> {

    SteamAccount findBySteamId(String steamId);
}