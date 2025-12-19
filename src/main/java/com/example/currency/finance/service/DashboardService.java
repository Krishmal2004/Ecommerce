package com.example.currency.finance.service;

import com.example.currency.finance.dto.DashboardResponse;
import com.example.currency.finance.model.Card;
import com.example.currency.finance.model.Expense;
import com.example.currency.finance.model.Income;
import com.example.currency.finance.model.User;
import com.example.currency.finance.repository.CardRepository;
import com.example.currency.finance.repository.ExpenseRepository;
import com.example.currency.finance.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final CardRepository cardRepository;
    private final AiAdviceService aiAdviceService;

    public DashboardService(IncomeRepository incomeRepository, ExpenseRepository expenseRepository, CardRepository cardRepository, AiAdviceService aiAdviceService) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.cardRepository = cardRepository;
        this.aiAdviceService = aiAdviceService;
    }

    public DashboardResponse getDashboardData(User user) {
        DashboardResponse response = new DashboardResponse();
        response.setUserName(user.getFullName());

        // 1. Fetch Basic Data
        List<Income> incomes = incomeRepository.findByUser(user);
        List<Card> cards = cardRepository.findByUser(user);
        List<Expense> allExpenses = expenseRepository.findByUser(user);

        // --- NEW LOGIC: Date Calculations ---
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).toLocalDate().atTime(23, 59, 59);

        // Fetch Monthly Expenses using existing Repository method
        List<Expense> monthExpensesList = expenseRepository.findByUserAndDateRange(user, startOfMonth, endOfMonth);

        // Filter for "Due Today" (compare LocalDate)
        List<Expense> todayExpensesList = monthExpensesList.stream()
                .filter(e -> e.getExpenseDate().toLocalDate().isEqual(LocalDate.now()))
                .collect(Collectors.toList());

        // 2. Calculate Totals
        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = allExpenses.stream().mapToDouble(Expense::getAmount).sum();
        double totalBalance = totalIncome - totalExpense;

        response.setTotalIncome(totalIncome);
        response.setTotalExpenses(totalExpense);
        response.setTotalBalance(totalBalance);

        // 3. Map Cards
        List<DashboardResponse.CardSummary> cardSummaries = cards.stream().map(card -> {
            DashboardResponse.CardSummary summary = new DashboardResponse.CardSummary();
            summary.setId(card.getId());
            summary.setBankName(card.getBankName());
            summary.setCardNumber(card.getCardNumber().substring(Math.max(0, card.getCardNumber().length() - 4)));
            summary.setCardType(card.getCardType());
            summary.setBalance(card.getBalance());
            return summary;
        }).collect(Collectors.toList());
        response.setCards(cardSummaries);

        // 4. Map Recent Expenses (Top 5)
        List<DashboardResponse.ExpenseSummary> recentSummaries = allExpenses.stream()
                .sorted((e1, e2) -> e2.getExpenseDate().compareTo(e1.getExpenseDate()))
                .limit(5)
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        response.setRecentExpenses(recentSummaries);

        // --- 5. Map Monthly Expenses (Full List for Month) ---
        List<DashboardResponse.ExpenseSummary> monthlySummaries = monthExpensesList.stream()
                .sorted((e1, e2) -> e2.getExpenseDate().compareTo(e1.getExpenseDate()))
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        response.setMonthlyExpenses(monthlySummaries);

        // --- 6. Map Due Today (Alert List) ---
        List<DashboardResponse.ExpenseSummary> todaySummaries = todayExpensesList.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        response.setDueToday(todaySummaries);

        response.setRecentIncome(new ArrayList<>());

        // --- 7. Generate AI Insight (MOVED UP) ---
        // I removed the 'return response;' that was here previously
        String insight = aiAdviceService.generateFinancialAdvice(response);
        response.setAiInsight(insight);

        // Final Return
        return response;
    }

    // Helper to map Entity to DTO
    private DashboardResponse.ExpenseSummary mapToSummary(Expense e) {
        DashboardResponse.ExpenseSummary sum = new DashboardResponse.ExpenseSummary();
        sum.setId(e.getId());
        sum.setCategory(e.getCategory());
        sum.setDescription(e.getDescription());
        sum.setAmount(e.getAmount());
        sum.setDate(e.getExpenseDate().toLocalDate().toString());
        // Added safe handling for paymentType if needed later
        sum.setPaymentType(e.getPaymentType());
        return sum;
    }
}