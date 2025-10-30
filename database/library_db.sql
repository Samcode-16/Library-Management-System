CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('admin','member') DEFAULT 'member',
  name VARCHAR(100)
);

CREATE TABLE books (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255),
  isIssued BOOLEAN DEFAULT TRUE
);

CREATE TABLE transactions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  book_id INT,
  issue_date DATE,
  return_date DATE,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (book_id) REFERENCES books(id)
);


INSERT INTO users (username, password, role, name) VALUES ('admin', 'admin@password#sam123', 'admin', 'Administrator');
