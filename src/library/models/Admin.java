package library.models;

import library.services.DBConnection;
import java.sql.*;
import java.util.Scanner;

public class Admin extends User {
    private Connection conn;

    public Admin() {
        super();
        conn = DBConnection.getConnection();
    }

    public Admin(int id, String name, String username, String password) {
        super(id, name, username, password, "ADMIN");
        conn = DBConnection.getConnection();
    }

    public Admin(String name, String username, String password) {
        super(name, username, password, "ADMIN");
    }

    //Admin specific methods
    public void addBook() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Add New Book ---");
        System.out.print("Book title: ");
        String title = sc.nextLine();
        System.out.print("Author: ");
        String author = sc.nextLine();

        String sql = "INSERT INTO books (title, author, isIssued) VALUES (?, ?, 0)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, author);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Book added successfully!");
            } else {
                System.out.println("Failed to add book.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    //view all books
    public void viewBooks() {
        String sql = "SELECT * FROM books";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- Library Books ---");
            System.out.printf("%-5s %-30s %-25s %-10s%n", "ID", "Title", "Author", "Status");
            System.out.println("---------------------------------------------------------------");

            while (rs.next()) {
                String status = rs.getBoolean("isIssued") ? "Available" : "Issued";
                System.out.printf("%-5d %-30s %-25s %-10s%n",
                        rs.getInt("id"), rs.getString("title"), rs.getString("author"), status);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving books: " + e.getMessage());
        }
    }

    //update book details
    public void updateBook() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Book ID to update: ");
        int id = sc.nextInt();
        sc.nextLine(); // clear buffer
        System.out.print("New Title: ");
        String newTitle = sc.nextLine();
        System.out.print("New Author: ");
        String newAuthor = sc.nextLine();

        String sql = "UPDATE books SET title = ?, author = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newTitle);
            ps.setString(2, newAuthor);
            ps.setInt(3, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Book updated successfully!");
            } else {
                System.out.println("No book found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating book: " + e.getMessage());
        }
    }

    //delete book
    public void deleteBook() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Book ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("No book found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }
}