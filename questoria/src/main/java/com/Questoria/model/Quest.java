package com.Questoria.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private int targetProgress;
    private boolean active;

    @OneToMany(mappedBy = "quest")
    private List<QuestPlayer> questPlayers;

    public Quest() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTargetProgress() {
        return targetProgress;
    }

    public void setTargetProgress(int targetProgress) {
        this.targetProgress = targetProgress;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<QuestPlayer> getQuestPlayers() {
        return questPlayers;
    }

    public void setQuestPlayers(List<QuestPlayer> questPlayers) {
        this.questPlayers = questPlayers;
    }
}