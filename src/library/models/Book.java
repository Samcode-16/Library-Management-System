package library.models;

public class Book {
    private int id;
    private String title;
    private String author;
    private boolean isIssued;

    //constructor for DB results
    public Book(int id, String title, String author, boolean isIssued) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isIssued = isIssued;
    }

    // Constructor for newly added books
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isIssued = false; // default to Available
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isIssued() {
        return isIssued;
    }

    @Override
    public String toString() {
        return String.format("%d. %s by %s - %s", id, title, author, (isIssued ? "Issued" : "Available"));
    }
}