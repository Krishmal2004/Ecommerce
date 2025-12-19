package com.example.currency.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CardRequest {

    @NotBlank(message = "Card type is required")
    @Pattern(regexp = "CREDIT|DEBIT", message = "Card type must be CREDIT or DEBIT")
    private String cardType;

    @NotBlank(message = "Card number is required")
    private String cardNumber;

    @NotBlank(message = "Card holder name is required")
    private String cardHolderName;

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotNull(message = "Balance is required")
    private Double balance;

    // --- NEW FIELDS ---
    @NotBlank(message = "CVC is required")
    private String cvc;

    @NotBlank(message = "Expiry Date is required")
    private String expiryDate;
    // ------------------

    // Getters and Setters
    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    // --- NEW GETTERS/SETTERS ---
    public String getCvc() { return cvc; }
    public void setCvc(String cvc) { this.cvc = cvc; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}