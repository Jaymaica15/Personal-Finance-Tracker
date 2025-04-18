package com.ui;

import com.model.Transaction;
import com.model.FinanceTracker;
import com.storage.FileHandler;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;

public class ConsoleUI {

    private FinanceTracker tracker = new FinanceTracker();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": addTransaction(); break;
                case "2": viewBalance(); break;
                case "3": viewAllTransactions(); break;
                case "4": saveData(); break;
                case "5": loadData(); break;
                case "6": System.out.println("Exiting... Goodbye!"); running = false; break;
                default: System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Personal Finance Tracker ===");
        System.out.println("1. Add Transaction");
        System.out.println("2. View Balance");
        System.out.println("3. View All Transactions");
        System.out.println("4. Save Data");
        System.out.println("5. Load Data");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private void addTransaction() {
        try {
            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter type (income/expense): ");
            String type = scanner.nextLine();

            System.out.print("Enter category (e.g., Food, Rent, Other): ");
            String category = scanner.nextLine();

            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            Transaction t = new Transaction(amount, type, category, date, description);
            tracker.addTransaction(t);
            System.out.println("Transaction added!");
        } catch (Exception e) {
            System.out.println("Error: Invalid input. Please try again.");
        }
    }

    private void viewBalance() {
        System.out.printf("Current Balance: $%.2f\n", tracker.getBalance());
    }

    private void viewAllTransactions() {
        List<Transaction> list = tracker.getAllTransactions();
        if (list.isEmpty()) {
            System.out.println("No transactions recorded.");
        } else {
            System.out.println("=== All Transactions ===");
            for (Transaction t : list) {
                System.out.println(t);
            }
        }
    }

    private void saveData() {
        try {
            FileHandler.saveToFile(tracker.getAllTransactions(), "data/transactions.txt");
            System.out.println("Data saved to data/transactions.txt");
        } catch (Exception e) {
            System.out.println("Error saving file.");
        }
    }

    private void loadData() {
        try {
            List<Transaction> data = FileHandler.loadFromFile("data/transactions.txt");
            tracker.setTransactions(data);
            System.out.println("Data loaded from data/transactions.txt");
        } catch (Exception e) {
            System.out.println("Error loading file.");
        }
    }
}