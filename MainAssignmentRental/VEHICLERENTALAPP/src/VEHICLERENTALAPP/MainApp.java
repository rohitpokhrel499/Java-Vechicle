package VEHICLERENTALAPP;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Start with the Login Screen
        LoginScreen loginScreen = new LoginScreen();
        loginScreen.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}