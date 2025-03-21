package VEHICLERENTALAPP;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboard {
    public void show(Stage stage) {
        Button addVehicleButton = new Button("Add Vehicle");
        Button manageVehiclesButton = new Button("Manage Vehicles");
        Button manageRequestsButton = new Button("Manage Requests");
        Button logoutButton = new Button("Logout");
        Button backButton = new Button("Back");

        addVehicleButton.setOnAction(e -> {
            showAddVehiclePage(stage); 
        });

        manageVehiclesButton.setOnAction(e -> {
        	showManageVehiclePage(stage); 
        });

        manageRequestsButton.setOnAction(e -> {
        	showManageRequestPage(stage); 
        });

        logoutButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.show(new Stage());
            stage.close();
        });

        // Back Button Action
        backButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.show(new Stage());
            stage.close();
        });

        VBox layout = new VBox(10, addVehicleButton, manageVehiclesButton, manageRequestsButton, logoutButton, backButton);
        Scene scene = new Scene(layout, 350, 250);
        stage.setTitle("Admin Dashboard");
        stage.setScene(scene);
        stage.show();
    }
    
    private void showAddVehiclePage(Stage stage) {
        AddVehicle addVehicle = new AddVehicle();
        stage.setScene(addVehicle.getScene(stage));
    }
    
    private void showManageVehiclePage(Stage stage) {
        ManageVehicle manageVehicle = new ManageVehicle();
        stage.setScene(manageVehicle.getScene(stage));
    }
    
    private void showManageRequestPage(Stage stage) {
        ManageRequests requestVehicle = new ManageRequests();
        stage.setScene(requestVehicle.getScene(stage));
    }
}
