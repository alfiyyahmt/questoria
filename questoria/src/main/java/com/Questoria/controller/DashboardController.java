package com.Questoria.controller;

import com.Questoria.dto.DashboardQuest;
import com.Questoria.model.BacklogItem;
import com.Questoria.model.Player;
import com.Questoria.model.Quest;
import com.Questoria.model.QuestPlayer;
import com.Questoria.service.BacklogService;
import com.Questoria.service.PlayerService;
import com.Questoria.service.QuestPlayerService;
import com.Questoria.service.QuestService;
import com.Questoria.service.SteamStoreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final BacklogService backlogService;
    private final PlayerService playerService;
    private final SteamStoreService steamStoreService;
    private final QuestService questService;
    private final QuestPlayerService questPlayerService;

    public DashboardController(
            BacklogService backlogService,
            PlayerService playerService,
            SteamStoreService steamStoreService,
            QuestService questService,
            QuestPlayerService questPlayerService) {

        this.backlogService = backlogService;
        this.playerService = playerService;
        this.steamStoreService = steamStoreService;
        this.questService = questService;
        this.questPlayerService = questPlayerService;
    }

    @GetMapping("/")
    public String dashboard(
            @RequestParam(required = false) String search,
            Model model,
            HttpSession session) {

        Long playerId =
                (Long) session.getAttribute("playerId");

        if (playerId == null) {

            model.addAttribute(
                    "isLoggedIn",
                    false
            );

            model.addAttribute(
                    "featuredGames",
                    steamStoreService.getFeaturedGames()
            );

            addSearchResults(
                    search,
                    model
            );

            return "dashboard";
        }

        Player player =
                playerService.getPlayerById(playerId);

        if (player == null) {

            model.addAttribute(
                    "isLoggedIn",
                    false
            );

            model.addAttribute(
                    "featuredGames",
                    steamStoreService.getFeaturedGames()
            );

            addSearchResults(
                    search,
                    model
            );

            return "dashboard";
        }

        List<BacklogItem> backlogItems =
                backlogService.getBacklogByPlayer(player);

        if (backlogItems == null) {
            backlogItems = new ArrayList<>();
        }

        long libraryCount =
                player.getLibraryGames() != null
                        ? player.getLibraryGames().size()
                        : 0;

        long backlogCount =
                backlogItems.size();

        List<DashboardQuest> dashboardQuests =
                getDashboardQuests(player);

        long questCount =
                dashboardQuests.size();

        model.addAttribute(
                "player",
                player
        );

        model.addAttribute(
                "libraryCount",
                libraryCount
        );

        model.addAttribute(
                "backlogCount",
                backlogCount
        );

        model.addAttribute(
                "questCount",
                questCount
        );

        model.addAttribute(
                "recentGames",
                backlogItems.stream()
                        .limit(5)
                        .toList()
        );

        model.addAttribute(
                "activeQuests",
                dashboardQuests
        );

        model.addAttribute(
                "isLoggedIn",
                true
        );

        addSearchResults(
                search,
                model
        );

        return "dashboard";
    }

    private List<DashboardQuest> getDashboardQuests(
            Player player) {

        List<DashboardQuest> dashboardQuests =
                new ArrayList<>();

        List<Quest> quests =
                questService.getActiveQuests();

        for (Quest quest : quests) {

            QuestPlayer questPlayer =
                    questPlayerService.getQuestPlayer(
                            player,
                            quest
                    );

            DashboardQuest dashboardQuest =
                    new DashboardQuest();

            dashboardQuest.setId(
                    quest.getId()
            );

            dashboardQuest.setTitle(
                    quest.getTitle()
            );

            dashboardQuest.setDescription(
                    quest.getDescription()
            );

            dashboardQuest.setTargetProgress(
                    quest.getTargetProgress()
            );

            if (questPlayer != null) {

                dashboardQuest.setJoined(true);

                dashboardQuest.setProgress(
                        questPlayer.getProgress()
                );

                dashboardQuest.setCompleted(
                        questPlayer.isCompleted()
                );

            } else {

                dashboardQuest.setJoined(false);

                dashboardQuest.setProgress(0);

                dashboardQuest.setCompleted(false);
            }

            dashboardQuests.add(
                    dashboardQuest
            );
        }

        return dashboardQuests;
    }

    private void addSearchResults(
            String search,
            Model model) {

        if (search != null && !search.isBlank()) {

            List<Map<String, Object>> searchResults =
                    steamStoreService.searchGames(search);

            model.addAttribute(
                    "searchResults",
                    searchResults
            );

            model.addAttribute(
                    "searchQuery",
                    search
            );
        }
    }
}