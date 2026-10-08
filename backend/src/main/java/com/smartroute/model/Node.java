package com.smartroute.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private String type;
    private String description;
    private boolean hasEvCharging;
    private List<ChargingStation> chargingStations = new ArrayList<>();

    public Node() {}

    public Node(String id, String name, double latitude, double longitude, String type, String description, boolean hasEvCharging) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.description = description;
        this.hasEvCharging = hasEvCharging;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isHasEvCharging() { return hasEvCharging; }
    public void setHasEvCharging(boolean hasEvCharging) { this.hasEvCharging = hasEvCharging; }

    public List<ChargingStation> getChargingStations() { return chargingStations; }
    public void setChargingStations(List<ChargingStation> chargingStations) { this.chargingStations = chargingStations; }

    public void addChargingStation(ChargingStation cs) {
        this.chargingStations.add(cs);
        this.hasEvCharging = true;
    }
}
