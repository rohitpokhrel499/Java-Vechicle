package VEHICLERENTALAPP;

import javafx.beans.property.*;

public class Vehicle {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty type;
    private final DoubleProperty price;
    private final StringProperty status;

    public Vehicle(int id, String name, String type, double price, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.price = new SimpleDoubleProperty(price);
        this.status = new SimpleStringProperty(status);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }

    public String getType() { return type.get(); }
    public StringProperty typeProperty() { return type; }

    public double getPrice() { return price.get(); }
    public DoubleProperty priceProperty() { return price; }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
}
