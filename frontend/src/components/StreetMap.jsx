import React, { useEffect } from 'react';
import {
  CircleMarker,
  MapContainer,
  Polyline,
  Popup,
  TileLayer,
  Tooltip,
  useMap,
} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

const DEFAULT_CENTER = [17.385, 78.4867];

const FitGraphBounds = ({ nodes }) => {
  const map = useMap();

  useEffect(() => {
    if (nodes.length > 0) {
      map.fitBounds(nodes.map((node) => [node.latitude, node.longitude]), {
        padding: [24, 24],
        maxZoom: 13,
      });
    }
  }, [map, nodes]);

  return null;
};

const StreetMap = ({ nodes, edges, routes, activeRoute, startLocation, destination }) => {
  const nodesByName = new Map(nodes.map((node) => [node.name, node]));
  const getRoutePositions = (route) =>
    (route?.path || [])
      .map((name) => nodesByName.get(name))
      .filter(Boolean)
      .map((node) => [node.latitude, node.longitude]);

  const routeLayers = [
    { route: routes.find((route) => route.algorithm === 'Dijkstra'), color: '#38bdf8', weight: 4, dashArray: '8 7' },
    { route: routes.find((route) => route.algorithm === 'A*'), color: '#c084fc', weight: 4 },
    {
      route: activeRoute || routes.find((route) => route.algorithm === 'Modified Dijkstra'),
      color: '#10b981',
      weight: 6,
    },
  ];

  return (
    <div style={{ height: '550px', overflow: 'hidden', borderRadius: 'var(--radius-md)', border: '1px solid rgba(255, 255, 255, 0.12)' }}>
      <MapContainer
        center={DEFAULT_CENTER}
        zoom={11}
        scrollWheelZoom
        style={{ width: '100%', height: '100%', background: '#e2e8f0' }}
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <FitGraphBounds nodes={nodes} />

        {edges.map((edge, index) => {
          const from = nodesByName.get(edge.source);
          const to = nodesByName.get(edge.destination);
          if (!from || !to) return null;

          return (
            <Polyline
              key={`road-${index}`}
              positions={[[from.latitude, from.longitude], [to.latitude, to.longitude]]}
              pathOptions={{
                color: edge.tollCost > 0 ? '#f59e0b' : '#64748b',
                opacity: 0.65,
                weight: edge.roadType === 'Expressway' ? 4 : 2,
              }}
            />
          );
        })}

        {routeLayers.map(({ route, color, weight, dashArray }, index) => {
          const positions = getRoutePositions(route);
          if (positions.length < 2) return null;

          return (
            <Polyline
              key={`route-${route.algorithm}-${index}`}
              positions={positions}
              pathOptions={{ color, weight, opacity: 0.9, dashArray }}
            />
          );
        })}

        {nodes.map((node) => {
          const isStart = node.name === startLocation;
          const isDestination = node.name === destination;
          const color = isStart ? '#10b981' : isDestination ? '#f43f5e' : node.hasEvCharging ? '#f59e0b' : '#2563eb';

          return (
            <CircleMarker
              key={node.id || node.name}
              center={[node.latitude, node.longitude]}
              radius={isStart || isDestination ? 8 : 6}
              pathOptions={{ color: '#fff', fillColor: color, fillOpacity: 1, weight: 2 }}
            >
              <Tooltip>{node.name}</Tooltip>
              <Popup>
                <strong>{node.name}</strong>
                <br />
                {node.type}
                {node.hasEvCharging && <><br />EV charging available</>}
              </Popup>
            </CircleMarker>
          );
        })}
      </MapContainer>
    </div>
  );
};

export default StreetMap;
