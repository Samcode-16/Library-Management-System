package library.main;

import library.services.LibraryService;
import library.utils.InputUtils;
import library.models.User;
import library.models.Admin;
import library.models.Book;
import library.models.Transaction;
import library.services.DBConnection;

import java.util.List;

public class LibraryApp {
    private static final LibraryService service = new LibraryService();

    public static void main(String[] args) {
        System.out.println("=== Welcome to Library Management System ===");

        // main loop
        while (true) {
            System.out.println("\n1) Login");
            System.out.println("2) Register (Member)");
            System.out.println("3) Exit");
            int choice = InputUtils.getInt("Choose: ");

            switch (choice) {
                case 1 -> handleLogin();
                case 2 -> handleRegister();
                case 3 -> {
                    DBConnection.closeConnection();
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void handleRegister() {
        System.out.println("\n--- Register New Member ---");
        String name = InputUtils.getString("Full name: ");
        String username = InputUtils.getString("Choose username: ");
        if (service.usernameExists(username)) {
            System.out.println("Username already taken.");
            return;
        }
        String password = InputUtils.getString("Choose password: ");
        boolean ok = service.registerUser(username, password, name);
        System.out.println(ok ? "Registered successfully. You can login now." : "Registration failed.");
    }

    private static void handleLogin() {
        System.out.println("\n--- Login ---");
        String username = InputUtils.getString("Username: ");
        String password = InputUtils.getString("Password: ");
        User user = service.login(username, password);

        if (user == null) {
            System.out.println("Invalid credentials.");
            return;
        }

        System.out.println("Logged in as: " + user.getName() + " (" + user.getRole() + ")");
        if ("admin".equalsIgnoreCase(user.getRole())) {
            adminMenu(user);
        } else {
            memberMenu(user);
        }
    }

    // admin menu
    private static void adminMenu(User user) {
        Admin admin = new Admin(user.getId(), user.getName(), user.getUsername(), user.getPassword());
    
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1) View Books");
            System.out.println("2) Add Book");
            System.out.println("3) Update Book");
            System.out.println("4) Delete Book");
            System.out.println("5) Logout");
            int ch = InputUtils.getInt("Choose: ");

            switch (ch) {
                case 1 -> admin.viewBooks();
                case 2 -> admin.addBook();
                case 3 -> admin.updateBook();
                case 4 -> admin.deleteBook();
                case 5 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    //member menu
    private static void memberMenu(User user) {
        while (true) {
            System.out.println("\n--- Member Menu ---");
            System.out.println("1) View Books");
            System.out.println("2) Issue Book");
            System.out.println("3) Return Book");
            System.out.println("4) My Transactions");
            System.out.println("5) Logout");
            int ch = InputUtils.getInt("Choose: ");
            switch (ch) {
                case 1 -> displayBooks();
                case 2 -> {
                    displayBooks();
                    int bookId = InputUtils.getInt("Enter Book ID to issue: ");
                    boolean ok = service.issueBook(user.getId(), bookId);
                    System.out.println(ok ? "Book issued." : "Issue failed.");
                }
                case 3 -> {
                    int bookId = InputUtils.getInt("Enter Book ID to return: ");
                    boolean ok = service.returnBook(user.getId(), bookId);
                    System.out.println(ok ? "Book returned." : "Return failed.");
                }
                case 4 -> displayUserTransactions(user.getId());
                case 5 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    //displays books
    private static void displayBooks() {
        List<Book> books = service.listAllBooks();
        System.out.println("\n--- Books ---");
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        for (Book b : books) {
            System.out.println(b);
        }
    }

    private static void displayAllTransactions() {
        List<Transaction> tx = service.listAllTransactions();
        System.out.println("\n--- All Transactions ---");
        if (tx.isEmpty()) { System.out.println("No transactions."); return; }
        for (Transaction t : tx) System.out.println(t);
    }

    private static void displayUserTransactions(int userId) {
        List<Transaction> tx = service.listUserTransactions(userId);
        System.out.println("\n--- Your Transactions ---");
        if (tx.isEmpty()) { System.out.println("You have no transactions."); return; }
        for (Transaction t : tx) System.out.println(t);
    }
}
