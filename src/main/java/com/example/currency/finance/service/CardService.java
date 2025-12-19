package com.example.currency. finance.service;

import com. example.currency.finance.dto. CardRequest;
import com.example.currency.finance.model.Card;
import com.example.currency.finance.model.User;
import com.example.currency.finance.repository.CardRepository;
import org.springframework. stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card addCard(User user, CardRequest request) {
        Card card = new Card();
        card.setUser(user);
        card.setCardType(request.getCardType());
        card.setCardNumber(request.getCardNumber());
        card.setCardHolderName(request.getCardHolderName());
        card.setBankName(request.getBankName());
        card.setBalance(request.getBalance());
        card.setCvc(request.getCvc());
        card.setExpiryDate(request.getExpiryDate());

        String cardNumber = request.getCardNumber();
        if (cardNumber. length() > 4) {
            cardNumber = cardNumber.substring(cardNumber.length() - 4);
        }
        card.setCardNumber(cardNumber);

        card.setCardHolderName(request.getCardHolderName());
        card.setBankName(request.getBankName());
        card.setBalance(request.getBalance());

        return cardRepository.save(card);
    }

    public List<Card> getUserCards(User user) {
        return cardRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public void deleteCard(Long cardId, User user) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if (! card.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        cardRepository.delete(card);
    }
}