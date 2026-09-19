package com.Questoria.service;

import com.Questoria.model.Game;
import com.Questoria.model.Player;
import com.Questoria.model.Review;
import com.Questoria.model.SteamFriend;
import com.Questoria.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SteamFriendService steamFriendService;
    private final PlayerService playerService;

    public ReviewService(
            ReviewRepository reviewRepository,
            SteamFriendService steamFriendService,
            PlayerService playerService) {

        this.reviewRepository = reviewRepository;
        this.steamFriendService = steamFriendService;
        this.playerService = playerService;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Review> getReviewsByPlayer(Player player) {
        return reviewRepository.findByPlayerOrderByCreatedAtDesc(player);
    }

    public List<Review> getReviewsByGame(Game game) {
        return reviewRepository.findByGameOrderByCreatedAtDesc(game);
    }

    public Review getReviewByPlayerAndGame(
            Player player,
            Game game) {

        return reviewRepository
                .findByPlayerAndGame(player, game)
                .orElse(null);
    }

    public Review saveReview(Review review) {

        if (review.getCreatedAt() == null) {
            review.setCreatedAt(LocalDateTime.now());
        }

        return reviewRepository.save(review);
    }

    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }

    public List<Review> getReviewsByFriends(Player player) {

        List<Review> friendReviews =
                new ArrayList<>();

        if (player == null
                || player.getSteamAccount() == null) {

            return friendReviews;
        }

        String steamId =
                player.getSteamAccount().getSteamId();

        if (steamId == null || steamId.isBlank()) {
            return friendReviews;
        }

        List<SteamFriend> steamFriends =
                steamFriendService.getFriends(steamId);

        if (steamFriends == null
                || steamFriends.isEmpty()) {

            return friendReviews;
        }

        List<Player> questoriaPlayers =
                playerService.getAllPlayers();

        List<Review> allReviews =
                reviewRepository.findAllByOrderByCreatedAtDesc();

        for (SteamFriend steamFriend : steamFriends) {

            if (steamFriend == null
                    || steamFriend.getSteamId() == null
                    || steamFriend.getSteamId().isBlank()) {

                continue;
            }

            String friendSteamId =
                    steamFriend.getSteamId();

            Player questoriaFriend =
                    questoriaPlayers
                            .stream()
                            .filter(p ->
                                    p.getSteamAccount() != null
                            )
                            .filter(p ->
                                    friendSteamId.equals(
                                            p.getSteamAccount()
                                                    .getSteamId()
                                    )
                            )
                            .findFirst()
                            .orElse(null);

            if (questoriaFriend == null) {
                continue;
            }

            for (Review review : allReviews) {

                if (review == null
                        || review.getPlayer() == null) {
                    continue;
                }

                if (review.getPlayer()
                        .getId()
                        .equals(questoriaFriend.getId())) {

                    friendReviews.add(review);
                }
            }
        }

        return friendReviews;
    }
}