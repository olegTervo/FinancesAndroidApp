package com.example.finances.interface_adapters.repository;

import com.example.finances.domain.enums.InvestmentType;
import com.example.finances.domain.interfaces.IInvestmentRepository;
import com.example.finances.domain.models.Investment;
import com.example.finances.frameworks_and_drivers.database.investment.InvestmentDao;

import java.util.ArrayList;

import javax.inject.Inject;

public class InvestmentRepository implements IInvestmentRepository {
    private final InvestmentDao investmentDao;

    @Inject
    public InvestmentRepository(InvestmentDao dao) {
        investmentDao = dao;
    }

    @Override
    public Investment GetInvestment(long id) {
        return investmentDao.GetInvestment(id);
    }

    @Override
    public ArrayList<Investment> GetInvestments() {
        return investmentDao.GetInvestments();
    }

    @Override
    public ArrayList<Investment> SearchInvestments(InvestmentType type) {
        return investmentDao.SearchInvestments(type);
    }

    @Override
    public Investment CreateInvestment(String name, float amount) {
        long id = investmentDao.CreateInvestment(name, amount);
        return investmentDao.GetInvestment(id);
    }

    @Override
    public Investment UpdateInvestment(Investment investment) {
        investmentDao.UpdateInvestment(investment);
        return investmentDao.GetInvestment(investment.getId());
    }

    @Override
    public boolean DeleteInvestment(long id) {
        return investmentDao.DeleteInvestment(id);
    }
}
