package com.pluralsight;

import java.time.*;

public class Transaction {

    private LocalDate date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    public Transaction() {}
    public Transaction(LocalDate date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public String getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}

    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getVendor() {return vendor;}
    public void setVendor(String vendor) {this.vendor = vendor;}

    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}

    public String toRow() {
        return date + "|"
                + getTime() + "|"
                + getDescription() + "|"
                + vendor + "|"
                + amount;
    }
}
