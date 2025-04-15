package com.model;

import java.time.LocalDate;

public class Transaction {
    private double amount;
    private String type; // "income" or "expense"
    private String category;
    private LocalDate date;
    private String description;

    public Transaction(double amount, String type, String category, LocalDate date, String description) {
        this.amount = amount;
        this.type = type.toLowerCase();
        this.category = category;
        this.date = date;
        this.description = description;
    }

    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("[%s] %s - $%.2f (%s): %s",
            date.toString(), type.toUpperCase(), amount, category, description);
    }
}
