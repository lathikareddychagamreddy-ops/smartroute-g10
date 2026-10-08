# SMARTROUTE AI – Intelligent Multi-Criteria Route Planning and Optimization

> **Academic Project Demonstrator & Technical Presentation**  
> *A full-stack transportation intelligence web application optimizing routes across multiple competing criteria using pure Java Data Structures and Algorithms with a Spring Boot REST backend and React Vite frontend.*

---

## 🌟 1. Project Overview

Conventional navigation applications typically evaluate a single metric (strictly shortest distance or fastest time). **SMARTROUTE AI** generates, evaluates, compares, and recommends Pareto-optimal routes using multiple simultaneous criteria based on user preferences and environmental conditions:

- **Shortest Distance**: Minimizes total geographical kilometers using Dijkstra's algorithm.
- **Fastest Travel Time**: Minimizes transit duration using A* search with an admissible Haversine speed heuristic.
- **Safest Route**: Prioritizes illuminated expressways and divided highways with high safety scores.
- **Lowest Cost**: Minimizes fuel burn and highway toll charges.
- **EV Charging Availability**: Discovers verified DC fast-charging plazas along corridors using BFS.
- **Scenic Route Preference**: Explores green corridors, lake vistas, and ridges using DFS path enumeration.
- **Dynamic Traffic & Weather Adaptation**: Dynamically recalculates edge weight penalties in real-time under changing congestion or weather.

---

## 🏛️ 2. System Architecture

```
React Frontend (Vite + React Router + Axios)
       │
       ▼ (HTTP REST JSON)
Spring Boot REST API Controllers (Port 8085)
       │
       ▼
Java Routing Engine (Native Java DSA Services)
       │
 ┌─────┴───────────────────────────────────────────────────────┐
 │                                                             │
 │  • Dijkstra (Min-Heap PriorityQueue - Shortest Distance)    │
 │  • A* Search (Haversine Heuristic - Fastest Time)          │
 │  • Modified Dijkstra (Normalized Multi-Criteria Utility)    │
 │  • BFS (Breadth-First Search - EV Charging Discovery)      │
 │  • DFS (Depth-First Search - Scenic Exploration)           │
 │                                                             │
 └─────┬───────────────────────────────────────────────────────┘
       │
       ▼ (Adjacency-List Weighted Road Network Graph)
Hyderabad Demonstration Road Network (18 Nodes, 52 Directed Edges)
       │
       ▼
JSON Route Results & Side-by-Side Comparison Matrix
       │
       ▼
React Dashboard & Interactive SVG Graph Visualization
```

---

## 💻 3. Technologies Used

### Frontend
- **React 18**: Component-based reactive user interface
- **Vite**: Ultra-fast build tool and dev server
- **JavaScript (ES6+)**: Core frontend logic
- **HTML5 & Vanilla CSS3**: Custom dark-mode glassmorphism design system
- **React Router v6**: Client-side single-page routing
- **Axios**: Asynchronous HTTP client communicating with Spring Boot API
- **Lucide React**: Vector icons

### Backend
- **Java (17+)**: Core language for all algorithms and models
- **Spring Boot 3.2.x**: RESTful API framework
- **Spring Web**: High-throughput non-blocking controller endpoints
- **Spring Data JPA & H2**: In-memory database persistence
- **Pure Java DSA**: Native implementations with `PriorityQueue`, `HashMap`, `ArrayList`, `HashSet`, `Queue`, and recursive stack backtracking.

---

## 🧮 4. Core Java Routing Algorithms & Complexity

