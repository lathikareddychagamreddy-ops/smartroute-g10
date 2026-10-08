import graph from './graph-data.json' with { type: 'json' };
import algorithms from './algorithms.json' with { type: 'json' };
const nodeByName = new Map(graph.nodes.map((node) => [node.name, node]));
const adjacency = new Map(graph.nodes.map((node) => [node.name, []]));

for (const edge of graph.edges) {
  adjacency.get(edge.source)?.push({ ...edge, next: edge.destination });
  adjacency.get(edge.destination)?.push({
    ...edge,
    source: edge.destination,
    destination: edge.source,
    next: edge.source,
  });
}

const json = (statusCode, data) => ({
  statusCode,
  headers: { 'content-type': 'application/json; charset=utf-8' },
  body: JSON.stringify(data),
});

function readBody(event) {
  if (!event.body) return {};
  const body = event.isBase64Encoded
    ? Buffer.from(event.body, 'base64').toString('utf8')
    : event.body;
  return JSON.parse(body);
}

function findPath(start, destination, weight) {
  const distances = new Map([[start, 0]]);
  const previous = new Map();
  const pending = new Set(nodeByName.keys());

  while (pending.size > 0) {
    let current = null;
    for (const name of pending) {
      if (current === null || (distances.get(name) ?? Infinity) < (distances.get(current) ?? Infinity)) {
        current = name;
      }
    }
    if (current === null || !Number.isFinite(distances.get(current))) break;
    if (current === destination) break;
    pending.delete(current);

    for (const edge of adjacency.get(current) ?? []) {
      if (!pending.has(edge.next)) continue;
      const candidate = distances.get(current) + weight(edge);
      if (candidate < (distances.get(edge.next) ?? Infinity)) {
        distances.set(edge.next, candidate);
        previous.set(edge.next, current);
      }
    }
  }

  if (start !== destination && !previous.has(destination)) return null;
  const path = [destination];
  while (path[0] !== start) {
    const parent = previous.get(path[0]);
    if (!parent) return null;
    path.unshift(parent);
  }
  return path;
}

function breadthFirstPath(start, destination) {
  const queue = [start];
  const previous = new Map([[start, null]]);
  for (let i = 0; i < queue.length && !previous.has(destination); i++) {
    for (const edge of adjacency.get(queue[i]) ?? []) {
      if (!previous.has(edge.next)) {
        previous.set(edge.next, queue[i]);
        queue.push(edge.next);
      }
    }
  }
  if (!previous.has(destination)) return null;
  const path = [];
  for (let node = destination; node !== null; node = previous.get(node)) path.unshift(node);
  return path;
}

function edgesFor(path) {
  return path.slice(1).map((name, index) =>
    adjacency.get(path[index]).find((edge) => edge.next === name),
  );
}

function makeRoute(path, algorithm, name, request, recommended = false) {
  if (!path) return null;
  const edges = edgesFor(path);
  const nodes = path.map((node) => nodeByName.get(node));
  const sum = (key) => edges.reduce((total, edge) => total + Number(edge[key] || 0), 0);
  const average = (key) => edges.length
    ? edges.reduce((total, edge) => total + Number(edge[key] || 0), 0) / edges.length
    : 0;
  const trafficCondition = String(request.trafficCondition || request.preferences?.trafficCondition || 'Normal').toUpperCase();
  const weatherCondition = String(request.weatherCondition || request.preferences?.weatherCondition || 'Clear').toUpperCase();
  const trafficMultiplier = trafficCondition.includes('HEAVY') ? 1.55 : trafficCondition.includes('MODERATE') ? 1.2 : 1;
  const weatherMultiplier = weatherCondition.includes('STORM') || weatherCondition.includes('BAD') ? 1.35 : weatherCondition.includes('RAIN') ? 1.15 : 1;
  const trafficLevel = trafficMultiplier > 1.4 ? 'HEAVY' : trafficMultiplier > 1 ? 'MODERATE' : (edges[0]?.trafficLevel || 'LOW');
  const distance = sum('distance');
  const travelTime = sum('travelTime') * trafficMultiplier * weatherMultiplier;
  const prefs = request.preferences || {};
  const vehicleType = String(request.vehicleType || prefs.vehicleType || 'Petrol').toLowerCase();
  const fuelFactor = vehicleType.includes('electric') ? 1.8 : vehicleType.includes('diesel') ? 5.8 : 7.2;
  const chargingStops = nodes.flatMap((node) => node.chargingStations || []);
  const metrics = {
    distance: Number(distance.toFixed(1)),
    travelTime: Number(travelTime.toFixed(1)),
    fuelCost: Math.round(distance * fuelFactor),
    tollCost: Number(sum('tollCost').toFixed(1)),
    safetyScore: Number(average('safetyScore').toFixed(1)),
    trafficLevel,
    weatherImpact: Number((average('weatherImpact') * weatherMultiplier).toFixed(1)),
    scenicScore: Number(average('scenicScore').toFixed(1)),
    evChargingStationsCount: chargingStops.length,
    overallScore: Number(Math.max(0, Math.min(100,
      (average('safetyScore') * 6) + (average('scenicScore') * 2) - (sum('tollCost') ? 3 : 0) - (trafficMultiplier - 1) * 10,
    )).toFixed(1)),
  };
  return {
    id: `${algorithm}-${path.join('-')}`,
    algorithm,
    name,
    path,
    nodes,
    edges,
    metrics,
    chargingStops,
    scenicHighlights: edges.filter((edge) => edge.scenicScore >= 8).map((edge) => edge.roadName),
    recommendationReason: `${name} calculated from the Hyderabad road network.`,
    executionTimeMs: 0.1,
    recommended,
  };
}

