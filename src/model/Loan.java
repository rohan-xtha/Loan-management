package model;

import java.time.LocalDate;

public class Loan {
    private int loanId;
    private int borrowerId;
    private String borrowerName; // For convenience in TableView
    private double amount;
    private double interestRate;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private String status; // pending, partial, cleared
    private String type; // Money, Item
    private String riskLevel; // Low, Medium, High (Calculated by AI logic)
    private String description; // Description of item or notes

    public Loan() {}

    public Loan(int loanId, int borrowerId, String borrowerName, double amount, double interestRate, 
                LocalDate loanDate, LocalDate dueDate, String status, String type, String riskLevel, String description) {
        this.loanId = loanId;
        this.borrowerId = borrowerId;
        this.borrowerName = borrowerName;
        this.amount = amount;
        this.interestRate = interestRate;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.status = status;
        this.type = type;
        this.riskLevel = riskLevel;
        this.description = description;
    }

    // Getters and Setters
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }

    public int getBorrowerId() { return borrowerId; }
    public void setBorrowerId(int borrowerId) { this.borrowerId = borrowerId; }

    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
