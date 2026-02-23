# Digital Borrow and Lend Tracker - Project Documentation

**Date:** February 22, 2026  
**Author:** [Your Name/Team Name]  
**Course:** [Course Name]  
**Submitted to:** [Instructor Name]

---

## 1. Cover Letter

**To:**  
[Instructor Name]  
[Department Name]  
[College/University Name]

**Subject:** Submission of Project Report on "Digital Borrow and Lend Tracker"

Respected Sir/Madam,

I am pleased to submit the project report for the "Digital Borrow and Lend Tracker" as part of the requirements for the [Course Name/Semester]. This project aims to digitize and simplify the management of informal lending and borrowing records, addressing the common issues of lost data and calculation errors in manual record-keeping.

Through this project, I have applied core concepts of Object-Oriented Programming (OOP), Database Management, and User Interface Design using Java and JavaFX. The system includes features for tracking loans, calculating interest, and providing AI-based risk assessments.

I hope this report meets the expected standards and demonstrates the effort put into understanding and solving the problem.

Sincerely,  
[Your Name]  
[Your Roll Number]

---

## 2. Table of Contents

1.  Introduction
2.  Problem Description
3.  Requirement Analysis
4.  UML Diagrams
5.  Database Design
6.  User Interface Design
7.  Design Pattern Discussion
8.  OOP and Data Structure Concepts
9.  Technological Stack
10. Conclusion

---

## 3. List of Abbreviations

*   **UI:** User Interface
*   **UX:** User Experience
*   **DB:** Database
*   **SQL:** Structured Query Language
*   **OOP:** Object-Oriented Programming
*   **MVC:** Model-View-Controller
*   **ERD:** Entity Relationship Diagram
*   **IDE:** Integrated Development Environment
*   **AI:** Artificial Intelligence (Rule-based in this context)

---

## 4. Introduction

The **Digital Borrow and Lend Tracker** is a desktop application designed to help individuals or small lenders manage their lending and borrowing activities efficiently. In many informal settings, transactions are recorded on paper or simply remembered, leading to disputes, forgotten debts, and calculation errors. This system provides a digital solution to record, track, and analyze these financial interactions.

The application allows users to add borrower details, record new loans, update statuses (e.g., Paid, Pending), and automatically calculate interest based on the principal amount and rate. Additionally, it features a rule-based AI module that assesses the risk level of a loan based on the amount and interest rate, offering suggestions to the lender.

---

## 5. Problem Description

### Context
Informal lending (borrowing among friends, family, or small circles) is a common practice. However, the management of these transactions is often chaotic. Lenders rely on memory or physical notebooks, which are prone to damage, loss, or unauthorized access.

### Problem Statement
The current manual system suffers from:
*   **Data Loss:** Physical notebooks can be lost or damaged.
*   **Calculation Errors:** Manually calculating interest and total amounts due is error-prone.
*   **Lack of Insights:** It is difficult to quickly see who owes the most or which loans are high-risk.
*   **Inaccessibility:** Records are not easily searchable or sortable.

### Target Users
*   **Individual Lenders:** People who frequently lend money to friends or acquaintances.
*   **Small Business Owners:** Those who offer credit to customers and need a simple tracker.
*   **Students/Roommates:** To track shared expenses and small debts.

---

## 6. Requirement Analysis

### Functional Requirements
1.  **Borrower Management:**
    *   Add new borrowers with Name and Contact information.
    *   View a list of all borrowers.
2.  **Loan Management:**
    *   Create new loan records linked to a specific borrower.
    *   Input details: Amount, Interest Rate, Due Date, Type (Money/Item).
    *   Update loan status (Pending, Paid, Overdue).
    *   Delete loan records.
3.  **Search and Filter:**
    *   Search for loans by borrower name (future implementation placeholder in UI).
    *   Filter loans by status.
4.  **Risk Analysis (AI Feature):**
    *   Automatically calculate "Risk Level" (Low, Medium, High) based on loan amount and interest rate.
    *   Provide lending suggestions (e.g., "Safe to lend", "Review borrower history").
5.  **Interest Calculation:**
    *   System automatically calculates the interest amount based on the rate provided.

### Non-Functional Requirements
1.  **Usability:** The interface is designed with JavaFX to be intuitive, using clear labels, modern styling, and logical grouping of fields.
2.  **Performance:** The system uses a local MySQL database (or in-memory fallback) for instant data retrieval and updates.
3.  **Reliability:** Data is persisted in a database to prevent loss upon application closure. Input validation ensures correct data types (e.g., numbers for amounts).
4.  **Scalability:** The database schema is normalized, allowing for future expansion (e.g., adding partial payments or transaction history).
5.  **Security:** Basic input validation prevents SQL injection risks (via PreparedStatements).

---

## 7. UML Diagrams

### Use Case Diagram
*   **Actor:** User (Lender/Admin)
*   **Use Cases:**
    *   Manage Borrowers (Add, Update)
    *   Manage Loans (Create, Read, Update, Delete)
    *   View Dashboard/Risk Insights
    *   Calculate Interest

### Activity Diagram (Based on provided flow)
The system follows a logical flow:
1.  **Start** -> **Login** (Conceptual)
2.  **Dashboard**: User views Loan Records.
3.  **Action**: User chooses to "Request/Add Loan".
4.  **Process**:
    *   System checks Borrower Details.
    *   User enters Loan Amount & Interest.
    *   **AI Analysis**: System analyzes borrowing patterns/risk.
    *   **Decision**:
        *   If Risk is High -> "Provide Caution Suggestion".
        *   If Risk is Low -> "Loan Successful".