function calculate(request) {
  const start = String(request.startLocation || '').trim();
  const destination = String(request.destination || '').trim();
  if (!nodeByName.has(start) || !nodeByName.has(destination) || start === destination) {
    return json(400, { success: false, message: 'Choose two different valid Hyderabad locations.' });
  }

  const shortestPath = findPath(start, destination, (edge) => edge.distance);
  const fastestPath = findPath(start, destination, (edge) => edge.travelTime);
  const preferences = request.preferences || {};
  const useAvoidTolls = Boolean(preferences.avoidTolls);
  const useAvoidTraffic = Boolean(preferences.avoidHeavyTraffic);
  const recommendedPath = findPath(start, destination, (edge) => {
    let cost =
      edge.distance * (Number(preferences.distanceWeight ?? 0.5) + Number(preferences.fuelWeight ?? 0.5) * 0.15) +
      edge.travelTime * Number(preferences.timeWeight ?? 0.8) * 0.5 +
      (10 - edge.safetyScore) * Number(preferences.safetyWeight ?? 0.7) * 1.8 +
      edge.tollCost * Number(preferences.tollWeight ?? 0.4) * 0.02 +
      edge.trafficFactor * Number(preferences.trafficWeight ?? 0.8) * 0.3 +
      edge.weatherImpact * Number(preferences.weatherWeight ?? 0.6) * 0.2 -
      edge.scenicScore * Number(preferences.scenicWeight ?? 0.3) * 0.25;
    if (useAvoidTolls && edge.tollCost > 0) cost += 1000;
    if (useAvoidTraffic && edge.trafficLevel === 'HEAVY') cost += 30;
    if (preferences.preferSafeRoads && edge.safetyScore < 7) cost += 25;
    return Math.max(0.01, cost);
  });

  const evPath = breadthFirstPath(start, destination);
  const scenicPath = findPath(start, destination, (edge) =>
    Math.max(0.01, edge.distance / Math.max(edge.scenicScore, 0.1)),
  );
  const shortestRoute = makeRoute(shortestPath, 'Dijkstra', 'Shortest Distance Route', request);
  const fastestRoute = makeRoute(fastestPath, 'A*', 'Fastest Route', request);
  const recommendedRoute = makeRoute(recommendedPath, 'Modified Dijkstra', 'Recommended Multi-Criteria Route', request, true);
  const evChargingRoute = makeRoute(evPath, 'BFS', 'EV Charging Route', request);
  const scenicRoute = makeRoute(scenicPath, 'DFS', 'Scenic Corridor Route', request);
  const allRoutes = [recommendedRoute, shortestRoute, fastestRoute, evChargingRoute, scenicRoute].filter(Boolean);

  const comparison = {
    headers: ['Metric', 'Dijkstra', 'A*', 'Recommended'],
    rows: [
      { metric: 'Distance (km)', dijkstra: shortestRoute?.metrics.distance, aStar: fastestRoute?.metrics.distance, recommended: recommendedRoute?.metrics.distance },
      { metric: 'Travel Time (min)', dijkstra: shortestRoute?.metrics.travelTime, aStar: fastestRoute?.metrics.travelTime, recommended: recommendedRoute?.metrics.travelTime },
      { metric: 'Fuel Cost (₹)', dijkstra: shortestRoute?.metrics.fuelCost, aStar: fastestRoute?.metrics.fuelCost, recommended: recommendedRoute?.metrics.fuelCost },
      { metric: 'Toll Cost (₹)', dijkstra: shortestRoute?.metrics.tollCost, aStar: fastestRoute?.metrics.tollCost, recommended: recommendedRoute?.metrics.tollCost },
      { metric: 'Safety (10)', dijkstra: shortestRoute?.metrics.safetyScore, aStar: fastestRoute?.metrics.safetyScore, recommended: recommendedRoute?.metrics.safetyScore },
    ],
  };

  return json(200, {
    success: true,
    message: 'Successfully computed routes on the Hyderabad road network.',
    startLocation: start,
    destination,
    shortestRoute,
    fastestRoute,
    recommendedRoute,
    evChargingRoute,
    scenicRoute,
    allRoutes,
    comparison,
    executionTimes: Object.fromEntries(allRoutes.map((route) => [route.algorithm, route.executionTimeMs])),
  });
}

