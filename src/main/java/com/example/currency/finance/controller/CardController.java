package com.example.currency.finance.controller;

import com.example.currency.finance.dto.CardRequest;
import com. example.currency.finance.model. Card;
import com.example.currency.finance.model.User;
import com.example.currency.finance.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web. bind.annotation.*;

import java. util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<?> addCard(@Valid @RequestBody CardRequest cardRequest,
                                     HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                return ResponseEntity. status(401).body("Unauthorized");
            }

            User user = (User) session. getAttribute("user");
            Card card = cardService.addCard(user, cardRequest);

            return ResponseEntity.ok(Map. of("message", "Card added successfully", "card", card));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e. getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserCards(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                return ResponseEntity.status(401).body("Unauthorized");
            }

            User user = (User) session.getAttribute("user");
            List<Card> cards = cardService.getUserCards(user);

            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            return ResponseEntity. badRequest().body(Map.of("error", e. getMessage()));
        }
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable Long cardId,
                                        HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                return ResponseEntity. status(401).body("Unauthorized");
            }

            User user = (User) session. getAttribute("user");
            cardService.deleteCard(cardId, user);

            return ResponseEntity.ok(Map.of("message", "Card deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest(). body(Map.of("error", e.getMessage()));
        }
    }
}