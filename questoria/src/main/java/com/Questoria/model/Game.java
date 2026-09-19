package com.Questoria.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long steamAppId;
    private String title;
    private String developer;
    private String genre;
    private String cover;

    @OneToMany(mappedBy = "game")
    private List<BacklogItem> backlogItems;

    public Game() {
    }

    public Long getId() {
        return id;
    }

    public Long getSteamAppId() {
        return steamAppId;
    }

    public void setSteamAppId(Long steamAppId) {
        this.steamAppId = steamAppId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<BacklogItem> getBacklogItems() {
        return backlogItems;
    }

    public void setBacklogItems(List<BacklogItem> backlogItems) {
        this.backlogItems = backlogItems;
    }
}