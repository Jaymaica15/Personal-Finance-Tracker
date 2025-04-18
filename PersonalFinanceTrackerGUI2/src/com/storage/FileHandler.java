package com.storage;

import com.model.Transaction;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public static void saveToFile(List<Transaction> transactions, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (Transaction t : transactions) {
            writer.write(String.format("%s,%s,%s,%.2f,%s\n",
                t.getDate(), t.getType(), t.getCategory(), t.getAmount(), t.getDescription()));
        }
        writer.close();
    }

    public static List<Transaction> loadFromFile1(String filename) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            LocalDate date = LocalDate.parse(parts[0]);
            String type = parts[1];
            String category = parts[2];
            double amount = Double.parseDouble(parts[3]);
            String description = parts[4];
            transactions.add(new Transaction(amount, type, category, date, description));
        }
        reader.close();
        return transactions;
    }

	public static List<Transaction> loadFromFile(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

}
