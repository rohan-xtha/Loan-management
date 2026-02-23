package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Borrower;
import model.Loan;
import service.LoanService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LoanController {

    @FXML private TableView<Loan> loanTable;
    @FXML private TableColumn<Loan, Integer> colId;
    @FXML private TableColumn<Loan, String> colBorrower;
    @FXML private TableColumn<Loan, Double> colAmount;
    @FXML private TableColumn<Loan, Double> colInterest;
    @FXML private TableColumn<Loan, LocalDate> colDate;
    @FXML private TableColumn<Loan, LocalDate> colDueDate;
    @FXML private TableColumn<Loan, String> colStatus;
    @FXML private TableColumn<Loan, String> colRisk;

    @FXML private ComboBox<Borrower> borrowerCombo;
    @FXML private TextField amountField;
    @FXML private TextField interestField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<String> statusCombo;
    @FXML private ComboBox<String> typeCombo;
    @FXML private TextArea descArea;

    @FXML private Label riskLabel;
    @FXML private Label suggestionLabel;
    @FXML private Label reminderLabel;

    @FXML private TextField newBorrowerName;
    @FXML private TextField newBorrowerContact;

    private LoanService loanService = new LoanService();
    private ObservableList<Loan> loanList = FXCollections.observableArrayList();
    private ObservableList<Borrower> borrowerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        colBorrower.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colInterest.setCellValueFactory(new PropertyValueFactory<>("interestRate"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRisk.setCellValueFactory(new PropertyValueFactory<>("riskLevel"));

        // Setup Combos
        statusCombo.setItems(FXCollections.observableArrayList("pending", "partial", "cleared"));
        typeCombo.setItems(FXCollections.observableArrayList("Money", "Item"));

        // Load data
        loadBorrowers();
        loadLoans();

        // Table selection listener
        loanTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showLoanDetails(newSelection);
            }
        });
    }

    private void loadBorrowers() {
        try {
            borrowerList.setAll(loanService.getAllBorrowers());
            borrowerCombo.setItems(borrowerList);
        } catch (SQLException e) {
            showAlert("Error", "Could not load borrowers: " + e.getMessage());
        }
    }

    private void loadLoans() {
        try {
            loanList.setAll(loanService.getAllLoans());
            loanTable.setItems(loanList);
        } catch (SQLException e) {
            showAlert("Error", "Could not load loans: " + e.getMessage());
        }
    }

    private void showLoanDetails(Loan loan) {
        amountField.setText(String.valueOf(loan.getAmount()));
        interestField.setText(String.valueOf(loan.getInterestRate()));
        dueDatePicker.setValue(loan.getDueDate());
        statusCombo.setValue(loan.getStatus());
        typeCombo.setValue(loan.getType());
        descArea.setText(loan.getDescription());
        
        // Find and select borrower in combo
        for (Borrower b : borrowerList) {
            if (b.getBorrowerId() == loan.getBorrowerId()) {
                borrowerCombo.setValue(b);
                break;
            }
        }

        // Update AI labels
        riskLabel.setText("Risk Level: " + loan.getRiskLevel());
        suggestionLabel.setText(loanService.getAISuggestion(loan));
        reminderLabel.setText(loanService.getReminderMessage(loan));
    }

    @FXML
    private void handleAddLoan() {
        try {
            Loan loan = new Loan();
            Borrower selected = borrowerCombo.getValue();
            if (selected == null) {
                showAlert("Warning", "Please select a borrower.");
                return;
            }
            loan.setBorrowerId(selected.getBorrowerId());
            loan.setAmount(Double.parseDouble(amountField.getText()));
            loan.setInterestRate(Double.parseDouble(interestField.getText()));
            loan.setLoanDate(LocalDate.now());
            loan.setDueDate(dueDatePicker.getValue());
            loan.setStatus(statusCombo.getValue());
            loan.setType(typeCombo.getValue());
            loan.setDescription(descArea.getText());

            loanService.addLoan(loan);
            loadLoans();
            handleClearFields();
        } catch (Exception e) {
            showAlert("Error", "Invalid input or database error: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateLoan() {
        Loan selected = loanTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a loan to update.");
            return;
        }

        try {
            selected.setAmount(Double.parseDouble(amountField.getText()));
            selected.setInterestRate(Double.parseDouble(interestField.getText()));
            selected.setDueDate(dueDatePicker.getValue());
            selected.setStatus(statusCombo.getValue());
            selected.setType(typeCombo.getValue());
            selected.setDescription(descArea.getText());

            loanService.updateLoan(selected);
            loadLoans();
            showAlert("Success", "Loan updated successfully.");
        } catch (Exception e) {
            showAlert("Error", "Could not update loan: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteLoan() {
        Loan selected = loanTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a loan to delete.");
            return;
        }

        try {
            loanService.deleteLoan(selected.getLoanId());
            loadLoans();
            handleClearFields();
        } catch (SQLException e) {
            showAlert("Error", "Could not delete loan: " + e.getMessage());
        }
    }

    @FXML
    private void handleClearFields() {
        borrowerCombo.setValue(null);
        amountField.clear();
        interestField.clear();
        dueDatePicker.setValue(null);
        statusCombo.setValue(null);
        typeCombo.setValue(null);
        descArea.clear();
        riskLabel.setText("Risk Level: -");
        suggestionLabel.setText("Suggestion: -");
        reminderLabel.setText("Reminder: -");
    }

    @FXML
    private void handleAddBorrower() {
        String name = newBorrowerName.getText();
        String contact = newBorrowerContact.getText();
        if (name.isEmpty()) {
            showAlert("Warning", "Name cannot be empty.");
            return;
        }

        try {
            Borrower b = new Borrower(0, name, contact);
            loanService.addBorrower(b);
            newBorrowerName.clear();
            newBorrowerContact.clear();
            loadBorrowers();
            showAlert("Success", "Borrower added successfully.");
        } catch (SQLException e) {
            showAlert("Error", "Could not add borrower: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
