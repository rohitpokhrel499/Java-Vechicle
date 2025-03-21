package VEHICLERENTALAPP;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerDashboard {
    private Stage stage; // Store stage reference

    public void show(Stage stage) {
        this.stage = stage; // Assign the stage to class variable
        
        Button browseVehiclesButton = new Button("Browse Vehicles");
        Button historyButton = new Button("History");
        Button logoutButton = new Button("Logout");
        Button backButton = new Button("Back");

        browseVehiclesButton.setOnAction(e -> openBrowseVehicles()); // Open Browse Vehicles in the same window

        historyButton.setOnAction(e -> openhistoryVehicles());

        logoutButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.show(stage); // Reuse the same stage
        });

        backButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.show(stage); // Reuse the same stage
        });

        VBox layout = new VBox(10, browseVehiclesButton, historyButton, logoutButton, backButton);
        Scene scene = new Scene(layout, 300, 200);
        stage.setTitle("Customer Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private void openBrowseVehicles() {
        BrowseVehicles browseVehicles = new BrowseVehicles();
        Scene browseScene = browseVehicles.getScene(stage); // Get the browse vehicle scene
        stage.setScene(browseScene); // Change scene to browse vehicles
    }
    
    private void openhistoryVehicles() {
        CustomerHistory history = new CustomerHistory();
        Scene browseScene = history.getScene(stage); // Get the browse vehicle scene
        stage.setScene(browseScene); // Change scene to browse vehicles
    }
}
