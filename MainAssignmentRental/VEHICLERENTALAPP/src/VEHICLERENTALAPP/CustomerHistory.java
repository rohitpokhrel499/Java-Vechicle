package VEHICLERENTALAPP;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerHistory {

    private TableView<RequestHistory> table;  // Updated to RequestHistory

    public Scene getScene(Stage stage) {
        Label titleLabel = new Label("Your Rental History");

        table = new TableView<>();
        setupTable();

        Button refreshButton = new Button("Refresh");
        Button backButton = new Button("Back");

        refreshButton.setOnAction(e -> loadRentalHistory());
        backButton.setOnAction(e -> {
            CustomerDashboard customerDashboard = new CustomerDashboard();
            customerDashboard.show(stage); // Go back to customer dashboard
        });

        VBox layout = new VBox(10, titleLabel, table, refreshButton, backButton);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 600, 400);
        loadRentalHistory(); // Load history when the scene is created

        return scene;
    }

    private void setupTable() {
        // Setting up the table columns for RequestHistory
        TableColumn<RequestHistory, Integer> vehicleIdColumn = new TableColumn<>("Vehicle ID");
        vehicleIdColumn.setCellValueFactory(cellData -> cellData.getValue().vehicleIdProperty().asObject());

        TableColumn<RequestHistory, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        table.getColumns().addAll(vehicleIdColumn, statusColumn);
    }

    private void loadRentalHistory() {
        ObservableList<RequestHistory> rentalHistoryList = FXCollections.observableArrayList();
        // Adjusted query without REQUEST_ID
        String query = "SELECT rr.STATUS, rr.VEHICLE_ID " +
                       "FROM RENTAL_REQUESTS rr " +
                       "WHERE rr.USER_ID = ?";  // Assuming we have USER_ID from GlobalVar

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Replace `GlobalVar.USER_ID` with the actual logged-in user's ID
            stmt.setInt(1, GlobalVar.id); // Fetch the history of the logged-in user

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rentalHistoryList.add(new RequestHistory(
                            rs.getInt("VEHICLE_ID"),
                            rs.getString("STATUS")
                    ));
                }

                table.setItems(rentalHistoryList); // Populate the table with the data

            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load rental history.");
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
