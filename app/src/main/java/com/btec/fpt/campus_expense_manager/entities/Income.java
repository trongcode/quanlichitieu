package com.btec.fpt.campus_expense_manager.entities;
public class Income {
    private String description;
    private String amount;
    private String date;

    public Income(String description, String amount, String date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}