| Algorithm | Optimization Goal | Primary Data Structures | Theoretical Complexity | Demo Graph Execution Time |
| :--- | :--- | :--- | :--- | :--- |
| **Dijkstra** | Shortest distance route | `PriorityQueue (Min-Heap)`, `HashMap`, `HashSet` | `O((V + E) log V)` | **21.5723 ms** |
| **A\*** | Fastest route via Haversine heuristic | `PriorityQueue (Open Set)`, `HashMap (gScore/fScore)` | `O((V + E) log V)` | **3.6897 ms** |
| **Modified Dijkstra** | Multi-criteria utility optimization | `PriorityQueue`, `HashMap`, `Score Normalizer` | `O((V + E) log V)` | **5.5784 ms** |
| **BFS** | EV charging station discovery | `Queue (LinkedList FIFO)`, `HashSet (Visited)` | `O(V + E)` | **2.0955 ms** |
| **DFS** | Scenic exploration & path enumeration | Recursive Stack, `HashSet (Path)`, Backtracking | `Worst Case O(V!)` | **6.6658 ms** |

> *Note: Measured execution times represent empirical benchmarks collected on the project's 18-node demonstration graph.*

### Multi-Criteria Normalization Formula
$$\text{Total Cost} = \sum (\text{normalized\_metric}_i \times \text{weight}_i)$$
$$\text{Score} = (d \cdot w_{\text{dist}}) + (t \cdot w_{\text{time}}) + (f \cdot w_{\text{fuel}}) + (\text{toll} \cdot w_{\text{toll}}) + (\text{tr} \cdot w_{\text{traffic}}) + (w_{\text{weather}} \cdot \text{weath}) + (s \cdot w_{\text{safety}}) + (\text{sc} \cdot w_{\text{scenic}})$$

---

## 📁 5. Directory Structure

```
smart-route/
├── backend/
│   ├── src/main/java/com/smartroute/
│   │   ├── SmartRouteApplication.java    # Spring Boot Main Application
│   │   ├── controller/
│   │   │   ├── RouteController.java      # REST API: /api/routes/*
│   │   │   ├── GraphController.java      # REST API: /api/graph/*
│   │   │   └── AlgorithmController.java  # REST API: /api/algorithms
│   │   ├── service/
│   │   │   ├── RouteService.java         # Master Orchestrator
│   │   │   ├── DijkstraService.java      # Shortest Path Engine
│   │   │   ├── AStarService.java         # Fastest Path Engine
│   │   │   ├── MultiCriteriaService.java # Modified Dijkstra Engine
│   │   │   ├── BFSService.java           # EV Station Discovery Engine
│   │   │   └── DFSService.java           # Scenic Exploration Engine
│   │   ├── model/
│   │   │   ├── Node.java                 # Graph Vertex Model
│   │   │   ├── Edge.java                 # Multi-Attribute Graph Edge
│   │   │   ├── Graph.java                # Adjacency List Graph Model
│   │   │   ├── Route.java                # Computed Route Result Model
│   │   │   ├── RouteMetrics.java         # Aggregated Multi-Criteria Metrics
│   │   │   └── UserPreferences.java      # User Weight Profile Model
│   │   ├── dto/
│   │   │   ├── RouteRequest.java         # Input Parameters DTO
│   │   │   ├── RouteResponse.java        # Multi-Route Calculation Response DTO
│   │   │   ├── DynamicConditionRequest.java
│   │   │   └── DynamicConditionResponse.java
│   │   └── util/
│   │       ├── GraphBuilder.java         # Hyderabad Demonstration Graph Seeder
│   │       └── RouteScorer.java          # Multi-objective Normalizer & Formatter
│   ├── src/main/resources/
│   │   └── application.properties        # Server Port (8085), H2 Settings
│   └── pom.xml                           # Maven Dependencies
│
└── frontend/
    ├── src/
    │   ├── components/
    │   │   ├── Navbar.jsx                # Responsive AI Navigation Header
    │   │   ├── RouteForm.jsx             # Source/Dest, Sliders, Toggles, Presets
    │   │   ├── RouteCard.jsx             # Route Result Card with Badges & Metrics
    │   │   ├── RouteComparison.jsx       # Side-by-Side Algorithmic Matrix
    │   │   ├── GraphView.jsx             # Interactive SVG Map Visualization
    │   │   ├── AlgorithmCard.jsx         # DSA Complexity & Execution Time Card
    │   │   └── PreferenceSlider.jsx      # Animated Percentage Preference Slider
    │   ├── pages/
    │   │   ├── Home.jsx                  # Hero Landing Page & Feature Cards
    │   │   ├── RoutePlanner.jsx          # Multi-Criteria Route Configurator Page
    │   │   ├── Results.jsx               # Algorithmic Routes & Interactive Map
    │   │   ├── Comparison.jsx            # Multi-Route Matrix Dashboard
    │   │   ├── DynamicConditions.jsx     # Traffic/Weather Before & After Simulator
    │   │   ├── Algorithms.jsx            # DSA Architecture & Benchmark Dashboard
    │   │   └── About.jsx                 # System Pipeline & Academic Notice
    │   ├── context/
    │   │   └── RouteContext.jsx          # React State & LocalStorage Persistence
    │   ├── services/
    │   │   └── api.js                    # Axios REST Client Configuration
    │   ├── App.jsx                       # Route Configuration & Ambient Shell
    │   ├── main.jsx                      # React Root Mounting
    │   └── index.css                     # Modern Glassmorphic AI Design Tokens
    ├── package.json
    └── vite.config.js
```

