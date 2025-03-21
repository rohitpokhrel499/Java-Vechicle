package VEHICLERENTALAPP;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageVehicle {
    
    private TableView<Vehicle> table;

    public Scene getScene(Stage stage) {
        Label titleLabel = new Label("Manage Vehicles");

        table = new TableView<>();
        setupTable();

        Button refreshButton = new Button("Refresh");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button backButton = new Button("Back");

        refreshButton.setOnAction(e -> loadVehicles());
        updateButton.setOnAction(e -> updateSelectedVehicle());
        deleteButton.setOnAction(e -> deleteSelectedVehicle());

        backButton.setOnAction(e -> {
            AdminDashboard adminDashboard = new AdminDashboard();
            adminDashboard.show(stage);
        });

        VBox layout = new VBox(10, titleLabel, table, refreshButton, updateButton, deleteButton, backButton);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 600, 400);
        loadVehicles(); // Load vehicles on startup

        return scene;
    }

    private void setupTable() {
        TableColumn<Vehicle, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Vehicle, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Vehicle, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Vehicle, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Vehicle, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idColumn, nameColumn, typeColumn, priceColumn, statusColumn);
    }

    private void loadVehicles() {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
        String query = "SELECT * FROM VEHICLES";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getString("TYPE"),
                        rs.getDouble("PRICE"),
                        rs.getString("STATUS")
                ));
            }

            table.setItems(vehicles);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading vehicles: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateSelectedVehicle() {
        Vehicle selectedVehicle = table.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a vehicle to update.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedVehicle.getPrice() + "");
        dialog.setTitle("Update Vehicle");
        dialog.setHeaderText("Update Price for " + selectedVehicle.getName());
        dialog.setContentText("Enter new price:");

        dialog.showAndWait().ifPresent(newPrice -> {
            try {
                double price = Double.parseDouble(newPrice);
                updateVehiclePrice(selectedVehicle.getId(), price);
                loadVehicles(); // Refresh table
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid price format.");
            }
        });
    }

    private void updateVehiclePrice(int vehicleId, double newPrice) {
        String query = "UPDATE VEHICLES SET PRICE = ? WHERE ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, newPrice);
            stmt.setInt(2, vehicleId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle price updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update vehicle.");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating vehicle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteSelectedVehicle() {
        Vehicle selectedVehicle = table.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a vehicle to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this vehicle?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                deleteVehicleFromDatabase(selectedVehicle.getId());
                loadVehicles(); // Refresh table
            }
        });
    }

    private void deleteVehicleFromDatabase(int vehicleId) {
        String query = "DELETE FROM VEHICLES WHERE ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, vehicleId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle deleted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete vehicle.");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error deleting vehicle: " + e.getMessage());
            e.printStackTrace();
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
