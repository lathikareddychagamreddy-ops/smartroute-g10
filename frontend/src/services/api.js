import axios from 'axios';

// Uses Vite proxy in dev mode to seamlessly forward to Spring Boot on port 8085
const API_BASE_URL = '/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

export const routeApi = {
  // Main route calculation endpoint
  calculateRoutes: async (payload) => {
    const response = await apiClient.post('/routes/calculate', payload);
    return response.data;
  },

  // Single algorithm calculations
  getShortestRoute: async (payload) => {
    const response = await apiClient.post('/routes/shortest', payload);
    return response.data;
  },

  getFastestRoute: async (payload) => {
    const response = await apiClient.post('/routes/fastest', payload);
    return response.data;
  },

  getRecommendedRoute: async (payload) => {
    const response = await apiClient.post('/routes/recommended', payload);
    return response.data;
  },

  getEVChargingRoute: async (payload) => {
    const response = await apiClient.post('/routes/ev-charging', payload);
    return response.data;
  },

  getScenicRoute: async (payload) => {
    const response = await apiClient.post('/routes/scenic', payload);
    return response.data;
  },

  // Dynamic conditions recalculation (traffic / weather before & after)
  recalculateConditions: async (payload) => {
    const response = await apiClient.post('/routes/recalculate', payload);
    return response.data;
  },

  // Graph topology and node lists
  getGraph: async () => {
    const response = await apiClient.get('/graph');
    return response.data;
  },

  getNodes: async () => {
    const response = await apiClient.get('/graph/nodes');
    return response.data;
  },

  // Algorithm metadata & benchmarks
  getAlgorithms: async () => {
    const response = await apiClient.get('/algorithms');
    return response.data;
  },
};

export default apiClient;