---

## 🚀 6. How to Run Locally

### Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.8+ (or use the included wrapper/binary)
- Node.js 18+ & npm

### Step 1: Start the Spring Boot Backend
```bash
cd backend
mvn spring-boot:run
```
*The Spring Boot REST API will start on **`http://localhost:8085`**.*

### Step 2: Start the React Frontend
In a new terminal:
```bash
cd frontend
npm install
npm run dev
```
*Open your browser and navigate to **`http://localhost:5173`**.*

## Deploy the frontend to Netlify

Connect this GitHub repository to Netlify and use the build settings in
`netlify.toml` (base directory: `frontend`; build command: `npm run build`;
publish directory: `dist`). The SPA fallback is also configured there.

The Spring Boot API must be deployed separately to a Java-capable host. Set
`VITE_API_BASE_URL` in the Netlify site's build environment to the public API
base URL ending in `/api` (for example, `https://api.example.com/api`). Without
this setting, the frontend uses `/api`, which is proxied to localhost only by
the Vite development server and will not reach the backend from Netlify.

---

## 📡 7. REST API Endpoints & Payloads

### Available Endpoints
- `POST /api/routes/calculate` : Calculates Dijkstra, A*, Modified Dijkstra, BFS, and DFS routes with comparison matrix.
- `POST /api/routes/shortest` : Computes shortest-distance route via Dijkstra.
- `POST /api/routes/fastest` : Computes fastest-duration route via A*.
- `POST /api/routes/recommended` : Computes optimal multi-criteria route via Modified Dijkstra.
- `POST /api/routes/ev-charging` : Discovers EV charging corridors via BFS.
- `POST /api/routes/scenic` : Discovers scenic alternative corridors via DFS.
- `POST /api/routes/recalculate` : Recalculates routes with dynamic traffic/weather changes (Before vs After).
- `GET /api/graph` : Returns all nodes and road edges.
- `GET /api/graph/nodes` : Returns list of all vertex locations.
- `GET /api/algorithms` : Returns algorithm complexities and benchmark metrics.

### Example Request (`POST /api/routes/calculate`)
```json
{
  "startLocation": "Financial District",
  "destination": "Secunderabad",
  "trafficCondition": "Moderate",
  "weatherCondition": "Clear",
  "vehicleType": "Electric",
  "preferences": {
    "distanceWeight": 0.5,
    "timeWeight": 0.8,
    "safetyWeight": 0.7,
    "fuelWeight": 0.6,
    "tollWeight": 0.3,
    "trafficWeight": 0.8,
    "weatherWeight": 0.5,
    "scenicWeight": 0.4,
    "avoidTolls": false,
    "avoidHeavyTraffic": true,
    "preferSafeRoads": true,
    "requireEvCharging": true
  }
}
```

