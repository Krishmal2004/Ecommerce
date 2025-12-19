package com.example.currency.finance.dto;

import java.util.List;
import java.util.Map;

public class DashboardResponse {

    private String userName;
    private Double totalBalance;
    private Double totalIncome;
    private Double totalExpenses;
    private String aiInsight;
    private List<CardSummary> cards;
    private List<ExpenseSummary> recentExpenses;
    private List<IncomeSummary> recentIncome;
    private Map<String, Double> monthlyAnalytics;
    private Map<String, Double> weeklyAnalytics;
    private Map<String, Double> yearlyAnalytics;
    private List<ExpenseSummary> monthlyExpenses;
    private List<ExpenseSummary> dueToday;

    public static class CardSummary {
        private Long id;
        private String cardType;
        private String cardNumber;
        private String bankName;
        private Double balance;

        public Long getId() {
            return id;
        }


        public void setId(Long id) {
            this.id = id;
        }

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }
    }

    public static class ExpenseSummary {
        private Long id;
        private String category;
        private String description;
        private Double amount;
        private String date;
        private String paymentType;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
        public String getPaymentType() {return paymentType;}
        public void setPaymentType(String paymentType) {this.paymentType = paymentType;}
    }

    public static class IncomeSummary {
        private Long id;
        private String source;
        private String description;
        private Double amount;
        private String date;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
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
            this. amount = amount;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    // Main Getters and Setters
    public String getUserName() {
        return userName;
    }
    public String getAiInsight() {
        return aiInsight;
    }
    public void setAiInsight(String aiInsight) {
        this.aiInsight = aiInsight;
    }

    public void setUserName(String userName) {
        this. userName = userName;
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public List<CardSummary> getCards() {
        return cards;
    }

    public void setCards(List<CardSummary> cards) {
        this.cards = cards;
    }

    public List<ExpenseSummary> getRecentExpenses() {
        return recentExpenses;
    }

    public void setRecentExpenses(List<ExpenseSummary> recentExpenses) {
        this.recentExpenses = recentExpenses;
    }

    public List<IncomeSummary> getRecentIncome() {
        return recentIncome;
    }

    public void setRecentIncome(List<IncomeSummary> recentIncome) {
        this.recentIncome = recentIncome;
    }

    public Map<String, Double> getMonthlyAnalytics() {
        return monthlyAnalytics;
    }

    public void setMonthlyAnalytics(Map<String, Double> monthlyAnalytics) {
        this.monthlyAnalytics = monthlyAnalytics;
    }

    public Map<String, Double> getWeeklyAnalytics() {
        return weeklyAnalytics;
    }

    public void setWeeklyAnalytics(Map<String, Double> weeklyAnalytics) {
        this.weeklyAnalytics = weeklyAnalytics;
    }

    public Map<String, Double> getYearlyAnalytics() {
        return yearlyAnalytics;
    }
    public List<ExpenseSummary> getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(List<ExpenseSummary> monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public List<ExpenseSummary> getDueToday() {
        return dueToday;
    }

    public void setDueToday(List<ExpenseSummary> dueToday) {
        this.dueToday = dueToday;
    }

    public void setYearlyAnalytics(Map<String, Double> yearlyAnalytics) {
        this.yearlyAnalytics = yearlyAnalytics;
    }
}