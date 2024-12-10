package com.btec.fpt.campus_expense_manager.entities;
public class Expense {
    private int id;
    private double amount;
    private String description;
    private String date;
    private int type; // 0 = Expense, 1 = Income
    private String email;

    public Expense(int id, double amount, String description, String date, int type, String email) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.email = email;
    }

    // Getter methods
    public int getId() { return id; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public int getType() { return type; }
    public String getEmail() { return email; }
}