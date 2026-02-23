Digital Borrow and Lend Tracker
===============================

A JavaFX application for managing personal lending and borrowing records with rule-based AI for risk analysis.

Features:
- CRUD operations for Borrowers and Loans.
- Track payment status, due dates, and loan history.
- Rule-based AI for risk classification (Low, Medium, High).
- Intelligent suggestions for lending decisions.
- Polite reminder messages based on borrower behavior.
- MySQL database integration for data storage.

Setup Instructions:
1. Database:
   - Install MySQL Server.
   - Run the provided `db_setup.sql` script to create the database and tables.
   - Update `src/service/DatabaseConnection.java` with your MySQL username and password if different from the default (root / no password).

2. Java Environment:
   - Ensure JDK 11 or higher is installed.
   - JavaFX SDK is required to run the application.

3. Running the App (Console):
   - Open a terminal in the project root.
   - Run: `java -cp bin application.ConsoleApp`

4. Running the App (GUI with included JavaFX):
   - I have downloaded the JavaFX SDK to the `javafx-sdk-21.0.2` folder.
   - To run the GUI application, use the following command in your terminal:
     `java --module-path "javafx-sdk-21.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp bin application.Launcher`

Project Structure:
- `src/application`: Main entry point.
- `src/model`: Data objects (Borrower, Loan).
- `src/controller`: UI logic (LoanController).
- `src/service`: Business logic and DB connection (LoanService, DatabaseConnection).
- `resources`: UI design (main.fxml).
- `db_setup.sql`: Database schema script.

Developed as part of the Loan Management System project.
