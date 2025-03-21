package VEHICLERENTALAPP;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BrowseVehicles {

    private TableView<Vehicle> table = new TableView<>();

    public Scene getScene(Stage stage) {  
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

        TableColumn<Vehicle, Void> rentColumn = new TableColumn<>("Rent");
        rentColumn.setCellFactory(param -> new TableCell<>() {
            private final Button rentButton = new Button("Rent");

            {
                rentButton.setOnAction(event -> {
                    Vehicle vehicle = getTableView().getItems().get(getIndex());
                    rentVehicle(vehicle);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(rentButton);
                }
            }
        });

        table.getColumns().addAll(idColumn, nameColumn, typeColumn, priceColumn, statusColumn, rentColumn);
        loadVehicleData();

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            CustomerDashboard customerDashboard = new CustomerDashboard();
            customerDashboard.show(stage); 
        });

        VBox layout = new VBox(10, table, backButton);
        layout.setPadding(new javafx.geometry.Insets(20));
        return new Scene(layout, 600, 400);
    }

    private void loadVehicleData() {
        ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();
        String query = "SELECT * FROM VEHICLES WHERE STATUS = 'Available'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vehicleList.add(new Vehicle(
                        rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getString("TYPE"),
                        rs.getDouble("PRICE"),
                        rs.getString("STATUS")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setItems(vehicleList);
    }

    private void rentVehicle(Vehicle vehicle) {
        String updateVehicleQuery = "UPDATE VEHICLES SET STATUS = 'Pending' WHERE ID = ?";
        String insertRequestQuery = "INSERT INTO RENTAL_REQUESTS (USER_ID, VEHICLE_ID, STATUS) VALUES (?, ?, 'Pending')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateVehicleQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertRequestQuery)) {

            // Update vehicle status to "Pending"
            updateStmt.setInt(1, vehicle.getId());
            int updatedRows = updateStmt.executeUpdate();

            if (updatedRows > 0) {
                // Insert rental request into database (replace "CurrentUser" with actual username logic)
                insertStmt.setInt(1, GlobalVar.id); 
                insertStmt.setInt(2, vehicle.getId());
                insertStmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Rental request submitted successfully!");
                loadVehicleData();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to rent vehicle.");
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
