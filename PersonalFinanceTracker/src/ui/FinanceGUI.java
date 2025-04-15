package ui;

import com.model.FinanceTracker;
import com.model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class FinanceGUI {

    private FinanceTracker tracker = new FinanceTracker();

    public void launch() {
        JFrame frame = new JFrame("Personal Finance Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton addBtn = new JButton("Add Transaction");
        JButton balanceBtn = new JButton("View Balance");
        JButton viewBtn = new JButton("View Transactions");

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);

        panel.add(addBtn);
        panel.add(balanceBtn);
        panel.add(viewBtn);
        panel.add(new JScrollPane(outputArea));

        frame.add(panel);
        frame.setVisible(true);

        addBtn.addActionListener(e -> {
            try {
                String amountStr = JOptionPane.showInputDialog("Amount:");
                double amount = Double.parseDouble(amountStr);

                String type = JOptionPane.showInputDialog("Type (income/expense):");
                String category = JOptionPane.showInputDialog("Category:");
                String desc = JOptionPane.showInputDialog("Description:");

                Transaction t = new Transaction(amount, type, category, LocalDate.now(), desc);
                tracker.addTransaction(t);
                outputArea.setText("Transaction added.");
            } catch (Exception ex) {
                outputArea.setText("Invalid input.");
            }
        });

        balanceBtn.addActionListener(e -> {
            outputArea.setText(String.format("Current Balance: $%.2f", tracker.getBalance()));
        });

        viewBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Transaction t : tracker.getAllTransactions()) {
                sb.append(t.toString()).append("\n");
            }
            outputArea.setText(sb.toString());
        });
    }
}

