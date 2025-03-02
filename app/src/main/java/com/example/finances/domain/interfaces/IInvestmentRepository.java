package com.example.finances.domain.interfaces;

import com.example.finances.domain.enums.InvestmentType;
import com.example.finances.domain.models.Investment;

import java.util.ArrayList;
import java.util.List;

public interface IInvestmentRepository {
    Investment GetInvestment(long id);
    List<Investment> GetInvestments();
    List<Investment> SearchInvestments(InvestmentType type);
    Investment CreateInvestment(String name, float amount);
    Investment UpdateInvestment(Investment investment);
    boolean DeleteInvestment(long id);
}
