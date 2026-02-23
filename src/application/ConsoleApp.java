package application;

import model.Borrower;
import model.Loan;
import service.LoanService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private static LoanService loanService = new LoanService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Digital Borrow and Lend Tracker (Console Version) ===");

        while (true) {
            System.out.println("\n1. Add Borrower");
            System.out.println("2. Add Loan");
            System.out.println("3. View All Loans");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            try {
                switch (choice) {
                    case 1:
                        addBorrower();
                        break;
                    case 2:
                        addLoan();
                        break;
                    case 3:
                        viewLoans();
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addBorrower() throws Exception {
        System.out.print("Enter Borrower Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Contact: ");
        String contact = scanner.nextLine();

        Borrower b = new Borrower(0, name, contact);
        int id = loanService.addBorrower(b);
        System.out.println("Borrower added with ID: " + id);
    }

    private static void addLoan() throws Exception {
        List<Borrower> borrowers = loanService.getAllBorrowers();
        if (borrowers.isEmpty()) {
            System.out.println("No borrowers found. Please add a borrower first.");
            return;
        }

        System.out.println("Select Borrower:");
        for (int i = 0; i < borrowers.size(); i++) {
            System.out.println((i + 1) + ". " + borrowers.get(i).getName());
        }
        int bIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Interest Rate (%): ");
        double interest = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Due Date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate dueDate = LocalDate.parse(dateStr);

        Loan loan = new Loan();
        loan.setBorrowerId(borrowers.get(bIndex).getBorrowerId());
        loan.setAmount(amount);
        loan.setInterestRate(interest);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(dueDate);
        loan.setStatus("pending");
        loan.setType("Money");
        loan.setDescription("Console Entry");

        loanService.addLoan(loan);
        System.out.println("Loan added successfully!");
    }

    private static void viewLoans() throws Exception {
        List<Loan> loans = loanService.getAllLoans();
        System.out.println("\n--- Loan Records ---");
        for (Loan l : loans) {
            System.out.println("ID: " + l.getLoanId() + " | Borrower: " + l.getBorrowerName() +
                    " | Amount: " + l.getAmount() + " | Status: " + l.getStatus() +
                    " | Risk: " + l.getRiskLevel());
            System.out.println("   AI Suggestion: " + loanService.getAISuggestion(l));
            System.out.println("   Reminder: " + loanService.getReminderMessage(l));
        }
    }
}
