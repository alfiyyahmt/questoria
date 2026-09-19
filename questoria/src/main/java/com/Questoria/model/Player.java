package com.Questoria.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String displayName;

    private String role;

    @OneToOne
    private SteamAccount steamAccount;

    @OneToMany(mappedBy = "player")
    private List<BacklogItem> backlogItems;

    @OneToMany(mappedBy = "player")
    private List<QuestPlayer> questPlayers;

    public Player() {}

    public Long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public SteamAccount getSteamAccount() {
        return steamAccount;
    }

    public void setSteamAccount(SteamAccount steamAccount) {
        this.steamAccount = steamAccount;
    }

    public List<BacklogItem> getBacklogItems() {
        return backlogItems;
    }

    public void setBacklogItems(List<BacklogItem> backlogItems) {
        this.backlogItems = backlogItems;
    }

    public List<QuestPlayer> getQuestPlayers() {
        return questPlayers;
    }

    public void setQuestPlayers(List<QuestPlayer> questPlayers) {
        this.questPlayers = questPlayers;
    }

    @OneToMany(mappedBy = "player")
    private List<Review> reviews;
    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}