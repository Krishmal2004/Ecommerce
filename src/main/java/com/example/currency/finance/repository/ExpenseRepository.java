package com.example.currency.finance.repository;

import com.example.currency.finance.model.Expense;
import com.example.currency.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // --- ADD THIS LINE HERE ---
    List<Expense> findByUser(User user);
    // --------------------------

    List<Expense> findByUserOrderByExpenseDateDesc(User user);

    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.expenseDate BETWEEN :startDate AND :endDate")
    List<Expense> findByUserAndDateRange(@Param("user") User user,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user")
    Double getTotalExpensesByUser(@Param("user") User user);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.expenseDate BETWEEN :startDate AND :endDate")
    Double getTotalExpensesByUserAndDateRange(@Param("user") User user,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);
}