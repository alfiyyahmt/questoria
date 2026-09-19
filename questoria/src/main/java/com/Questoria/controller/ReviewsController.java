package com.Questoria.controller;

import com.Questoria.model.Game;
import com.Questoria.model.Player;
import com.Questoria.model.Review;
import com.Questoria.repository.GameRepository;
import com.Questoria.service.PlayerService;
import com.Questoria.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ReviewsController {

    private final ReviewService reviewService;
    private final PlayerService playerService;
    private final GameRepository gameRepository;

    public ReviewsController(
            ReviewService reviewService,
            PlayerService playerService,
            GameRepository gameRepository) {

        this.reviewService = reviewService;
        this.playerService = playerService;
        this.gameRepository = gameRepository;
    }

    @GetMapping("/reviews")
    public String reviews(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) Long gameId,
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

        List<Review> reviews;

        if ("mine".equalsIgnoreCase(filter)) {

            reviews =
                    reviewService.getReviewsByPlayer(
                            player
                    );

        } else if ("friends".equalsIgnoreCase(filter)) {

            reviews =
                    reviewService.getReviewsByFriends(
                            player
                    );

        } else {

            reviews =
                    reviewService.getAllReviews();
        }

        model.addAttribute(
                "reviews",
                reviews
        );

        model.addAttribute(
                "selectedFilter",
                filter
        );

        model.addAttribute(
                "player",
                player
        );

        model.addAttribute(
                "isLoggedIn",
                true
        );

        model.addAttribute(
                "isAdmin",
                "ADMIN".equalsIgnoreCase(
                        player.getRole()
                )
        );

        if (gameId != null) {

            Game game =
                    gameRepository
                            .findById(gameId)
                            .orElse(null);

            if (game != null) {

                Review existingReview =
                        reviewService
                                .getReviewByPlayerAndGame(
                                        player,
                                        game
                                );

                model.addAttribute(
                        "selectedGame",
                        game
                );

                model.addAttribute(
                        "existingReview",
                        existingReview
                );
            }
        }

        return "reviews";
    }

    @PostMapping("/reviews/save")
    public String saveReview(
            @RequestParam Long gameId,
            @RequestParam int rating,
            @RequestParam String content,
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

        Game game =
                gameRepository
                        .findById(gameId)
                        .orElse(null);

        if (game == null) {
            return "redirect:/reviews";
        }

        if (rating < 1 || rating > 5) {
            return "redirect:/reviews?gameId=" + gameId;
        }

        if (content == null || content.isBlank()) {
            return "redirect:/reviews?gameId=" + gameId;
        }

        Review review =
                reviewService.getReviewByPlayerAndGame(
                        player,
                        game
                );

        if (review == null) {

            review =
                    new Review();

            review.setPlayer(player);
            review.setGame(game);

        }

        review.setRating(rating);
        review.setContent(content);

        reviewService.saveReview(review);

        return "redirect:/reviews?gameId=" + gameId;
    }
}