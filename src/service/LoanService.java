package service;

import model.Borrower;
import model.Loan;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanService {

    // In-memory fallback for demo mode
    private static List<Borrower> demoBorrowers = new ArrayList<>();
    private static List<Loan> demoLoans = new ArrayList<>();
    private static int borrowerIdCounter = 1;
    private static int loanIdCounter = 1;

    // --- Borrower CRUD ---

    public int addBorrower(Borrower borrower) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            borrower.setBorrowerId(borrowerIdCounter++);
            demoBorrowers.add(borrower);
            return borrower.getBorrowerId();
        }

        String sql = "INSERT INTO borrowers (name, contact) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, borrower.getName());
            pstmt.setString(2, borrower.getContact());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } finally {
            conn.close();
        }
        return -1;
    }

    public List<Borrower> getAllBorrowers() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            return new ArrayList<>(demoBorrowers);
        }

        List<Borrower> borrowers = new ArrayList<>();
        String sql = "SELECT * FROM borrowers";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                borrowers.add(new Borrower(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact")
                ));
            }
        } finally {
            conn.close();
        }
        return borrowers;
    }

    // --- Loan CRUD ---

    public void addLoan(Loan loan) throws SQLException {
        // First, calculate risk level using AI logic
        loan.setRiskLevel(calculateRiskLevel(loan));

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            loan.setLoanId(loanIdCounter++);
            // Find borrower name for the loan object (convenience)
            for (Borrower b : demoBorrowers) {
                if (b.getBorrowerId() == loan.getBorrowerId()) {
                    loan.setBorrowerName(b.getName());
                    break;
                }
            }
            demoLoans.add(loan);
            return;
        }

        String sql = "INSERT INTO loans (borrower_id, amount, interest_rate, loan_date, due_date, status, type, risk_level, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loan.getBorrowerId());
            pstmt.setDouble(2, loan.getAmount());
            pstmt.setDouble(3, loan.getInterestRate());
            pstmt.setDate(4, Date.valueOf(loan.getLoanDate()));
            pstmt.setDate(5, loan.getDueDate() != null ? Date.valueOf(loan.getDueDate()) : null);
            pstmt.setString(6, loan.getStatus());
            pstmt.setString(7, loan.getType());
            pstmt.setString(8, loan.getRiskLevel());
            pstmt.setString(9, loan.getDescription());
            pstmt.executeUpdate();
        } finally {
            conn.close();
        }
    }

    public List<Loan> getAllLoans() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            return new ArrayList<>(demoLoans);
        }

        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.name as borrower_name FROM loans l JOIN borrowers b ON l.borrower_id = b.id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                loans.add(new Loan(
                        rs.getInt("id"),
                        rs.getInt("borrower_id"),
                        rs.getString("borrower_name"),
                        rs.getDouble("amount"),
                        rs.getDouble("interest_rate"),
                        rs.getDate("loan_date").toLocalDate(),
                        rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null,
                        rs.getString("status"),
                        rs.getString("type"),
                        rs.getString("risk_level"),
                        rs.getString("description")
                ));
            }
        } finally {
            conn.close();
        }
        return loans;
    }

    public void updateLoan(Loan loan) throws SQLException {
        loan.setRiskLevel(calculateRiskLevel(loan));

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            for (int i = 0; i < demoLoans.size(); i++) {
                if (demoLoans.get(i).getLoanId() == loan.getLoanId()) {
                    demoLoans.set(i, loan);
                    break;
                }
            }
            return;
        }

        String sql = "UPDATE loans SET amount=?, interest_rate=?, due_date=?, status=?, type=?, risk_level=?, description=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, loan.getAmount());
            pstmt.setDouble(2, loan.getInterestRate());
            pstmt.setDate(3, loan.getDueDate() != null ? Date.valueOf(loan.getDueDate()) : null);
            pstmt.setString(4, loan.getStatus());
            pstmt.setString(5, loan.getType());
            pstmt.setString(6, loan.getRiskLevel());
            pstmt.setString(7, loan.getDescription());
            pstmt.setInt(8, loan.getLoanId());
            pstmt.executeUpdate();
        } finally {
            conn.close();
        }
    }

    public void deleteLoan(int loanId) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            demoLoans.removeIf(l -> l.getLoanId() == loanId);
            return;
        }

        String sql = "DELETE FROM loans WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loanId);
            pstmt.executeUpdate();
        } finally {
            conn.close();
        }
    }

    // --- Rule-Based AI Logic ---

    public String calculateRiskLevel(Loan loan) throws SQLException {
        int pendingLoans = 0;
        int overdueLoans = 0;

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            for (Loan l : demoLoans) {
                if (l.getBorrowerId() == loan.getBorrowerId() && !"cleared".equals(l.getStatus())) {
                    pendingLoans++;
                    if (l.getDueDate() != null && l.getDueDate().isBefore(LocalDate.now())) {
                        overdueLoans++;
                    }
                }
            }
        } else {
            String sql = "SELECT status, due_date FROM loans WHERE borrower_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, loan.getBorrowerId());
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String status = rs.getString("status");
                        Date dueDate = rs.getDate("due_date");
                        if (!"cleared".equals(status)) {
                            pendingLoans++;
                            if (dueDate != null && dueDate.toLocalDate().isBefore(LocalDate.now())) {
                                overdueLoans++;
                            }
                        }
                    }
                }
            } finally {
                conn.close();
            }
        }

        // Rules
        if (overdueLoans > 0) return "High";
        if (pendingLoans > 2) return "High";
        if (pendingLoans > 0) return "Medium";
        if (loan.getAmount() > 10000) return "Medium"; // Arbitrary threshold
        return "Low";
    }

    public String getAISuggestion(Loan loan) {
        if ("High".equals(loan.getRiskLevel())) {
            return "Suggestion: This borrower has a high risk profile. Consider asking for collateral or a shorter repayment period.";
        } else if ("Medium".equals(loan.getRiskLevel())) {
            return "Suggestion: Moderate risk. Ensure the due date is clear and send a reminder a week before.";
        } else {
            return "Suggestion: Low risk. This seems like a safe transaction.";
        }
    }

    public String getReminderMessage(Loan loan) {
        if ("cleared".equals(loan.getStatus())) return "";
        
        LocalDate today = LocalDate.now();
        if (loan.getDueDate() != null && loan.getDueDate().isBefore(today)) {
            return "Polite Reminder: Hello " + loan.getBorrowerName() + ", just a gentle reminder that your loan of " + loan.getAmount() + " was due on " + loan.getDueDate() + ". Please let us know when you can settle it.";
        } else if (loan.getDueDate() != null && loan.getDueDate().isBefore(today.plusDays(3))) {
            return "Polite Reminder: Hi " + loan.getBorrowerName() + ", your loan repayment is due in a few days (" + loan.getDueDate() + "). Just wanted to keep you informed!";
        }
        return "No immediate reminder needed.";
    }
}
