package com.example.currency.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time. LocalDateTime;

public class IncomeRequest {

    @NotBlank(message = "Source is required")
    private String source;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount is required")
    private Double amount;

    @NotNull(message = "Income date is required")
    private LocalDateTime incomeDate;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this. source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(LocalDateTime incomeDate) {
        this. incomeDate = incomeDate;
    }
}