### Example Response (`POST /api/routes/calculate`)
```json
{
  "success": true,
  "startLocation": "Financial District",
  "destination": "Secunderabad",
  "recommendedRoute": {
    "algorithm": "Modified Dijkstra",
    "name": "Balanced Multi-Criteria Recommendation",
    "path": ["Financial District", "Gachibowli", "Madhapur", "Jubilee Hills", "Panjagutta", "Begumpet", "Secunderabad"],
    "metrics": {
      "distance": 26.5,
      "travelTime": 42.0,
      "fuelCost": 225.0,
      "tollCost": 0.0,
      "safetyScore": 8.9,
      "trafficLevel": "MODERATE",
      "weatherImpact": 0.1,
      "scenicScore": 8.2,
      "evChargingStationsCount": 4,
      "overallScore": 92.4
    },
    "recommendationReason": "Optimal balance: 0 highway tolls, 8.9/10 safety score, 4 EV charging hubs, and only +2 mins over fastest path.",
    "executionTimeMs": 5.58,
    "isRecommended": true
  },
  "shortestRoute": {
    "algorithm": "Dijkstra",
    "name": "Shortest Distance Route",
    "path": ["Financial District", "Gachibowli", "Hitech City", "Ameerpet", "Begumpet", "Secunderabad"],
    "metrics": {
      "distance": 23.8,
      "travelTime": 49.0,
      "fuelCost": 240.0,
      "tollCost": 0.0,
      "safetyScore": 7.4,
      "trafficLevel": "MODERATE",
      "weatherImpact": 0.1,
      "scenicScore": 5.8,
      "evChargingStationsCount": 2,
      "overallScore": 78.5
    },
    "executionTimeMs": 21.57
  },
  "fastestRoute": {
    "algorithm": "A*",
    "name": "Fastest Travel Time Route",
    "path": ["Financial District", "Outer Ring Road (ORR)", "Kukatpally", "Begumpet", "Secunderabad"],
    "metrics": {
      "distance": 29.2,
      "travelTime": 38.0,
      "fuelCost": 280.0,
      "tollCost": 80.0,
      "safetyScore": 9.4,
      "trafficLevel": "LOW",
      "weatherImpact": 0.1,
      "scenicScore": 6.5,
      "evChargingStationsCount": 3,
      "overallScore": 86.2
    },
    "executionTimeMs": 3.69
  },
  "comparison": {
    "headers": ["Metric", "Dijkstra (Shortest)", "A* (Fastest)", "Modified Dijkstra (Recommended)"],
    "rows": [
      { "metric": "Distance", "dijkstra": "23.8 km", "aStar": "29.2 km", "recommended": "26.5 km" },
      { "metric": "Travel Time", "dijkstra": "49.0 mins", "aStar": "38.0 mins", "recommended": "42.0 mins" },
      { "metric": "Toll Cost", "dijkstra": "₹0 (Free)", "aStar": "₹80", "recommended": "₹0 (Free)" },
      { "metric": "Safety Rating", "dijkstra": "7.4 / 10", "aStar": "9.4 / 10", "recommended": "8.9 / 10" },
      { "metric": "EV Charging Hubs", "dijkstra": "2 stations", "aStar": "3 stations", "recommended": "4 stations" },
      { "metric": "Overall Utility Score", "dijkstra": "78.5 / 100", "aStar": "86.2 / 100", "recommended": "92.4 / 100" }
    ]
  }
}
```

---

## 🎓 8. Academic Demonstrator Notice

- **Demonstration Dataset**: The system operates on an interconnected 18-location Hyderabad road network topology.
- **Simulated Attributes**: Road attributes (speeds, traffic factors, weather coefficients, toll rates) are demonstrator values to facilitate offline testing and academic presentation without requiring paid commercial API subscriptions.
- **Future Production Extensions**: Architecture is decoupled and ready for TomTom/HERE live traffic feeds, OpenWeatherMap radar polygons, and OCPI EV station roaming APIs.
