package com.Questoria.service;

import com.Questoria.model.Quest;
import com.Questoria.repository.QuestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestService {

    private final QuestRepository questRepository;

    public QuestService(
            QuestRepository questRepository) {

        this.questRepository = questRepository;
    }

    public List<Quest> getAllQuests() {

        return questRepository.findAll();
    }

    public List<Quest> getActiveQuests() {

        return questRepository.findByActiveTrue();
    }

    public Quest getQuestById(Long id) {

        return questRepository
                .findById(id)
                .orElse(null);
    }

    public Quest saveQuest(Quest quest) {

        return questRepository.save(quest);
    }

    public void deleteQuest(Long id) {

        questRepository.deleteById(id);
    }
}