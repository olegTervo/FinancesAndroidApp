package com.example.finances.frameworks_and_drivers.database.loan;

import java.time.LocalDate;

public class LoanDao extends Object {
    public int id;
    public int unpaid;
    public int rate;
    public LocalDate openDate;

    public LoanDao(int id, int unpaid, int rate, LocalDate openDate) {
        this.id = id;
        this.unpaid = unpaid;
        this.rate = rate;
        this.openDate = openDate;
    }

    @Override
    public String toString() {
        return "id:" + id + "|" + unpaid + "€|" + openDate.toString();
    }
}
