package com.btec.fpt.campus_expense_manager.models;

public class TransactionInfor {

    public TransactionInfor(int item1, String description) {
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    private int ImageId;
    private Double Amount;
    private String Description;
    private String Date;



    public TransactionInfor(Double amount, String description, String date ) {
        Amount = amount;
        Description = description;
        Date = date;

    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        this.Amount = amount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }


}
