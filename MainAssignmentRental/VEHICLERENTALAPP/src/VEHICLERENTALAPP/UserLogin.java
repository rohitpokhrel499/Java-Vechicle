package VEHICLERENTALAPP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLogin {

    public boolean userLogin(String email, String password) {
        String query = "SELECT user_id FROM USERS WHERE EMAIL=? AND PASSWORD=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { // ✅ If user exists
                    int userId = rs.getInt("user_id"); // Fetch user ID
                    GlobalVar.id = userId; // ✅ Assign user ID to global variable
                    return true; // ✅ Login successful
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Login failed
    }
}
