package com.example.currency.finance.repository;

import com.example.currency.finance.model.Card;
import com.example.currency.finance.model.User;
import org.springframework.data.jpa.repository. JpaRepository;
import org. springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByUser(User user);
    List<Card> findByUserOrderByCreatedAtDesc(User user);
}