package lib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class Book {
    private String author;
    private String title;
    private int id;

    Book(int id, String author, String title) {
        this.id = id;
        this.author = author;
        this.title = title;
    }

    public static Book addBook(String author, String title) {
        int id = JDBC.insertBook(author, title);
        if (id == -1){
            return  null;
        }
        Book book = new Book(id, author, title);
        System.out.println("Book Added with Id: "+ id + "Author: " + author + " Title: " + title);
        return book;
    }

    public static ArrayList<Book> getAllBooks() {
        return JDBC.selectAllBooks();
    }

    public String getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static ObservableList<Book> getObservable() {
        return FXCollections.observableArrayList(getAllBooks());
    }
}