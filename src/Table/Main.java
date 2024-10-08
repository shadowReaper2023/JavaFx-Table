package Table;

import JDBC.DBConnection;
import Person.Person;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    DBConnection dbConnection = new DBConnection();
    TableView<Person> tableView = new TableView<>();
    ObservableList<Person> personList = FXCollections.observableArrayList();

    // Input fields
    TextField inputFirstName = new TextField();
    TextField inputLastName = new TextField();
    TextField inputEmail = new TextField();
    TextField inputPhoneNumber = new TextField();
    TextField inputAddress = new TextField();
    TextField inputId = new TextField();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Person Table CRUD");

        // Table columns for Person
        TableColumn<Person, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());

        TableColumn<Person, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setCellValueFactory(data -> data.getValue().firstNameProperty());

        TableColumn<Person, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setCellValueFactory(data -> data.getValue().lastNameProperty());

        TableColumn<Person, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());

        TableColumn<Person, String> colPhoneNumber = new TableColumn<>("Phone Number");
        colPhoneNumber.setCellValueFactory(data -> data.getValue().phoneNumberProperty());

        TableColumn<Person, String> colAddress = new TableColumn<>("Address");
        colAddress.setCellValueFactory(data -> data.getValue().addressProperty());

        tableView.getColumns().addAll(colId, colFirstName, colLastName, colEmail, colPhoneNumber, colAddress);

        // Set prompt texts for input fields
        inputFirstName.setPromptText("First Name");
        inputLastName.setPromptText("Last Name");
        inputEmail.setPromptText("Email");
        inputPhoneNumber.setPromptText("Phone Number");
        inputAddress.setPromptText("Address");
        inputId.setPromptText("ID");

        // Buttons for CRUD operations
        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button viewButton = new Button("View");

        HBox hbox = new HBox(10, inputId, inputFirstName, inputLastName, inputEmail, inputPhoneNumber, inputAddress, addButton, updateButton, deleteButton, viewButton);
        hbox.setPadding(new Insets(10));

        VBox vbox = new VBox(10, tableView, hbox);
        vbox.setPadding(new Insets(10));

        // Add button functionality
        addButton.setOnAction(e -> {
            String firstName = inputFirstName.getText();
            String lastName = inputLastName.getText();
            String email = inputEmail.getText();
            String phoneNumber = inputPhoneNumber.getText();
            String address = inputAddress.getText();
            dbConnection.create(firstName, lastName, email, phoneNumber, address);  // Insert into Person table
            loadData();  // Refresh table
            clearFields(); // Clear input fields
        });

        // Update button functionality
        updateButton.setOnAction(e -> {
            int id = Integer.parseInt(inputId.getText());
            String firstName = inputFirstName.getText();
            String lastName = inputLastName.getText();
            String email = inputEmail.getText();
            String phoneNumber = inputPhoneNumber.getText();
            String address = inputAddress.getText();
            dbConnection.update(id, firstName, lastName, email, phoneNumber, address);
            loadData();
            clearFields(); // Clear input fields
        });

        // Delete button functionality
        deleteButton.setOnAction(e -> {
            int id = Integer.parseInt(inputId.getText());
            dbConnection.delete(id);
            loadData();
            clearFields(); // Clear input fields
        });

        // View button functionality
        viewButton.setOnAction(e -> {
            String idText = inputId.getText();
            if (idText.isEmpty()) {
                loadData(); // Load all data if ID is not provided
            } else {
                int id = Integer.parseInt(idText);
                viewPerson(id); // Load specific person by ID
            }
        });

        // Create the scene and show the stage
        Scene scene = new Scene(vbox, 900, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadData() {
        personList.clear();
        personList.addAll(dbConnection.read());  // Add the fetched data to the ObservableList
        tableView.setItems(personList);  // Set the items in the TableView
    }

    private void viewPerson(int id) {
        personList.clear();
        Person person = dbConnection.read().stream()
            .filter(p -> p.getId() == id)
            .findFirst()
            .orElse(null);
        
        if (person != null) {
            personList.add(person); // Add the found person to the list
        } else {
            showAlert("Person Not Found", "No person found with ID: " + id);
        }
        tableView.setItems(personList);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        inputId.clear();
        inputFirstName.clear();
        inputLastName.clear();
        inputEmail.clear();
        inputPhoneNumber.clear();
        inputAddress.clear();
    }
}