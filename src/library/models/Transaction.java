package library.models;

import java.sql.Date;

public class Transaction {
    private int id;
    private int bookId;
    private int userId;
    private Date issueDate;
    private Date returnDate;

    public Transaction(int id, int bookId, int userId, Date issueDate, Date returnDate) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }

    public Transaction(int bookId, int userId, Date issueDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.issueDate = issueDate;
    }

    public int getId() { 
        return id; 
    }
    public int getBookId() { 
        return bookId; 
    }
    public int getUserId() { 
        return userId; 
    }
    public Date getIssueDate() { 
        return issueDate; 
    }
    public Date getReturnDate() { 
        return returnDate; 
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return String.format("T#%d | user:%d | book:%d | issued:%s | returned:%s",
            id, userId, bookId,
            issueDate != null ? issueDate.toString() : "null",
            returnDate != null ? returnDate.toString() : "Not returned");
    }
}
