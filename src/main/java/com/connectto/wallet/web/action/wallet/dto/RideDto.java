package com.connectto.wallet.web.action.wallet.dto;

import java.util.Date;

/**
 * Created by Serozh on 1/21/16.
 */
public class RideDto {
    /*"ride": {
        "startAddress": "1-2 Charents St, Yerevan, Armenia",
        "amount": 0,
        "car": {
            "model": "Toyota ANY",
            "location": "location20",
            "name": "Car1",
            "class": "SUV.x",
            "licensePlate": "12345"
        },
        "startDate": "2015-11-30 06:15:40.0",
        "distance": 10.637901251444692,
        "distanceType": "mile",
        "endDate": "2015-11-30 06:21:16.0",
        "driver": {
            "phone": "123456789",
            "name": "Driver for Vahe"
        },
        "detailedUrl": "https://www.vshoo.com/TSM/index.jsp?rideId=1741",
        "endAddress": "",
        "currencyType": "AMD"
    }*/

    private CarDto carDto;
    private String driverName;

    private String startAddress;
    private String endAddress;

    private String distance;
    private String distanceType;

    private String detailedUrl;

    private Date startDate;
    private Date endDate;

    public CarDto getCarDto() {
        return carDto;
    }

    public void setCarDto(CarDto carDto) {
        this.carDto = carDto;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistanceType() {
        return distanceType;
    }

    public void setDistanceType(String distanceType) {
        this.distanceType = distanceType;
    }

    public String getDetailedUrl() {
        return detailedUrl;
    }

    public void setDetailedUrl(String detailedUrl) {
        this.detailedUrl = detailedUrl;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}