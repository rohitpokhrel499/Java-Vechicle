package VEHICLERENTALAPP;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Request {

    private int requestId;  // Remains as an int
    private int userId;     // Remains as an int
    private IntegerProperty vehicleId;  // Changed to IntegerProperty
    private StringProperty status;      // Changed to StringProperty

    // Constructor now uses IntegerProperty for vehicleId and StringProperty for status
    public Request(int requestId, int userId, int vehicleId, String status) {
        this.requestId = requestId;
        this.userId = userId;
        this.vehicleId = new SimpleIntegerProperty(vehicleId);  // Initialize with SimpleIntegerProperty
        this.status = new SimpleStringProperty(status);  // Initialize with SimpleStringProperty
    }

    // Getter and Setter methods
    public int getRequestId() {
        return requestId;
    }

    public int getUserId() {
        return userId;
    }

    public int getVehicleId() {
        return vehicleId.get();  // Returns the value of vehicleId
    }

    public String getStatus() {
        return status.get();  // Returns the value of status
    }

    // Property methods for JavaFX TableView binding
    public IntegerProperty vehicleIdProperty() {
        return vehicleId;  // Returns the IntegerProperty for vehicleId
    }

    public StringProperty statusProperty() {
        return status;  // Returns the StringProperty for status
    }

    // Optionally, you can add setters if needed
    public void setStatus(String status) {
        this.status.set(status);  // Set the new status value using StringProperty
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId.set(vehicleId);  // Set the new vehicleId value using IntegerProperty
    }
}