5.  **End**: Loan is saved to the database.

### Class Diagram Structure
1.  **`Borrower` Class**:
    *   Attributes: `id` (int), `name` (String), `contact` (String).
    *   Methods: Getters, Setters.
2.  **`Loan` Class**:
    *   Attributes: `loanId`, `borrowerId`, `amount`, `interestRate`, `dueDate`, `status`.
    *   Methods: `calculateTotalAmount()`, Getters, Setters.
3.  **`LoanService` Class**:
    *   Methods: `addLoan()`, `getLoans()`, `analyzeRisk(Loan l)`.
    *   Responsibility: Business logic and AI rules.
4.  **`LoanController` Class**:
    *   Methods: `initialize()`, `handleAddLoan()`, `handleDelete()`.
    *   Responsibility: Handles UI events and bridges View and Model.
5.  **`DatabaseConnection` Class**:
    *   Methods: `getConnection()`.
    *   Responsibility: Manages MySQL connection (Singleton pattern).

---

## 8. Database Design

### Normalization
The database is designed up to the **Third Normal Form (3NF)** to reduce redundancy.
*   **1NF**: All columns contain atomic values.
*   **2NF**: All non-key attributes are dependent on the primary key.
*   **3NF**: No transitive dependencies (e.g., Borrower Name is stored in the `borrowers` table, not repeated in every loan record).

### ERD (Entity Relationship Diagram) Description
*   **Entity: Borrowers**
    *   Primary Key: `borrower_id` (INT, Auto Increment)
    *   Attributes: `name` (VARCHAR), `contact` (VARCHAR)
*   **Entity: Loans**
    *   Primary Key: `loan_id` (INT, Auto Increment)
    *   Foreign Key: `borrower_id` (References `borrowers.borrower_id`)
    *   Attributes: `amount` (DECIMAL), `interest_rate` (DECIMAL), `due_date` (DATE), `status` (VARCHAR)
*   **Relationship**: One Borrower **Has Many** Loans (1:N Relationship).

---

## 9. User Interface Design

The UI is built using **JavaFX** (FXML) and styled with **CSS**.

### Main Screen Layout
1.  **Left Panel (Loan Records)**:
    *   A generic `TableView` displaying all loans with columns: ID, Borrower, Amount, Interest, Date, Status, Risk.
    *   A "Quick Add Borrower" section at the bottom to easily register new people.
2.  **Right Panel (Loan Details & Action)**:
    *   **Input Form**: Fields for selecting a borrower, entering amount, interest, and due date.
    *   **Action Buttons**: Save, Update, Delete, Clear.
    *   **AI Insights Panel**: A dedicated section that displays the calculated Risk Level (High/Medium/Low) and dynamic suggestions based on the entered data.

### Navigation Flow
*   Single-window application (Dashboard style).
*   Users select a row in the table to populate the form (Master-Detail view).
*   Real-time updates: Adding a loan immediately refreshes the table.

---

## 10. Design Pattern Discussion

### MVC (Model-View-Controller)
The project strictly follows the MVC architecture:
*   **Model**: `Borrower.java`, `Loan.java` (Data structure and logic).
*   **View**: `main.fxml`, `application.css` (Visual representation).
*   **Controller**: `LoanController.java` (Handles user input and updates the Model/View).
*   **Justification**: MVC separates concerns, making the code easier to maintain, test, and expand. Changing the UI (View) doesn't break the business logic (Model).

### Singleton Pattern
*   Used in `DatabaseConnection.java`.
*   **Justification**: Ensures only one instance of the database connection is created, saving resources and preventing connection conflicts.

---

## 11. OOP and Data Structure Concepts

### Object-Oriented Programming (OOP)
1.  **Encapsulation**: All fields in `Borrower` and `Loan` classes are `private` and accessed via public getters/setters. This protects the data integrity.
2.  **Inheritance**: The `Main` class extends `javafx.application.Application` to inherit JavaFX lifecycle methods like `start()`.
3.  **Abstraction**: The `LoanService` handles the complex logic of database communication, hiding the SQL details from the Controller.

### Data Structures
1.  **ArrayList / ObservableList**:
    *   Used to store the list of loans and borrowers in memory for the UI.
    *   **Why**: `ObservableList` is essential for JavaFX `TableView` to automatically update the UI when data changes. It provides dynamic resizing and efficient access.
2.  **HashMap (Internal Logic)**:
    *   Could be used for caching borrower IDs for quick lookup (future optimization).

---

## 12. Technological Stack

*   **Programming Language**: Java (JDK 21)
*   **GUI Framework**: JavaFX (Version 21.0.2)
*   **Database**: MySQL (8.0)
*   **Build Tool**: Manual / Command Line (Javac)
*   **IDE**: VS Code / Trae (Compatible with Eclipse/IntelliJ)
*   **Design Tools**: SceneBuilder (for FXML)

---

## 13. Submission Requirements Check

*   [x] **Compiled Document**: All sections included.
*   [x] **Clear Headings**: Used Markdown headers.
*   [x] **Diagrams**: Described clearly (Images to be inserted by user).
*   [x] **Explanations**: "How and Why" provided for patterns and structures.

