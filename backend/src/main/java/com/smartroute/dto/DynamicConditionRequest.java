package com.smartroute.dto;

import com.smartroute.model.UserPreferences;

public class DynamicConditionRequest {
    private String startLocation;
    private String destination;
    private String trafficCondition = "Normal";  // "Normal", "Heavy"
    private String weatherCondition = "Normal";  // "Normal", "Bad Weather" / "Storm"
    private UserPreferences preferences = new UserPreferences();
    private String vehicleType = "Petrol";

    public DynamicConditionRequest() {}

    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getTrafficCondition() { return trafficCondition; }
    public void setTrafficCondition(String trafficCondition) { this.trafficCondition = trafficCondition; }

    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }

    public UserPreferences getPreferences() { return preferences; }
    public void setPreferences(UserPreferences preferences) { this.preferences = preferences; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
}
