package VEHICLERENTALAPP;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistrationScreen {
    public void show(Stage stage) {
        Label titleLabel = new Label("Registration");
        Label fullNameLabel = new Label("Full Name:");
        TextField fullNameField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label phoneLabel = new Label("Phone Number:");
        TextField phoneField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();
        Label roleLabel = new Label("Role:");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Driver", "Admin", "Customer");
        roleComboBox.setValue("Customer"); // Default role

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");

        // Register Button Action
        registerButton.setOnAction(e -> {
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String role = roleComboBox.getValue();

            // Basic validation
            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                System.out.println("Please fill in all fields.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match.");
                return;
            }

            // Save the user to the database
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.registerUser(fullName, email, phone, password, role);

            if (success) {
                System.out.println("Registration Successful!");
                stage.close(); // Close the registration window
            } else {
                System.out.println("Registration failed. Please try again.");
            }
        });

        // Back Button Action
        backButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.show(new Stage());
            stage.close();
        });

        VBox layout = new VBox(10, titleLabel, fullNameLabel, fullNameField, emailLabel, emailField,
                phoneLabel, phoneField, passwordLabel, passwordField, confirmPasswordLabel, confirmPasswordField,
                roleLabel, roleComboBox, registerButton, backButton);

        Scene scene = new Scene(layout, 350, 450); // Adjusted size for the registration form
        stage.setTitle("Registration");
        stage.setScene(scene);
        stage.show();
    }
}