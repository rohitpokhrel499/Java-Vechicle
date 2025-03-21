package VEHICLERENTALAPP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    public boolean registerUser(String fullName, String email, String phone, String password, String role) {
        String query = "INSERT INTO Users (fullName, email, phone, password, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, fullName);
            stmt.setString(2, email); // Add email parameter
            stmt.setString(3, phone);
            stmt.setString(4, password); // In a real application, hash the password before storing
            stmt.setString(5, role);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Return true if the user was successfully registered
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}