# 📚 Library Management System  

A **Java-based Library Management System** that helps manage books, users, and transactions efficiently.  
This project demonstrates database connectivity, object-oriented design, and modular programming in Java.

---

## Features

  **Admin and Member roles**
  - Admin can add, remove, or view all books and members.
  - Members can view, issue, and return books.
  **Book Management**
  - Add, list, and track book availability.
  **User Authentication**
  - Secure login and registration system.
  **Database Integration**
  - Uses MySQL for persistent data storage.
  **Transaction History**
  - Tracks book issues and returns.
  **Configurable Properties**
  - Database connection managed via `db.properties` file.

---

## Project Structure

LibraryManagementSystem/
│
├── bin/ # Compiled .class files
├── database/
│ └── library_db.sql # SQL schema for database setup
├── images/ # Optional assets
├── lib/
│ └── mysql-connector-j-9.5.0.jar # MySQL JDBC driver
├── src/
│ └── library/
│ ├── config/ # Configuration files
│ ├── main/ # Main app entry point
│ ├── models/ # Java model classes
│ ├── services/ # Business logic classes
│ └── utils/ # Utility classes
├── .gitignore
└── README.md


---

##  Technologies Used

| Category     | Tools / Frameworks          |
| ------------ | --------------------------- |
| Language     | Java (JDK 17+)              |
| Database     | MySQL                       |
| Connector    | MySQL Connector/J (v9.5.0)  |
| IDE / Editor | VS Code, IntelliJ IDEA      |
| Build / Run  | Command Line (javac / java) |

---

##  Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/Samcode-16/Library-Management-System.git
cd Library-Management-System

### 2. Set Up the Database


1. Open MySQL and create a database:

```MYSQL
CREATE DATABASE library_db;
USE library_db;

2. Import the SQL file:
```MYSQL
SOURCE database/library_db.sql;

1. Configure Database Connection

Edit src/library/config/db.properties:

```db.url=jdbc:mysql://localhost:3306/library_db
db.username=your_username
db.password=your_password```

4. Compile the Project

```javac -d bin -cp "lib/mysql-connector-j-9.5.0.jar" src/library/**/*.java```

5. Run the Application

```java -cp "bin;lib/mysql-connector-j-9.5.0.jar" library.main.LibraryApp```

| Class                                        | Responsibility                             |
| -------------------------------------------- | ------------------------------------------ |
| `LibraryApp.java`                            | Main entry point; handles menu navigation  |
| `LibraryService.java`                        | Core logic for issuing and returning books |
| `DBConnection.java`                          | Establishes database connection            |
| `Book.java`, `User.java`, `Transaction.java` | Data models for system entities            |
| `Admin.java`                                 | Admin user model with elevated privileges  |

Sample Console Output

=== Welcome to Library Management System ===
1) Login
2) Register (Member)
3) Exit

--- Member Menu ---
1) View Books
2) Issue Book
3) Return Book
4) My Transactions
5) Logout

Future Enhancements

📊 GUI using JavaFX or Swing
🌐 Web version using Spring Boot
📱 Mobile-friendly interface
🧾 Export reports in PDF/CSV

Author

**Samudyatha K Bhat**
Student Developer