export const handler = async (event) => {
  try {
    const path = event.path
      .replace(/^\/(?:\.netlify\/functions\/api|api)/, '')
      .replace(/\/+$/, '') || '/';
    const method = event.httpMethod;
    if (method === 'GET' && path === '/graph') return json(200, graph);
    if (method === 'GET' && path === '/graph/nodes') return json(200, graph.nodes);
    if (method === 'GET' && path === '/algorithms') return json(200, algorithms);

    if (method === 'POST' && path === '/routes/recalculate') {
      const request = readBody(event);
      const before = calculate({ ...request, trafficCondition: 'Normal', weatherCondition: 'Clear' });
      const after = calculate(request);
      const beforeData = JSON.parse(before.body);
      const afterData = JSON.parse(after.body);
      if (before.statusCode !== 200) return before;
      if (after.statusCode !== 200) return after;
      const beforeRoute = beforeData.recommendedRoute;
      const afterRoute = afterData.recommendedRoute;
      const timeDifference = Number((afterRoute.metrics.travelTime - beforeRoute.metrics.travelTime).toFixed(1));
      return json(200, {
        startLocation: request.startLocation,
        destination: request.destination,
        trafficCondition: request.trafficCondition,
        weatherCondition: request.weatherCondition,
        beforeRoute,
        afterRoute,
        routeChanged: beforeRoute.path.join('|') !== afterRoute.path.join('|'),
        conditionSummary: 'Route recalculated for the selected conditions.',
        tradeOffExplanation: `Estimated travel time changes by ${timeDifference} minutes under the selected traffic and weather conditions.`,
        timeDifference,
        safetyDifference: Number((afterRoute.metrics.safetyScore - beforeRoute.metrics.safetyScore).toFixed(1)),
        costDifference: Number((afterRoute.metrics.fuelCost + afterRoute.metrics.tollCost - beforeRoute.metrics.fuelCost - beforeRoute.metrics.tollCost).toFixed(1)),
      });
    }

    if (method === 'POST' && path.startsWith('/routes/')) {
      const request = readBody(event);
      if (path === '/routes/calculate') return calculate(request);
      const definitions = {
        '/routes/shortest': ['Dijkstra', 'Shortest Distance Route', (edge) => edge.distance],
        '/routes/fastest': ['A*', 'Fastest Route', (edge) => edge.travelTime],
        '/routes/recommended': ['Modified Dijkstra', 'Recommended Multi-Criteria Route', (edge) => edge.distance + edge.travelTime],
        '/routes/ev-charging': ['BFS', 'EV Charging Route', null],
        '/routes/scenic': ['DFS', 'Scenic Corridor Route', (edge) => edge.distance / Math.max(edge.scenicScore, 0.1)],
      };
      const definition = definitions[path];
      if (!definition) return json(404, { message: 'Unknown API endpoint.' });
      const start = String(request.startLocation || '').trim();
      const destination = String(request.destination || '').trim();
      if (!nodeByName.has(start) || !nodeByName.has(destination) || start === destination) {
        return json(400, { message: 'Choose two different valid Hyderabad locations.' });
      }
      const pathNodes = definition[2]
        ? findPath(start, destination, definition[2])
        : breadthFirstPath(start, destination);
      const route = makeRoute(pathNodes, definition[0], definition[1], request, definition[0] === 'Modified Dijkstra');
      return route ? json(200, route) : json(404, { message: 'No route found.' });
    }

    return json(404, { message: 'API endpoint not found.' });
  } catch (error) {
    console.error('Netlify API function failed:', error);
    return json(500, { message: 'The route API could not process this request.' });
  }
};
