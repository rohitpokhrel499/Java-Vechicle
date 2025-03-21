package VEHICLERENTALAPP;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddVehicle {

    private Scene scene;

    public Scene getScene(Stage stage) {
        Label titleLabel = new Label("Add New Vehicle");

        TextField idField = new TextField();
        idField.setPromptText("Vehicle ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Vehicle Name");

        TextField typeField = new TextField();
        typeField.setPromptText("Vehicle Type");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Available", "Pending", "Rented");
        statusComboBox.setValue("Available");

        Button addButton = new Button("Add Vehicle");
        Button backButton = new Button("Back");

        addButton.setOnAction(e -> {
            String idText = idField.getText();
            String name = nameField.getText();
            String type = typeField.getText();
            String priceText = priceField.getText();
            String status = statusComboBox.getValue();

            if (idText.isEmpty() || name.isEmpty() || type.isEmpty() || priceText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
                return;
            }

            try {
                int id = Integer.parseInt(idText);
                double price = Double.parseDouble(priceText);

                if (addVehicleToDatabase(id, name, type, price, status)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle added successfully!");
                    // Clear fields after successful addition
                    idField.clear();
                    nameField.clear();
                    typeField.clear();
                    priceField.clear();
                    statusComboBox.setValue("Available");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to add vehicle. Check logs for details.");
                }

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid ID or Price format.");
            }
        });

        backButton.setOnAction(e -> {
            AdminDashboard adminDashboard = new AdminDashboard();
            adminDashboard.show(stage);
        });

        VBox layout = new VBox(10, titleLabel, idField, nameField, typeField, priceField, statusComboBox, addButton, backButton);
        layout.setPadding(new Insets(20));

        scene = new Scene(layout, 300, 400);
        return scene;
    }

    private boolean addVehicleToDatabase(int id, String name, String type, double price, String status) {
        String query = "INSERT INTO VEHICLES (ID, NAME, TYPE, PRICE, STATUS) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, type);
            stmt.setDouble(4, price);
            stmt.setString(5, status);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Return true if insertion was successful

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
