package VEHICLERENTALAPP;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class RequestHistory {

    private int vehicleId;  // Vehicle ID
    private String status;

    public RequestHistory(int vehicleId, String status) {
        this.vehicleId = vehicleId;
        this.status = status;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getStatus() {
        return status;
    }

    // Getters for properties
    public IntegerProperty vehicleIdProperty() {
        return new SimpleIntegerProperty(vehicleId);
    }

    public StringProperty statusProperty() {
        return new SimpleStringProperty(status);
    }
}
