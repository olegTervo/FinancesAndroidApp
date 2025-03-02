package com.example.finances.interface_adapters.repository;

import com.example.finances.domain.enums.InvestmentType;
import com.example.finances.domain.enums.PriceType;
import com.example.finances.domain.interfaces.IInvestmentRepository;
import com.example.finances.domain.models.Investment;
import com.example.finances.frameworks_and_drivers.database.investment.InvestmentDao;
import com.example.finances.frameworks_and_drivers.database.price.PriceDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class InvestmentRepository implements IInvestmentRepository {
    private final InvestmentDao investmentDao;
    private final PriceDao priceDao;

    @Inject
    public InvestmentRepository(InvestmentDao dao, PriceDao priceDao) {
        this.investmentDao = dao;
        this.priceDao = priceDao;
    }

    @Override
    public Investment GetInvestment(long id) {
        return investmentDao.GetInvestment(id);
    }

    @Override
    public List<Investment> GetInvestments() {
        return investmentDao.GetInvestments();
    }

    @Override
    public List<Investment> SearchInvestments(InvestmentType type) {
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
        if( priceDao.DeletePrices(id, PriceType.Investment) )
            return investmentDao.DeleteInvestment(id);

        return false;
    }
}
