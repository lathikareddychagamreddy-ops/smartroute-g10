import React, { createContext, useContext, useState, useEffect } from 'react';
import { routeApi } from '../services/api';

const RouteContext = createContext();

const DEFAULT_PREFERENCES = {
  distanceWeight: 0.5,
  timeWeight: 0.8,
  safetyWeight: 0.7,
  fuelWeight: 0.5,
  tollWeight: 0.4,
  trafficWeight: 0.8,
  weatherWeight: 0.6,
  scenicWeight: 0.3,
  avoidTolls: false,
  avoidHeavyTraffic: true,
  preferSafeRoads: false,
  preferScenicRoads: false,
  requireEvCharging: false,
  weatherSensitive: false,
  trafficCondition: 'Normal',
  weatherCondition: 'Clear',
  vehicleType: 'Petrol',
};

export const RouteProvider = ({ children }) => {
  const [startLocation, setStartLocation] = useState('Financial District');
  const [destination, setDestination] = useState('Secunderabad');
  
  // Load saved preferences from localStorage if present
  const [preferences, setPreferences] = useState(() => {
    try {
      const saved = localStorage.getItem('smartroute_preferences');
      return saved ? { ...DEFAULT_PREFERENCES, ...JSON.parse(saved) } : DEFAULT_PREFERENCES;
    } catch (e) {
      return DEFAULT_PREFERENCES;
    }
  });

  const [trafficCondition, setTrafficCondition] = useState('Normal');
  const [weatherCondition, setWeatherCondition] = useState('Clear');
  const [vehicleType, setVehicleType] = useState('Petrol');

  const [routeResults, setRouteResults] = useState(null);
  const [selectedRoute, setSelectedRoute] = useState(null);
  const [graphData, setGraphData] = useState(null);
  const [availableNodes, setAvailableNodes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Persist preferences
  useEffect(() => {
    try {
      localStorage.setItem('smartroute_preferences', JSON.stringify(preferences));
    } catch (e) {
      console.warn('Could not save preferences to localStorage', e);
    }
  }, [preferences]);

  // Load graph and nodes on mount
  useEffect(() => {
    const fetchGraph = async () => {
      try {
        const data = await routeApi.getGraph();
        setGraphData(data);
        if (data && data.nodes) {
          setAvailableNodes(data.nodes);
        }
      } catch (err) {
        console.warn('Backend graph API not yet reachable. Using demo fallback data until connected.', err);
      }
    };
    fetchGraph();
  }, []);

  const updatePreference = (key, value) => {
    setPreferences((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  const calculateRoutes = async (customPayload = null) => {
    setLoading(true);
    setError(null);

    const payload = customPayload || {
      startLocation,
      destination,
      preferences,
      trafficCondition,
      weatherCondition,
      vehicleType,
    };

    try {
      const response = await routeApi.calculateRoutes(payload);
      if (response && response.success) {
        setRouteResults(response);
        // Default selected route to Recommended
        if (response.recommendedRoute) {
          setSelectedRoute(response.recommendedRoute);
        } else if (response.shortestRoute) {
          setSelectedRoute(response.shortestRoute);
        }
      } else {
        setError(response?.message || 'Failed to calculate routes.');
      }
      return response;
    } catch (err) {
      const errorMsg = err.response?.data?.message || err.message || 'Error communicating with Java Spring Boot backend on http://localhost:8085';
      setError(errorMsg);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return (
    <RouteContext.Provider
      value={{
        startLocation,
        setStartLocation,
        destination,
        setDestination,
        preferences,
        setPreferences,
        updatePreference,
        trafficCondition,
        setTrafficCondition,
        weatherCondition,
        setWeatherCondition,
        vehicleType,
        setVehicleType,
        routeResults,
        setRouteResults,
        selectedRoute,
        setSelectedRoute,
        graphData,
        availableNodes,
        loading,
        error,
        setError,
        calculateRoutes,
      }}
    >
      {children}
    </RouteContext.Provider>
  );
};

export const useRoute = () => useContext(RouteContext);
