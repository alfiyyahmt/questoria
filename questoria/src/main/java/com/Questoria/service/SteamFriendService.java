package com.Questoria.service;

import com.Questoria.integration.SteamWebAPI;
import com.Questoria.model.SteamFriend;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SteamFriendService {

    private final SteamWebAPI steamWebAPI;

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    public SteamFriendService(
            SteamWebAPI steamWebAPI) {

        this.steamWebAPI = steamWebAPI;
    }

    public List<SteamFriend> getFriends(String steamId) {

        List<SteamFriend> friends =
                new ArrayList<>();

        try {

            String friendListJson =
                    steamWebAPI.getFriendList(steamId);

            JsonNode root =
                    objectMapper.readTree(friendListJson);

            JsonNode friendNodes =
                    root.path("friendslist")
                            .path("friends");

            if (!friendNodes.isArray()) {
                return friends;
            }

            List<String> steamIds =
                    new ArrayList<>();

            for (JsonNode friendNode : friendNodes) {

                String friendSteamId =
                        friendNode.path("steamid").asText();

                if (!friendSteamId.isBlank()) {
                    steamIds.add(friendSteamId);
                }
            }

            if (steamIds.isEmpty()) {
                return friends;
            }

            for (int start = 0;
                 start < steamIds.size();
                 start += 100) {

                int end =
                        Math.min(
                                start + 100,
                                steamIds.size()
                        );

                List<String> batch =
                        steamIds.subList(start, end);

                String steamIdsParameter =
                        String.join(",", batch);

                String summariesJson =
                        steamWebAPI.getPlayerSummaries(
                                steamIdsParameter
                        );

                JsonNode summariesRoot =
                        objectMapper.readTree(
                                summariesJson
                        );

                JsonNode players =
                        summariesRoot.path("response")
                                .path("players");

                if (!players.isArray()) {
                    continue;
                }

                for (JsonNode playerNode : players) {

                    SteamFriend friend =
                            new SteamFriend();

                    friend.setSteamId(
                            playerNode
                                    .path("steamid")
                                    .asText()
                    );

                    friend.setPersonaName(
                            playerNode
                                    .path("personaname")
                                    .asText()
                    );

                    friend.setProfileUrl(
                            playerNode
                                    .path("profileurl")
                                    .asText()
                    );

                    friend.setAvatar(
                            playerNode
                                    .path("avatar")
                                    .asText()
                    );

                    friend.setAvatarMedium(
                            playerNode
                                    .path("avatarmedium")
                                    .asText()
                    );

                    friend.setAvatarFull(
                            playerNode
                                    .path("avatarfull")
                                    .asText()
                    );

                    friends.add(friend);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Gagal mengambil daftar teman Steam."
            );

            e.printStackTrace();
        }

        return friends;
    }
}