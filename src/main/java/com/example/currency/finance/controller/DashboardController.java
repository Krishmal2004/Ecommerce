package com.example.currency.finance.controller;

import com.example.currency.finance.dto.DashboardResponse;
import com.example.currency.finance.dto.ExpenseRequest; // Import this
import com.example.currency.finance.model.User;
import com.example.currency.finance.service.DashboardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("user");
        DashboardResponse data = dashboardService.getDashboardData(user);

        // --- NEW: Check if there is a pending item in the Cart (Session) ---
        ExpenseRequest pendingExpense = (ExpenseRequest) session.getAttribute("pendingExpense");
        model.addAttribute("pendingExpense", pendingExpense);
        // -------------------------------------------------------------------

        model.addAttribute("user", user);
        model.addAttribute("data", data);

        return "dashboard";
    }

}