package com.smartroute.model;

public class ChargingStation {
    private String id;
    private String name;
    private String locationName;
    private String operator;
    private int powerKw;
    private int availableSlots;
    private int totalSlots;
    private double costPerKwh;
    private String status;

    public ChargingStation() {}

    public ChargingStation(String id, String name, String locationName, String operator,
                           int powerKw, int availableSlots, int totalSlots, double costPerKwh, String status) {
        this.id = id;
        this.name = name;
        this.locationName = locationName;
        this.operator = operator;
        this.powerKw = powerKw;
        this.availableSlots = availableSlots;
        this.totalSlots = totalSlots;
        this.costPerKwh = costPerKwh;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public int getPowerKw() { return powerKw; }
    public void setPowerKw(int powerKw) { this.powerKw = powerKw; }

    public int getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(int availableSlots) { this.availableSlots = availableSlots; }

    public int getTotalSlots() { return totalSlots; }
    public void setTotalSlots(int totalSlots) { this.totalSlots = totalSlots; }

    public double getCostPerKwh() { return costPerKwh; }
    public void setCostPerKwh(double costPerKwh) { this.costPerKwh = costPerKwh; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
