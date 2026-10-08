package com.smartroute.algorithm;

/**
 * Location - Graph Vertex Representation
 * Represents a geographical node in the road network.
 */
public class Location {
    private Long id;
    private String name;
    private double latitude;
    private double longitude;
    private String state;
    private String description;

    public Location() {}

    public Location(Long id, String name, double latitude, double longitude, String state, String description) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
        this.description = description;
    }

    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return name + " (" + latitude + ", " + longitude + ")";
    }
}
