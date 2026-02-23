CREATE DATABASE IF NOT EXISTS loan_tracker;

USE loan_tracker;

CREATE TABLE IF NOT EXISTS borrowers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS loans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    borrower_id INT,
    amount DOUBLE NOT NULL,
    interest_rate DOUBLE DEFAULT 0,
    loan_date DATE NOT NULL,
    due_date DATE,
    status ENUM('pending', 'partial', 'cleared') DEFAULT 'pending',
    type ENUM('Money', 'Item') DEFAULT 'Money',
    risk_level VARCHAR(20),
    description TEXT,
    FOREIGN KEY (borrower_id) REFERENCES borrowers(id) ON DELETE CASCADE
);
