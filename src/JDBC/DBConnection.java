package JDBC;

import Person.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/table";
    private static final String USER = "root";
    private static final String PASS = "";

    // Establishing connection
    public Connection connect() {
        Connection connection = null;
        try {
            // Register Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    // CREATE method
    public void create(String firstName, String lastName, String email, String phoneNumber, String address) {
        String query = "INSERT INTO Person (firstName, lastName, email, phoneNumber, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, address);
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ method
    public List<Person> read() {
    // Use the typed ArrayList to avoid the warning
    List<Person> people = new ArrayList<>();  // Use with types
    String query = "SELECT * FROM Person";
    
    try (Connection connection = connect();
         PreparedStatement preparedStatement = connection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery()) {

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String email = resultSet.getString("email");
            String phoneNumber = resultSet.getString("phoneNumber");
            String address = resultSet.getString("address");

            // Add a Person object to the people list
            people.add(new Person(id, firstName, lastName, email, phoneNumber, address));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return people;  // The typed List<Person> is returned here
}


    // UPDATE method
    public void update(int id, String firstName, String lastName, String email, String phoneNumber, String address) {
        String query = "UPDATE Person SET firstName = ?, lastName = ?, email = ?, phoneNumber = ?, address = ? WHERE id = ?";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, address);
            preparedStatement.setInt(6, id);
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE method
    public void delete(int id) {
        String query = "DELETE FROM Person WHERE id = ?";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
