package library.services;

import library.models.Book;
import library.models.User;
import library.models.Transaction;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;


public class LibraryService {
    private final Connection conn;

    //constructor 
    public LibraryService() {
        this.conn = DBConnection.getConnection();

    }

    public boolean registerUser(String username, String password, String name) {
        String sql = "INSERT INTO users (username, password, name, role) VALUES (?, ?, ?, 'member')";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.executeUpdate();
            return true;
        }
        catch(SQLException e) {
            System.out.println("Register failed: " + e.getMessage());
            return false;
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT id, username, name, password, role FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                }
                else {
                    System.out.println("Invalid credentials.");
                }
            }
        }
        catch(SQLException e){
            System.out.println("LOGIN FAILED: " + e.getMessage());
        }
        return null;
    }

    public boolean addBook(String title, String author) {
        String sql ="INSERT INTO books (title, author, isIssued) VALUES (?, ?, TRUE)";
    
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, title);
            ps.setString(2, author);
            ps.executeUpdate();
            System.out.println("Book added successfully!");
            return true;
        }
        catch(SQLException e) {
            System.out.println("Add book failed: " + e.getMessage());
            return false;
        }    
    }

    public List<Book> listAllBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT id, title, author, isIssued FROM books ORDER BY id";
        try(Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
                while(rs.next()) {
                    list.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("isIssued")
                    ));
                }
            }
            catch(SQLException e) {
                System.out.println("List books failed: " + e.getMessage());
            }
            return list;
    }

    public boolean issueBook(int userId, int bookId) {
        String checkSql = "SELECT isIssued FROM books WHERE id = ?";
        String insertTxn = "INSERT INTO transactions (user_id, book_id, issue_date) VALUES (?, ?, CURDATE())";
        String updateBook = "UPDATE books SET isIssued = TRUE WHERE id = ?";

        try(PreparedStatement check = conn.prepareStatement(checkSql)) {
            check.setInt(1, bookId);
            try(ResultSet rs = check.executeQuery()) {
                if(rs.next()) {
                    boolean isIssued = rs.getBoolean("isIssued");
                    if (isIssued) {
                        System.out.println("Book is not available");
                        return false;
                    }
                }
                else {
                    System.out.println("Book not found.");
                    return false;
                }
            }

            //insert transaction and update book in a transaction
            conn.setAutoCommit(false);
            try(PreparedStatement psTxn = conn.prepareStatement(insertTxn);
                PreparedStatement psUpd = conn.prepareStatement(updateBook)) {
                psTxn.setInt(1, userId);
                psTxn.setInt(2, bookId);
                psTxn.executeUpdate();

                psUpd.setInt(1, bookId);
                psUpd.executeUpdate();

                conn.commit();
                return true; 
            }
            catch(SQLException e) {
                conn.rollback();
                System.out.println("Issue failed: " + e.getMessage());
                return false;
            }
            finally {
                conn.setAutoCommit(true);
            }
        }
        catch(SQLException e) {
            System.out.println("Issue operation error: " + e.getMessage());
            return false;
        }
    }

    public boolean returnBook(int userId, int bookId) {
         String findSql = "SELECT id FROM transactions WHERE user_id=? AND book_id=? AND return_date IS NULL";
        String updateTxn = "UPDATE transactions SET return_date = CURDATE() WHERE id = ?";
        String updateBook = "UPDATE books SET isIssued = FALSE WHERE id = ?";

        try (PreparedStatement find = conn.prepareStatement(findSql)) {
            find.setInt(1, userId);
            find.setInt(2, bookId);
            try (ResultSet rs = find.executeQuery()) {
                if (rs.next()) {
                    int txnId = rs.getInt("id");

                    conn.setAutoCommit(false);
                    try (PreparedStatement updTxn = conn.prepareStatement(updateTxn);
                         PreparedStatement updBook = conn.prepareStatement(updateBook)) {
                        updTxn.setInt(1, txnId);
                        updTxn.executeUpdate();

                        updBook.setInt(1, bookId);
                        updBook.executeUpdate();

                        conn.commit();
                        return true;
                    } catch (SQLException ex) {
                        conn.rollback();
                        System.out.println("Return failed: " + ex.getMessage());
                        return false;
                    } finally {
                        conn.setAutoCommit(true);
                    }
                } else {
                    System.out.println("No active issue found for this user and book.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Return operation error: " + e.getMessage());
            return false;
        }
    }

    //transactions
    public List<Transaction> listAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT id, user_id, book_id, issue_date, return_date FROM transactions ORDER BY id DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Transaction(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("book_id"),
                    rs.getDate("issue_date"),
                    rs.getDate("return_date")
                ));
            }
        } catch (SQLException e) {
            System.out.println("List transactions failed: " + e.getMessage());
        }
        return list;
    }
    public List<Transaction> listUserTransactions(int userId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT id, user_id, book_id, issue_date, return_date FROM transactions WHERE user_id = ? ORDER BY id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Transaction(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("book_id"),
                        rs.getDate("issue_date"),
                        rs.getDate("return_date")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("List user transactions failed: " + e.getMessage());
        }
        return list;
    }

    // utility: get Book by id as object
    public Book getBookById(int bookId) {
        String sql = "SELECT id, title, author, isIssued FROM books WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("isIssued")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Get book failed: " + e.getMessage());
        }
        return null;
    }

    // utility: check if username exists
    public boolean usernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("usernameExists error: " + e.getMessage());
            return true;
        }
    }
}
