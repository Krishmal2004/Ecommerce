package com.example.currency.finance. controller;

import com.example. currency.finance.dto.IncomeRequest;
import com.example.currency.finance.model.Income;
import com.example.currency.finance.model.User;
import com.example.currency.finance.service.IncomeService;
import jakarta.servlet.http. HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype. Controller;
import org.springframework. ui.Model;
import org. springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/income")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping
    public String showIncomePage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("user");
        List<Income> incomes = incomeService. getUserIncome(user);

        model. addAttribute("incomes", incomes);
        model.addAttribute("incomeRequest", new IncomeRequest());
        return "income";
    }

    @PostMapping("/add")
    public String addIncome(@Valid @ModelAttribute("incomeRequest") IncomeRequest incomeRequest,
                            BindingResult result,
                            HttpServletRequest request,
                            Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            User user = (User) session.getAttribute("user");
            List<Income> incomes = incomeService.getUserIncome(user);
            model.addAttribute("incomes", incomes);
            return "income";
        }

        try {
            User user = (User) session.getAttribute("user");
            incomeService. addIncome(user, incomeRequest);
            return "redirect:/income?success";
        } catch (Exception e) {
            model.addAttribute("error", e. getMessage());
            return "income";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteIncome(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User user = (User) session. getAttribute("user");
        incomeService.deleteIncome(id, user);

        return "redirect:/income?deleted";
    }
}