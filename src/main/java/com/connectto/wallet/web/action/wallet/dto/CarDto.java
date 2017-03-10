package com.connectto.wallet.web.action.wallet.dto;

/**
 * Created by Serozh on 1/21/16.
 */
public class CarDto {
    /*"car": {
        "model": "Toyota ANY",
        "location": "location20",
        "name": "Car1",
        "class": "SUV.x",
        "licensePlate": "12345"
    },*/

    private String model;
    private String location;
    private String name;
    private String carClass;
    private String licensePlate;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCarClass() {
        return carClass;
    }

    public void setCarClass(String carClass) {
        this.carClass = carClass;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
