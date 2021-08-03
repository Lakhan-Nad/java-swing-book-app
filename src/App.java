package src;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lib.JDBC;
import lib.Book;

public class App extends Application {

    public static void main(String[] args) {
        JDBC.start();
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();
        // add a new Book Tab
        Tab addTab = new Tab("Add Book");
        addTab.closableProperty().set(false);
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Enter Details of Book to Add");
        sceneTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        pane.add(sceneTitle, 0, 0, 2, 1);
        Label titleLabel = new Label("Title:");
        pane.add(titleLabel, 0, 1);
        final TextField titleField = new TextField();
        pane.add(titleField, 1, 1);
        Label authorLabel = new Label("Author:");
        pane.add(authorLabel, 0, 2);
        final TextField authorField = new TextField();
        pane.add(authorField, 1, 2);
        Button calculateButton = new Button("Add Book");
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        hbox.getChildren().add(calculateButton);
        pane.add(hbox, 1, 4);
        final Text taxMessage = new Text();
        pane.add(taxMessage, 1, 6);
        calculateButton.setOnAction(t -> {
            Book book = Book.addBook(authorField.getText(), titleField.getText());
            if (book != null) {
                titleField.clear();
                authorField.clear();
                taxMessage.setText("Book Added with Id " + book.getId());
            } else {
                taxMessage.setText("Unable to add Book");
            }
        });
        addTab.setContent(pane);

        // See Books Tab
        Tab seeTab = new Tab("View Books");
        seeTab.closableProperty().set(false);
        GridPane pane2 = new GridPane();
        pane2.setAlignment(Pos.CENTER);
        Text pane2Title = new Text("Book List");
        pane2Title.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        pane2.add(pane2Title, 0, 0);
        TableView<Book> table = new TableView<>();
        TableColumn<Book, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        table.getColumns().addAll(idColumn, titleColumn, authorColumn);
        table.setItems(Book.getObservable());
        Button loadAgainButton = new Button("Load Again");
        loadAgainButton.setOnAction(t -> {
            table.setItems(Book.getObservable());
        });
        pane2.add(table,0, 3);
        pane2.add(loadAgainButton,0,7);
        seeTab.setContent(pane2);
        // add tabs
        tabPane.getTabs().add(addTab);
        tabPane.getTabs().add(seeTab);

        // create scene
        Scene scene = new Scene(tabPane, 600, 500);

        // set the scene
        stage.setScene(scene);
        stage.show();
    }
}
