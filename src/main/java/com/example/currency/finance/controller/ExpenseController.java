package com.example.currency.finance.controller;

import com.example.currency.finance.dto.ExpenseRequest;
import com.example.currency.finance.model. Expense;
import com.example. currency.finance.model.User;
import com.example.currency. finance.service.ExpenseService;
import jakarta.servlet.http. HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind. annotation.*;

import java.util. List;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public String showExpensesPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("user");
        List<Expense> expenses = expenseService.getUserExpenses(user);

        model.addAttribute("expenses", expenses);
        model.addAttribute("expenseRequest", new ExpenseRequest());
        return "expenses";
    }

    @PostMapping("/add")
    public String addExpense(@Valid @ModelAttribute("expenseRequest") ExpenseRequest expenseRequest,
                             BindingResult result,
                             HttpServletRequest request,
                             Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            User user = (User) session.getAttribute("user");
            List<Expense> expenses = expenseService.getUserExpenses(user);
            model.addAttribute("expenses", expenses);
            return "expenses";
        }

        try {
            User user = (User) session.getAttribute("user");
            expenseService.addExpense(user, expenseRequest);
            return "redirect:/expenses? success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "expenses";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("user");
        expenseService. deleteExpense(id, user);

        return "redirect:/expenses? deleted";
    }
}