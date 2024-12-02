package com.btec.fpt.campus_expense_manager.models;

public class CategoriesModel {
    public String Amount;

    public CategoriesModel(String amount, String description, String date) {
        Amount = amount;
        Description = description;
        Date = date;
    }

    public String Description;
    public String Date;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
