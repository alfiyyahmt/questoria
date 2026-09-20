package com.Questoria.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthenticationService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getSteamLoginUrl() {
        return "https://steamcommunity.com/openid/login"
                + "?openid.ns=http://specs.openid.net/auth/2.0"
                + "&openid.mode=checkid_setup"
                + "&openid.return_to=https://outlet-caution-unbolted.ngrok-free.dev/auth/steam/callback"
                + "&openid.realm=https://outlet-caution-unbolted.ngrok-free.dev/"
                + "&openid.identity=http://specs.openid.net/auth/2.0/identifier_select"
                + "&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select";
    }

    public boolean validateSteamLogin(Map<String, String> parameters) {

        MultiValueMap<String, String> formData =
                new LinkedMultiValueMap<>();

        parameters.forEach(formData::add);

        formData.set("openid.mode", "check_authentication");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(formData, headers);

        String response = restTemplate.postForObject(
                "https://steamcommunity.com/openid/login",
                request,
                String.class
        );

        return response != null
                && response.contains("is_valid:true");
    }
}