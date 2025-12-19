package com.example.currency.finance.service;

import com.example.currency.finance.dto.ExpenseRequest;
import com.example.currency.finance.model.Expense;
import com.example.currency.finance.model.User;
import com.example.currency.finance.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense addExpense(User user, ExpenseRequest request) {
        Expense expense = new Expense();
        expense.setUser(user);
        expense.setCategory(request.getCategory());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());

        // Save the payment type (Card or Cash)
        expense.setPaymentType(request.getPaymentType());

        // Convert LocalDate to LocalDateTime
        if (request.getExpenseDate() != null) {
            expense.setExpenseDate(request.getExpenseDate().atStartOfDay());
        }

        return expenseRepository.save(expense);
    }

    public List<Expense> getUserExpenses(User user) {
        return expenseRepository.findByUserOrderByExpenseDateDesc(user);
    }

    public void deleteExpense(Long expenseId, User user) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        expenseRepository.delete(expense);
    }
}