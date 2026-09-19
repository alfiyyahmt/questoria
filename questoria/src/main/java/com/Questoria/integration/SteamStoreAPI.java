package com.Questoria.integration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SteamStoreAPI {

    private final RestTemplate restTemplate = new RestTemplate();

    public String searchGames(String query) {

        String url =
                "https://store.steampowered.com/api/storesearch/"
                        + "?term=" + query
                        + "&l=english"
                        + "&cc=us";

        return restTemplate.getForObject(
                url,
                String.class
        );
    }

    public String getGameDetails(Long appId) {

        String url =
                "https://store.steampowered.com/api/appdetails/"
                        + "?appids=" + appId
                        + "&l=english"
                        + "&cc=us";

        return restTemplate.getForObject(
                url,
                String.class
        );
    }

    public String getFeaturedGames() {

        String url =
                "https://store.steampowered.com/api/featured/"
                        + "?l=english"
                        + "&cc=us";

        return restTemplate.getForObject(
                url,
                String.class
        );
    }

    public String getFeaturedCategories() {

        String url =
                "https://store.steampowered.com/api/featuredcategories/"
                        + "?l=english"
                        + "&cc=us";

        return restTemplate.getForObject(
                url,
                String.class
        );
    }

    public String getMostPlayedGames() {

        String url =
                "https://store.steampowered.com/charts/mostplayed/";

        return restTemplate.getForObject(
                url,
                String.class
        );
    }
}