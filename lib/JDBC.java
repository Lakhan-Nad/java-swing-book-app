package lib;

import java.sql.*;
import java.util.*;

public class JDBC {
    final static String DATABASE_STRING = "jdbc:derby://localhost:1527/booksdb;create=true";
    private static Connection conn = null;

    public static void main(String[] args) {
        start();
        Book.addBook("Lakhan", "javaBook");
        Book.addBook("Lakhan", "Ramayan");

        System.out.println("All Added Books are: ");
        Book.getAllBooks().forEach(book -> {
            System.out.println("ID: " + book.getId() + " Title: " + book.getTitle() + " Author: " + book.getAuthor());
        });
        close();
    }

    public static void start() {
        try {
            conn = DriverManager.getConnection(DATABASE_STRING);
            System.out.println("DATABASE CONNECTED SUCCESSFULLY");
            createTables();
        } catch (SQLException ex) {
            if (ex.getSQLState().equals("XJ015")) {
                System.out.println("DERBY SHUTDOWN NORMALLY");
            } else {
                ex.printStackTrace();
            }
        }
    }

    public static void close() {
        try {
            deleteTables();
            conn.close();
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            if (ex.getSQLState().equals("XJ015")) {
                System.out.println("DERBY SHUTDOWN NORMALLY");
            } else {
                ex.printStackTrace();
            }
        }
    }

    private static boolean doesTableExists(String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet result = meta.getTables(null, null, tableName.toUpperCase(), null);
        Boolean ans = result.next();
        result.close();
        return ans;
    }

    private static void createTables() throws SQLException {
        // Create books table
        Statement statement = conn.createStatement();
        String sql;
        if (!doesTableExists("books")) {
            sql = "CREATE TABLE books (id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1), title VARCHAR(128), author VARCHAR(240), CONSTRAINT books_primary_key PRIMARY KEY (id))";
            statement.execute(sql);
            System.out.println("CREATED TABLE books.");
        }

        if (!doesTableExists("purchases")) {
            sql = "CREATE TABLE purchases (purchase_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1), username VARCHAR(240), book_id INT REFERENCES books(id), CONSTRAINT purchases_primary_key PRIMARY KEY (purchase_id))";
            statement.execute(sql);
            System.out.println("CREATED TABLE purchases.");
        }
        statement.close();
        System.out.println("CREATED TABLES SUCCESSFULLY");
    }

    private static void deleteTables() throws SQLException {
        Statement statement = conn.createStatement();
        String sql;
        // delete table purchases
        if (doesTableExists("purchases")) {
            sql = "DROP TABLE purchases";
            statement.execute(sql);
            System.out.println("DELETED TABLE purchases.");
        }
        // delete table books
        if (doesTableExists("books")) {
            sql = "DROP TABLE books";
            statement.execute(sql);
            System.out.println("DELETED TABLE books.");
        }
        statement.close();
        System.out.println("DELETED TABLES SUCCESSFULLY");
    }

    public static int insertBook(String author, String title) {
        try {
            Statement statement = conn.createStatement();
            String sql = "INSERT INTO books(title, author) VALUES ('" + title + "','" + author + "')";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int pk = rs.getInt(1);
            rs.close();
            statement.close();
            return pk;
        } catch (Exception e) {
            return -1;
        }
    }

    public static ArrayList<Book> selectAllBooks() {
        try {
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM books";
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<Book> books = new ArrayList<>();
            while (rs.next()) {
                books.add(new Book(rs.getInt("id"), rs.getString("author"), rs.getString("title")));
            }
            rs.close();
            statement.close();
            return books;
        } catch (Exception e) {
            return null;
        }
    }
}
