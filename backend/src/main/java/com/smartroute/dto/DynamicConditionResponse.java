package com.smartroute.dto;

import com.smartroute.model.Route;

public class DynamicConditionResponse {
    private String startLocation;
    private String destination;
    private String trafficCondition;
    private String weatherCondition;
    private Route beforeRoute;
    private Route afterRoute;
    private boolean routeChanged;
    private String conditionSummary;
    private String tradeOffExplanation;
    private double timeDifference;
    private double safetyDifference;
    private double costDifference;

    public DynamicConditionResponse() {}

    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getTrafficCondition() { return trafficCondition; }
    public void setTrafficCondition(String trafficCondition) { this.trafficCondition = trafficCondition; }

    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }

    public Route getBeforeRoute() { return beforeRoute; }
    public void setBeforeRoute(Route beforeRoute) { this.beforeRoute = beforeRoute; }

    public Route getAfterRoute() { return afterRoute; }
    public void setAfterRoute(Route afterRoute) { this.afterRoute = afterRoute; }

    public boolean isRouteChanged() { return routeChanged; }
    public void setRouteChanged(boolean routeChanged) { this.routeChanged = routeChanged; }

    public String getConditionSummary() { return conditionSummary; }
    public void setConditionSummary(String conditionSummary) { this.conditionSummary = conditionSummary; }

    public String getTradeOffExplanation() { return tradeOffExplanation; }
    public void setTradeOffExplanation(String tradeOffExplanation) { this.tradeOffExplanation = tradeOffExplanation; }

    public double getTimeDifference() { return timeDifference; }
    public void setTimeDifference(double timeDifference) { this.timeDifference = timeDifference; }

    public double getSafetyDifference() { return safetyDifference; }
    public void setSafetyDifference(double safetyDifference) { this.safetyDifference = safetyDifference; }

    public double getCostDifference() { return costDifference; }
    public void setCostDifference(double costDifference) { this.costDifference = costDifference; }
}
