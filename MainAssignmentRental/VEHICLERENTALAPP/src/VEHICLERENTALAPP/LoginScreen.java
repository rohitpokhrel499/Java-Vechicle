package VEHICLERENTALAPP;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {
    public void show(Stage stage) {
        Label titleLabel = new Label("Vehicle Rental System");
        Label userLabel = new Label("Email:");
        TextField userField = new TextField();
        Label passLabel = new Label("Password:");
        TextField passField = new TextField();
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        // Login Button Action
        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            
            UserLogin userCheck = new UserLogin();
            
            boolean checkLogin = userCheck.userLogin(username, password);

            // Simple login logic (for demonstration)
            if (username.equals("admin") && password.equals("admin")) {
                AdminDashboard adminDashboard = new AdminDashboard();
                adminDashboard.show(new Stage());
                stage.close();
            } else if (checkLogin) {
                CustomerDashboard customerDashboard = new CustomerDashboard();
                customerDashboard.show(new Stage());
                stage.close();
            } else {
                System.out.println("Invalid credentials");
            }
        });

        // Register Button Action
        registerButton.setOnAction(e -> {
            // Open the Registration Screen
            RegistrationScreen registrationScreen = new RegistrationScreen();
            registrationScreen.show(new Stage());
        });

        VBox layout = new VBox(10, titleLabel, userLabel, userField, passLabel, passField, loginButton, registerButton);
        Scene scene = new Scene(layout, 300, 300); // Increased height to accommodate the register button
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}