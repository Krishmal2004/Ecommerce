package com.example.currency.finance. service;

import com.example. currency.finance.dto.IncomeRequest;
import com.example.currency.finance.model.Income;
import com.example.currency.finance.model.User;
import com.example.currency.finance.repository.IncomeRepository;
import org.springframework.stereotype. Service;

import java.util. List;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    public Income addIncome(User user, IncomeRequest request) {
        Income income = new Income();
        income.setUser(user);
        income.setSource(request.getSource());
        income.setDescription(request.getDescription());
        income.setAmount(request.getAmount());
        income.setIncomeDate(request.getIncomeDate());

        return incomeRepository.save(income);
    }

    public List<Income> getUserIncome(User user) {
        return incomeRepository.findByUserOrderByIncomeDateDesc(user);
    }

    public void deleteIncome(Long incomeId, User user) {
        Income income = incomeRepository. findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        if (!income.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        incomeRepository.delete(income);
    }
}