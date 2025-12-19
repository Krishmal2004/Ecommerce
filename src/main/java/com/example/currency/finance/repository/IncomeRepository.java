package com.example.currency.finance.repository;

import com.example.currency.finance.model.Income;
import com.example.currency.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    // --- ADD THIS LINE HERE ---
    List<Income> findByUser(User user);
    // --------------------------

    List<Income> findByUserOrderByIncomeDateDesc(User user);

    @Query("SELECT i FROM Income i WHERE i.user = :user AND i.incomeDate BETWEEN :startDate AND :endDate")
    List<Income> findByUserAndDateRange(@Param("user") User user,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user = :user")
    Double getTotalIncomeByUser(@Param("user") User user);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user = :user AND i.incomeDate BETWEEN :startDate AND :endDate")
    Double getTotalIncomeByUserAndDateRange(@Param("user") User user,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);
}