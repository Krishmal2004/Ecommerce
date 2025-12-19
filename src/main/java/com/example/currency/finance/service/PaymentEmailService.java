package com.example.currency.finance.service;

import com.example.currency.finance.dto.ExpenseRequest;
import com.example.currency.finance.model.User;

public interface PaymentEmailService {
    void sendPaymentReceipt(User user, ExpenseRequest expense);
}