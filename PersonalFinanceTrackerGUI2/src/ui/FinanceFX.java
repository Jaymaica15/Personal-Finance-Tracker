package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.model.FinanceTracker;
import com.model.Transaction;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class FinanceFX extends Application {

    private FinanceTracker tracker = new FinanceTracker();
    private TextArea outputArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Button addBtn = new Button("Add Transaction");
        Button balanceBtn = new Button("View Balance");
        Button viewBtn = new Button("View Transactions");
        Button chartBtn = new Button("Show Spending Chart");

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(1000);

        layout.getChildren().addAll(addBtn, balanceBtn, viewBtn, chartBtn, outputArea);

        addBtn.setOnAction(_ -> showAddTransactionDialog());
        balanceBtn.setOnAction(_ -> showBalance());
        viewBtn.setOnAction(_ -> showAllTransactions());
        chartBtn.setOnAction(_ -> showChart(primaryStage));
        
        Label title = new Label("💸 Personal Finance Tracker");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        layout.getChildren().add(0, title);

        Scene scene = new Scene(layout, 600, 400);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle("Personal Finance Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

	private void showAddTransactionDialog() {
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Add Transaction");

        GridPane grid = new GridPane();
        grid.setHgap(50);
        grid.setVgap(20);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField amountField = new TextField();
        TextField typeField = new TextField();
        TextField categoryField = new TextField();
        TextField descriptionField = new TextField();

        grid.add(new Label("Amount:"), 0, 0);
        grid.add(amountField, 1, 0);
        grid.add(new Label("Type (income/expense):"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryField, 1, 2);
        grid.add(new Label("Description:"), 0, 3);
        grid.add(descriptionField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    String type = typeField.getText();
                    String category = categoryField.getText();
                    String description = descriptionField.getText();
                    return new Transaction(amount, type, category, LocalDate.now(), description);
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(transaction -> {
            tracker.addTransaction(transaction);
            outputArea.setText("Transaction added successfully!");
        });
    }

    private void showBalance() {
        outputArea.setText(String.format("Current Balance: $%.2f", tracker.getBalance()));
    }

    private void showAllTransactions() {
        StringBuilder sb = new StringBuilder();
        for (Transaction t : tracker.getAllTransactions()) {
            sb.append(t.toString()).append("\n");
        }
        outputArea.setText(sb.toString());
    }
    
    public void showChart(Stage stage) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Spending by Category");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expenses");

        tracker.getAllTransactions().stream()
            .filter(t -> t.getType().equals("expense"))
            .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)))
            .forEach((category, total) -> series.getData().add(new XYChart.Data<>(category, total)));

        chart.getData().add(series);
        Stage chartStage = new Stage();
        chartStage.setTitle("Expense Breakdown");
        VBox chartLayout = new VBox(chart);
        chartLayout.setPadding(new Insets(10));
        Scene chartScene = new Scene(chartLayout, 500, 400);
        chartStage.setScene(chartScene);
        chartStage.show();
    }
}
