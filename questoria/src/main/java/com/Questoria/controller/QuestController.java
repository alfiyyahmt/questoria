package com.Questoria.controller;

import com.Questoria.model.Player;
import com.Questoria.model.Quest;
import com.Questoria.model.QuestPlayer;
import com.Questoria.service.PlayerService;
import com.Questoria.service.QuestPlayerService;
import com.Questoria.service.QuestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class QuestController {

    private final QuestService questService;
    private final QuestPlayerService questPlayerService;
    private final PlayerService playerService;

    public QuestController(
            QuestService questService,
            QuestPlayerService questPlayerService,
            PlayerService playerService) {

        this.questService = questService;
        this.questPlayerService = questPlayerService;
        this.playerService = playerService;
    }

    @GetMapping({"/quests", "/quest"})
    public String quests(
            Model model,
            HttpSession session) {

        Long playerId =
                (Long) session.getAttribute("playerId");

        if (playerId == null) {
            return "redirect:/auth/steam";
        }

        Player player =
                playerService.getPlayerById(playerId);

        if (player == null) {
            return "redirect:/auth/steam";
        }

        List<Quest> quests =
                questService.getActiveQuests();

        Map<Long, QuestPlayer> questPlayers =
                new HashMap<>();

        for (Quest quest : quests) {

            QuestPlayer questPlayer =
                    questPlayerService.getQuestPlayer(
                            player,
                            quest
                    );

            if (questPlayer != null) {
                questPlayers.put(
                        quest.getId(),
                        questPlayer
                );
            }
        }

        model.addAttribute(
                "quests",
                quests
        );

        model.addAttribute(
                "questPlayers",
                questPlayers
        );

        return "quests";
    }

    @PostMapping("/quests/{id}/join")
    public String joinQuest(
            @PathVariable Long id,
            HttpSession session) {

        Long playerId =
                (Long) session.getAttribute("playerId");

        if (playerId == null) {
            return "redirect:/auth/steam";
        }

        Player player =
                playerService.getPlayerById(playerId);

        Quest quest =
                questService.getQuestById(id);

        if (player == null || quest == null) {
            return "redirect:/quests";
        }

        questPlayerService.joinQuest(
                player,
                quest
        );

        return "redirect:/quests";
    }

    @PostMapping("/quests/{id}/progress")
    public String updateQuestProgress(
            @PathVariable Long id,
            @RequestParam int progress,
            HttpSession session) {

        Long playerId =
                (Long) session.getAttribute("playerId");

        if (playerId == null) {
            return "redirect:/auth/steam";
        }

        Player player =
                playerService.getPlayerById(playerId);

        Quest quest =
                questService.getQuestById(id);

        if (player == null || quest == null) {
            return "redirect:/quests";
        }

        questPlayerService.updateProgress(
                player,
                quest,
                progress
        );

        return "redirect:/quests";
    }

    @GetMapping("/admin/quests")
    public String adminQuests(
            Model model,
            HttpSession session) {

        Player player =
                getLoggedInPlayer(session);

        if (player == null) {
            return "redirect:/auth/steam";
        }

        if (!"ADMIN".equalsIgnoreCase(
                player.getRole())) {

            return "redirect:/";
        }

        model.addAttribute(
                "quests",
                questService.getAllQuests()
        );

        return "admin-quests";
    }

    @PostMapping("/admin/quests")
    public String createQuest(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam int targetProgress,
            HttpSession session) {

        Player player =
                getLoggedInPlayer(session);

        if (player == null) {
            return "redirect:/auth/steam";
        }

        if (!"ADMIN".equalsIgnoreCase(
                player.getRole())) {

            return "redirect:/";
        }

        if (title == null || title.isBlank()) {
            return "redirect:/admin/quests";
        }

        if (description == null ||
                description.isBlank()) {

            return "redirect:/admin/quests";
        }

        if (targetProgress <= 0) {
            return "redirect:/admin/quests";
        }

        Quest quest = new Quest();

        quest.setTitle(title.trim());
        quest.setDescription(description.trim());
        quest.setTargetProgress(targetProgress);
        quest.setActive(true);

        questService.saveQuest(quest);

        return "redirect:/admin/quests";
    }

    @PostMapping("/admin/quests/{id}/delete")
    public String deleteQuest(
            @PathVariable Long id,
            HttpSession session) {

        Player player =
                getLoggedInPlayer(session);

        if (player == null) {
            return "redirect:/auth/steam";
        }

        if (!"ADMIN".equalsIgnoreCase(
                player.getRole())) {

            return "redirect:/";
        }

        Quest quest =
                questService.getQuestById(id);

        if (quest != null) {
            quest.setActive(false);
            questService.saveQuest(quest);
        }

        return "redirect:/admin/quests";
    }

    private Player getLoggedInPlayer(
            HttpSession session) {

        Long playerId =
                (Long) session.getAttribute("playerId");

        if (playerId == null) {
            return null;
        }

        return playerService.getPlayerById(
                playerId
        );
    }
}