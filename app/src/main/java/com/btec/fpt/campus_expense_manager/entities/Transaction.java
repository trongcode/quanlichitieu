package com.btec.fpt.campus_expense_manager.entities;

public class Transaction {

    private int id;
    private double amount;
    private String description;
    private String date;
    private int type;
    private String email;
    private String category;
/*
    private String category;
*/

    @Override
    public String toString() {
        return String.format("ID: %d, Amount: %.2f, Description: %s, Date: %s, type: %d ,category:%s",
                id, amount, description, date,type,category);
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(int categoryId) {
        this.category = category;
    }

    public Transaction(int id, double amount, String description, String date, int type, String email, String category) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.email = email;
        this.category = category;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
