package com.Questoria.service;

import com.Questoria.dto.SteamGameDetail;
import com.Questoria.integration.SteamStoreAPI;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SteamStoreService {

    private final SteamStoreAPI steamStoreAPI;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Map<String, Object>> cachedPopularGames =
            new ArrayList<>();

    private long popularGamesCacheTime = 0;

    private static final long CACHE_DURATION =
            10 * 60 * 1000;

    private static final int TARGET_GAME_COUNT = 40;

    public SteamStoreService(SteamStoreAPI steamStoreAPI) {
        this.steamStoreAPI = steamStoreAPI;
    }

    public List<Map<String, Object>> searchGames(String query) {

        List<Map<String, Object>> games =
                new ArrayList<>();

        try {

            String json =
                    steamStoreAPI.searchGames(query);

            if (json == null || json.isBlank()) {
                return games;
            }

            JsonNode root =
                    objectMapper.readTree(json);

            JsonNode items =
                    root.path("items");

            if (!items.isArray()) {
                return games;
            }

            for (JsonNode item : items) {

                if (!"app".equalsIgnoreCase(
                        item.path("type").asText())) {
                    continue;
                }

                long id =
                        item.path("id").asLong();

                String name =
                        item.path("name").asText();

                if (id == 0 || name.isBlank()) {
                    continue;
                }

                if (!isGame(name)) {
                    continue;
                }

                Map<String, Object> game =
                        new HashMap<>();

                game.put(
                        "id",
                        id
                );

                game.put(
                        "name",
                        name
                );

                game.put(
                        "tinyImage",
                        item.path("tiny_image").asText()
                );

                game.put(
                        "metascore",
                        item.path("metascore").asText()
                );

                String headerImage =
                        getSteamHeaderImage(id);

                game.put(
                        "image",
                        headerImage
                );

                game.put(
                        "fallbackImage",
                        getFallbackImage(id)
                );

                games.add(game);
            }

        } catch (Exception e) {

            System.out.println(
                    "Gagal mencari game Steam."
            );

            e.printStackTrace();
        }

        return games;
    }

    public synchronized List<Map<String, Object>> getFeaturedGames() {

        long currentTime =
                System.currentTimeMillis();

        if (!cachedPopularGames.isEmpty()
                && currentTime - popularGamesCacheTime
                < CACHE_DURATION) {

            return cachedPopularGames;
        }

        List<Map<String, Object>> games =
                new ArrayList<>();

        try {

            String html =
                    steamStoreAPI.getMostPlayedGames();

            if (html == null || html.isBlank()) {

                System.out.println(
                        "Steam Most Played tidak mengembalikan data."
                );

                return games;
            }

            Pattern pattern =
                    Pattern.compile(
                            "https://store\\.steampowered\\.com/app/(\\d+)[^\\\"]*\\\"[^>]*>(.*?)</a>",
                            Pattern.CASE_INSENSITIVE
                    );

            Matcher matcher =
                    pattern.matcher(html);

            List<Long> candidateIds =
                    new ArrayList<>();

            List<String> candidateNames =
                    new ArrayList<>();

            while (matcher.find()
                    && candidateIds.size() < 100) {

                Long appId;

                try {

                    appId =
                            Long.parseLong(
                                    matcher.group(1)
                            );

                } catch (Exception e) {

                    continue;
                }

                String rawName =
                        matcher.group(2);

                String name =
                        cleanHtml(rawName);

                if (appId == 0
                        || name.isBlank()) {
                    continue;
                }

                if (!isGame(name)) {
                    continue;
                }

                if (candidateIds.contains(appId)) {
                    continue;
                }

                candidateIds.add(appId);
                candidateNames.add(name);
            }

            for (int i = 0;
                 i < candidateIds.size();
                 i++) {

                if (games.size() >= TARGET_GAME_COUNT) {
                    break;
                }

                Long appId =
                        candidateIds.get(i);

                String fallbackName =
                        candidateNames.get(i);

                SteamGameData steamGame =
                        getSteamGameData(appId);

                if (steamGame == null) {
                    continue;
                }

                if (!"game".equalsIgnoreCase(
                        steamGame.type)) {
                    continue;
                }

                String name =
                        steamGame.name;

                if (name == null
                        || name.isBlank()) {

                    name = fallbackName;
                }

                if (!isGame(name)) {
                    continue;
                }

                String headerImage =
                        steamGame.headerImage;

                if (headerImage == null
                        || headerImage.isBlank()) {

                    headerImage =
                            getSteamHeaderImage(appId);
                }

                String fallbackImage =
                        getFallbackImage(appId);

                Map<String, Object> game =
                        new HashMap<>();

                game.put(
                        "id",
                        appId
                );

                game.put(
                        "name",
                        name
                );

                game.put(
                        "image",
                        headerImage
                );

                game.put(
                        "fallbackImage",
                        fallbackImage
                );

                games.add(game);
            }

            cachedPopularGames =
                    games;

            popularGamesCacheTime =
                    currentTime;

            System.out.println(
                    "Popular Steam games berhasil diambil: "
                            + games.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "Gagal mengambil game populer Steam."
            );

            e.printStackTrace();
        }

        return games;
    }

    private SteamGameData getSteamGameData(
            Long appId) {

        try {

            String json =
                    steamStoreAPI.getGameDetails(appId);

            if (json == null || json.isBlank()) {
                return null;
            }

            JsonNode root =
                    objectMapper.readTree(json);

            JsonNode app =
                    root.path(
                            String.valueOf(appId)
                    );

            if (!app.path("success").asBoolean()) {
                return null;
            }

            JsonNode data =
                    app.path("data");

            String type =
                    data.path("type").asText();

            if (!"game".equalsIgnoreCase(type)) {
                return null;
            }

            String name =
                    data.path("name").asText();

            String headerImage =
                    data.path("header_image").asText();

            return new SteamGameData(
                    type,
                    name,
                    headerImage
            );

        } catch (Exception e) {

            System.out.println(
                    "Gagal mengambil detail Steam AppID: "
                            + appId
            );

            return null;
        }
    }

    private String getSteamHeaderImage(
            Long appId) {

        return "https://shared.akamai.steamstatic.com/steam/apps/"
                + appId
                + "/header.jpg";
    }

    private String getFallbackImage(
            Long appId) {

        return "https://cdn.akamai.steamstatic.com/steam/apps/"
                + appId
                + "/capsule_616x353.jpg";
    }

    private String cleanHtml(
            String html) {

        if (html == null) {
            return "";
        }

        return html
                .replaceAll("<[^>]*>", "")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&#x27;", "'")
                .replace("&#x2F;", "/")
                .trim();
    }

    private boolean isGame(
            String name) {

        if (name == null
                || name.isBlank()) {

            return false;
        }

        String title =
                name.toLowerCase();

        String[] excludedWords = {

                "steam frame",
                "steam frames",

                "profile frame",
                "profile frames",

                "avatar frame",
                "avatar frames",

                "soundtrack",
                "original soundtrack",
                "official soundtrack",
                "ost",

                "music",
                "music pack",

                "dlc",
                "downloadable content",

                "demo",

                "artbook",
                "digital artbook",

                "wallpaper",

                "soundtrack bundle",
                "ost bundle",

                "bundle",
                "season pass",

                "expansion",
                "expansion pass",

                "sound pack",

                "dedicated server",

                "server",

                "sdk",

                "software",

                "editor",

                "tool",

                "tools",

                "benchmark",

                "driver",

                "utility"
        };

        for (String word :
                excludedWords) {

            if (title.contains(word)) {
                return false;
            }
        }

        return true;
    }

    public SteamGameDetail getGameDetails(
            Long appId) {

        SteamGameDetail game =
                new SteamGameDetail();

        try {

            String json =
                    steamStoreAPI.getGameDetails(appId);

            if (json == null
                    || json.isBlank()) {

                return game;
            }

            JsonNode root =
                    objectMapper.readTree(json);

            JsonNode app =
                    root.path(
                            String.valueOf(appId)
                    );

            if (!app.path("success").asBoolean()) {
                return game;
            }

            JsonNode data =
                    app.path("data");

            game.setAppId(
                    data.path(
                            "steam_appid"
                    ).asLong()
            );

            game.setName(
                    data.path(
                            "name"
                    ).asText()
            );

            game.setDescription(
                    data.path(
                            "short_description"
                    ).asText()
            );

            game.setHeaderImage(
                    data.path(
                            "header_image"
                    ).asText()
            );

            JsonNode developers =
                    data.path("developers");

            if (developers.isArray()
                    && developers.size() > 0) {

                game.setDeveloper(
                        developers
                                .get(0)
                                .asText()
                );
            }

            JsonNode genres =
                    data.path("genres");

            if (genres.isArray()
                    && genres.size() > 0) {

                game.setGenre(
                        genres
                                .get(0)
                                .path(
                                        "description"
                                )
                                .asText()
                );
            }

            JsonNode price =
                    data.path(
                            "price_overview"
                    );

            if (!price.isMissingNode()) {

                game.setPrice(
                        price.path(
                                "final_formatted"
                        ).asText()
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Gagal mengambil detail game Steam."
            );

            e.printStackTrace();
        }

        return game;
    }

    private static class SteamGameData {

        private final String type;
        private final String name;
        private final String headerImage;

        public SteamGameData(
                String type,
                String name,
                String headerImage) {

            this.type =
                    type;

            this.name =
                    name;

            this.headerImage =
                    headerImage;
        }
    }
}