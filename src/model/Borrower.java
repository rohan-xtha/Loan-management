package model;

public class Borrower {
    private int borrowerId;
    private String name;
    private String contact;

    public Borrower() {}

    public Borrower(int borrowerId, String name, String contact) {
        this.borrowerId = borrowerId;
        this.name = name;
        this.contact = contact;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return name;
    }
}
