package com.example.currency.finance.controller;

import com.example.currency.finance.dto.*;
import com.example.currency.finance.model.User;
import com.example.currency.finance.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/dashboard")
public class DashboardFormController {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final CardService cardService;
    private final StripeService stripeService;
    private final PaymentEmailService paymentEmailService;

    public DashboardFormController(IncomeService incomeService, ExpenseService expenseService,
                                   CardService cardService, StripeService stripeService, PaymentEmailService paymentEmailService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.cardService = cardService;
        this.stripeService = stripeService;
        this.paymentEmailService = paymentEmailService;
    }

    private User getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) return (User) session.getAttribute("user");
        return null;
    }

    // --- 1. ADD EXPENSE (Auto-Redirect) ---
    @PostMapping("/add-expense")
    public String addExpense(@ModelAttribute ExpenseRequest expenseRequest, HttpServletRequest request) {
        User user = getSessionUser(request);
        if (user == null) return "redirect:/login";

        if (expenseRequest.getExpenseDate() == null) {
            expenseRequest.setExpenseDate(LocalDate.now());
        }

        if ("CARD".equalsIgnoreCase(expenseRequest.getPaymentType())) {
            // Store in Cart
            HttpSession session = request.getSession();
            session.setAttribute("pendingExpense", expenseRequest);

            // Redirect to Retry Logic (Re-use code)
            return "redirect:/dashboard/checkout/retry";
        }

        expenseService.addExpense(user, expenseRequest);
        return "redirect:/dashboard";
    }

    // --- 2. RETRY PAYMENT (Called by Cart Modal) ---
    @GetMapping("/checkout/retry")
    public String retryPayment(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ExpenseRequest pending = (ExpenseRequest) session.getAttribute("pendingExpense");

        if (pending == null) return "redirect:/dashboard?error=cart_empty";

        String stripeUrl = stripeService.createCheckoutSession(
                pending.getAmount(),
                "usd",
                pending.getDescription(),
                "http://localhost:8080/dashboard/payment/success",
                "http://localhost:8080/dashboard/payment/cancel"
        );

        if (stripeUrl != null) {
            return "redirect:" + stripeUrl;
        } else {
            return "redirect:/dashboard?error=gateway_error";
        }
    }

    // --- 3. CLEAR CART ---
    @GetMapping("/cart/clear")
    public String clearCart(HttpServletRequest request) {
        request.getSession().removeAttribute("pendingExpense");
        return "redirect:/dashboard";
    }

    // --- 4. PAYMENT HANDLERS ---
    @GetMapping("/payment/success")
    public String handlePaymentSuccess(HttpServletRequest request) {
        User user = getSessionUser(request);
        HttpSession session = request.getSession();
        ExpenseRequest pendingExpense = (ExpenseRequest) session.getAttribute("pendingExpense");

        if (user != null && pendingExpense != null) {
            // Save DB
            expenseService.addExpense(user, pendingExpense);

            // 3. SEND CONFIRMATION EMAIL HERE
            // We run this in a separate thread so the user isn't waiting for the email to send
            new Thread(() -> {
                paymentEmailService.sendPaymentReceipt(user, pendingExpense);
            }).start();

            // Clear Cart
            session.removeAttribute("pendingExpense");
            return "redirect:/dashboard?payment=success";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/payment/cancel")
    public String handlePaymentCancel(HttpServletRequest request) {
        // IMPORTANT: We do NOT remove the attribute here.
        // We leave it in the session so the user can click the Cart Icon and try again.
        return "redirect:/dashboard?error=payment_cancelled";
    }

    // --- OTHER METHODS ---
    @PostMapping("/add-income")
    public String addIncome(@ModelAttribute IncomeRequest incomeRequest, HttpServletRequest request) {
        User user = getSessionUser(request);
        if (user == null) return "redirect:/login";
        if (incomeRequest.getIncomeDate() == null) incomeRequest.setIncomeDate(LocalDateTime.now());
        incomeService.addIncome(user, incomeRequest);
        return "redirect:/dashboard";
    }

    @PostMapping("/add-card")
    public String addCard(@ModelAttribute CardRequest cardRequest, HttpServletRequest request) {
        User user = getSessionUser(request);
        if (user == null) return "redirect:/login";
        cardService.addCard(user, cardRequest);
        return "redirect:/dashboard";
    }

    @GetMapping("/delete-card/{id}")
    public String deleteCard(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) return "redirect:/login";
        User user = (User) session.getAttribute("user");
        cardService.deleteCard(id, user);
        return "redirect:/dashboard";
    }
}