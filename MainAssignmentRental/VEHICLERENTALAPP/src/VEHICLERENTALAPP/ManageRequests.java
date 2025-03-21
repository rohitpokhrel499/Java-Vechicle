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

public class ManageRequests {

    private TableView<Request> table;

    public Scene getScene(Stage stage) {
        Label titleLabel = new Label("Manage Rental Requests");

        table = new TableView<>();
        setupTable();

        Button refreshButton = new Button("Refresh");
        Button approveButton = new Button("Approve");
        Button rejectButton = new Button("Reject");
        Button backButton = new Button("Back");

        refreshButton.setOnAction(e -> loadRequests());
        approveButton.setOnAction(e -> processRequest("Approved"));
        rejectButton.setOnAction(e -> processRequest("Rejected"));

        backButton.setOnAction(e -> {
            AdminDashboard adminDashboard = new AdminDashboard();
            adminDashboard.show(stage);
        });

        VBox layout = new VBox(10, titleLabel, table, refreshButton, approveButton, rejectButton, backButton);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 700, 450);
        loadRequests(); // Load requests when scene is created

        return scene;
    }

    private void setupTable() {
        TableColumn<Request, Integer> idColumn = new TableColumn<>("Request ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));

        TableColumn<Request, Integer> userColumn = new TableColumn<>("User ID");
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<Request, Integer> vehicleColumn = new TableColumn<>("Vehicle ID");
        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));

        TableColumn<Request, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idColumn, userColumn, vehicleColumn, statusColumn);
    }

    private void loadRequests() {
        ObservableList<Request> requests = FXCollections.observableArrayList();
        String query = "SELECT * FROM RENTAL_REQUESTS WHERE STATUS = 'Pending'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                requests.add(new Request(
                        rs.getInt("REQUEST_ID"),
                        rs.getInt("USER_ID"),
                        rs.getInt("VEHICLE_ID"),
                        rs.getString("STATUS")
                ));
            }

            table.setItems(requests);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading requests: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processRequest(String newStatus) {
        Request selectedRequest = table.getSelectionModel().getSelectedItem();
        if (selectedRequest == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a request to process.");
            return;
        }

        String updateRequestQuery = "UPDATE RENTAL_REQUESTS SET STATUS = ? WHERE REQUEST_ID = ?";
        String updateVehicleQuery = "UPDATE VEHICLES SET STATUS = ? WHERE ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement requestStmt = conn.prepareStatement(updateRequestQuery);
             PreparedStatement vehicleStmt = conn.prepareStatement(updateVehicleQuery)) {

            conn.setAutoCommit(false); // Start transaction

            // Update rental request status
            requestStmt.setString(1, newStatus);
            requestStmt.setInt(2, selectedRequest.getRequestId());
            int rowsUpdated = requestStmt.executeUpdate();

            if (rowsUpdated > 0) {
                // If approved, mark vehicle as "Rented", otherwise set it back to "Available"
                String vehicleStatus = newStatus.equals("Approved") ? "Rented" : "Available";
                vehicleStmt.setString(1, vehicleStatus);
                vehicleStmt.setInt(2, selectedRequest.getVehicleId());
                vehicleStmt.executeUpdate();

                conn.commit(); // Commit transaction

                showAlert(Alert.AlertType.INFORMATION, "Success", "Request " + newStatus.toLowerCase() + " successfully.");
                loadRequests(); // Refresh table data
            } else {
                conn.rollback(); // Rollback in case of failure
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update request.");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error processing request: " + e.getMessage());
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
