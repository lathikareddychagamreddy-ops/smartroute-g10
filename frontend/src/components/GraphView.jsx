import React, { useState, useMemo } from 'react';
import { useRoute } from '../context/RouteContext';
import { Zap, MapPin, Navigation, Info, Layers, Eye, CheckCircle2 } from 'lucide-react';
import StreetMap from './StreetMap';

const GraphView = ({ routes = [], activeRoute = null }) => {
  const { graphData, startLocation, destination, setStartLocation, setDestination } = useRoute();
  const [selectedNode, setSelectedNode] = useState(null);
  const [showAllPaths, setShowAllPaths] = useState(true);
  const [mapView, setMapView] = useState('street');

  // Nodes & Edges fallback or from graphData
  const nodes = graphData?.nodes || [];
  const edges = graphData?.edges || [];

  // Compute bounding box for projection
  const bounds = useMemo(() => {
    if (!nodes || nodes.length === 0) {
      return { minLat: 17.2, maxLat: 17.55, minLng: 78.3, maxLng: 78.6 };
    }
    let minLat = Infinity, maxLat = -Infinity, minLng = Infinity, maxLng = -Infinity;
    nodes.forEach(n => {
      if (n.latitude < minLat) minLat = n.latitude;
      if (n.latitude > maxLat) maxLat = n.latitude;
      if (n.longitude < minLng) minLng = n.longitude;
      if (n.longitude > maxLng) maxLng = n.longitude;
    });
    // Add margin
    const latMargin = (maxLat - minLat) * 0.1 || 0.02;
    const lngMargin = (maxLng - minLng) * 0.1 || 0.02;
    return {
      minLat: minLat - latMargin,
      maxLat: maxLat + latMargin,
      minLng: minLng - lngMargin,
      maxLng: maxLng + lngMargin,
    };
  }, [nodes]);

  // Project geo-coords to SVG viewBox (width: 900, height: 600)
  const SVG_WIDTH = 900;
  const SVG_HEIGHT = 600;

  const project = (lat, lng) => {
    const x = ((lng - bounds.minLng) / (bounds.maxLng - bounds.minLng)) * (SVG_WIDTH - 120) + 60;
    // Invert Y because latitude goes up north while SVG Y goes down
    const y = ((bounds.maxLat - lat) / (bounds.maxLat - bounds.minLat)) * (SVG_HEIGHT - 120) + 60;
    return { x, y };
  };

  const nodePositions = useMemo(() => {
    const map = {};
    nodes.forEach(n => {
      map[n.name] = project(n.latitude, n.longitude);
    });
    return map;
  }, [nodes, bounds]);

  // Identify shortest, fastest, recommended paths
  const shortestPath = routes.find(r => r.algorithm === 'Dijkstra')?.path || [];
  const fastestPath = routes.find(r => r.algorithm === 'A*')?.path || [];
  const recommendedPath = routes.find(r => r.algorithm === 'Modified Dijkstra')?.path || [];
  const currentActivePath = activeRoute?.path || recommendedPath;

  const isEdgeInPath = (u, v, path) => {
    if (!path || path.length < 2) return false;
    for (let i = 0; i < path.length - 1; i++) {
      if ((path[i] === u && path[i+1] === v) || (path[i] === v && path[i+1] === u)) {
        return true;
      }
    }
    return false;
  };

  return (
    <div className="card" style={{ padding: '1.25rem', position: 'relative' }}>
      {/* Header bar */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem', flexWrap: 'wrap', gap: '0.75rem' }}>
        <div>
          <h3 style={{ fontSize: '1.15rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Layers size={18} style={{ color: '#38bdf8' }} />
            Interactive Hyderabad Graph & Route Topology
          </h3>
          <p style={{ fontSize: '0.8rem', color: 'var(--text-dim)' }}>
            Demonstration topological graph model (18 Interconnected Nodes, Multi-Criteria Weighted Edges)
          </p>
        </div>

        <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center', flexWrap: 'wrap' }}>
          <button
            type="button"
            className={`btn ${mapView === 'street' ? 'btn-primary' : 'btn-secondary'}`}
            style={{ padding: '0.35rem 0.75rem', fontSize: '0.8rem' }}
            onClick={() => setMapView('street')}
          >
            Street Map
          </button>
          <button
            type="button"
            className={`btn ${mapView === 'network' ? 'btn-primary' : 'btn-secondary'}`}
            style={{ padding: '0.35rem 0.75rem', fontSize: '0.8rem' }}
            onClick={() => setMapView('network')}
          >
            Network Diagram
          </button>
          {mapView === 'network' && (
            <button
              type="button"
              className="btn btn-secondary"
              style={{ padding: '0.35rem 0.75rem', fontSize: '0.8rem' }}
              onClick={() => setShowAllPaths(!showAllPaths)}
            >
              <Eye size={14} />
              <span>{showAllPaths ? 'Hide Background Grid' : 'Show All Roads'}</span>
            </button>
          )}
        </div>
      </div>

      {mapView === 'street' ? (
        <StreetMap
          nodes={nodes}
          edges={edges}
          routes={routes}
          activeRoute={activeRoute}
          startLocation={startLocation}
          destination={destination}
        />
      ) : (
        <>
          {/* SVG Canvas Container */}
          <div style={{
        background: '#070b14',
        borderRadius: 'var(--radius-md)',
        border: '1px solid rgba(255, 255, 255, 0.08)',
        position: 'relative',
        overflow: 'hidden',
      }}>
        <svg
          viewBox={`0 0 ${SVG_WIDTH} ${SVG_HEIGHT}`}
          style={{ width: '100%', height: 'auto', display: 'block', maxHeight: '550px' }}
        >
          <defs>
            <linearGradient id="recPathGrad" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" stopColor="#10b981" />
              <stop offset="100%" stopColor="#38bdf8" />
            </linearGradient>
            <filter id="glow" x="-20%" y="-20%" width="140%" height="140%">
              <feGaussianBlur stdDeviation="4" result="blur" />
              <feComposite in="SourceGraphic" in2="blur" operator="over" />
            </filter>
          </defs>

          {/* 1. Base Road Edges */}
          {showAllPaths && edges.map((edge, idx) => {
            const p1 = nodePositions[edge.source];
            const p2 = nodePositions[edge.destination];
            if (!p1 || !p2) return null;

            return (
              <line
                key={`edge-${idx}`}
                x1={p1.x}
                y1={p1.y}
                x2={p2.x}
                y2={p2.y}
                stroke={edge.tollCost > 0 ? 'rgba(245, 158, 11, 0.25)' : 'rgba(255, 255, 255, 0.12)'}
                strokeWidth={edge.roadType === 'Expressway' ? '3' : '1.8'}
                strokeDasharray={edge.roadType === 'Scenic Corridor' ? '4 2' : 'none'}
              />
            );
          })}

          {/* 2. Shortest Route Path (Dijkstra) - Blue Dashed */}
          {shortestPath.length > 1 && edges.map((edge, idx) => {
            if (!isEdgeInPath(edge.source, edge.destination, shortestPath)) return null;
            const p1 = nodePositions[edge.source];
            const p2 = nodePositions[edge.destination];
            if (!p1 || !p2) return null;

            return (
              <line
                key={`sp-${idx}`}
                x1={p1.x}
                y1={p1.y}
                x2={p2.x}
                y2={p2.y}
                stroke="#38bdf8"
                strokeWidth="4"
                strokeDasharray="6 4"
                opacity="0.8"
              />
            );
          })}

          {/* 3. Fastest Route Path (A*) - Purple */}
          {fastestPath.length > 1 && edges.map((edge, idx) => {
            if (!isEdgeInPath(edge.source, edge.destination, fastestPath)) return null;
            const p1 = nodePositions[edge.source];
            const p2 = nodePositions[edge.destination];
            if (!p1 || !p2) return null;

            return (
              <line
                key={`fp-${idx}`}
                x1={p1.x}
                y1={p1.y}
                x2={p2.x}
                y2={p2.y}
                stroke="#c084fc"
                strokeWidth="4"
                opacity="0.75"
              />
            );
          })}

          {/* 4. Active / Recommended Path (Modified Dijkstra) - Bold Glowing Emerald */}
          {currentActivePath.length > 1 && edges.map((edge, idx) => {
            if (!isEdgeInPath(edge.source, edge.destination, currentActivePath)) return null;
            const p1 = nodePositions[edge.source];
            const p2 = nodePositions[edge.destination];
            if (!p1 || !p2) return null;

            return (
              <line
                key={`rec-${idx}`}
                x1={p1.x}
                y1={p1.y}
                x2={p2.x}
                y2={p2.y}
                stroke="url(#recPathGrad)"
                strokeWidth="6"
                strokeLinecap="round"
                filter="url(#glow)"
              />
            );
          })}

          {/* 5. Graph Vertices (Nodes) */}
          {nodes.map((node) => {
            const pos = nodePositions[node.name];
            if (!pos) return null;

            const isStart = node.name === startLocation;
            const isDest = node.name === destination;
            const isInActivePath = currentActivePath.includes(node.name);

            let fillColor = '#1e293b';
            let strokeColor = 'rgba(255, 255, 255, 0.3)';
            let radius = 7;

            if (isStart) {
              fillColor = '#10b981';
              strokeColor = '#34d399';
              radius = 11;
            } else if (isDest) {
              fillColor = '#f43f5e';
              strokeColor = '#fda4af';
              radius = 11;
            } else if (isInActivePath) {
              fillColor = '#38bdf8';
              strokeColor = '#fff';
              radius = 9;
            } else if (node.hasEvCharging) {
              fillColor = '#0f172a';
              strokeColor = '#fbbf24';
              radius = 8;
            }

            return (
              <g
                key={node.id || node.name}
                style={{ cursor: 'pointer' }}
                onClick={() => setSelectedNode(node)}
              >
                {/* Outer halo */}
                {(isStart || isDest || isInActivePath) && (
                  <circle
                    cx={pos.x}
                    cy={pos.y}
                    r={radius + 5}
                    fill="none"
                    stroke={isStart ? '#10b981' : (isDest ? '#f43f5e' : '#38bdf8')}
                    strokeWidth="2"
                    opacity="0.5"
                  />
                )}

                {/* Main vertex circle */}
                <circle
                  cx={pos.x}
                  cy={pos.y}
                  r={radius}
                  fill={fillColor}
                  stroke={strokeColor}
                  strokeWidth="2.5"
                />

                {/* Node Label */}
                <text
                  x={pos.x}
                  y={pos.y - (radius + 6)}
                  textAnchor="middle"
                  fill={isStart ? '#34d399' : (isDest ? '#fda4af' : (isInActivePath ? '#fff' : '#94a3b8'))}
                  fontSize={isStart || isDest ? '11px' : '9.5px'}
                  fontWeight={isStart || isDest || isInActivePath ? '700' : '500'}
                  fontFamily="Outfit, sans-serif"
                  style={{ textShadow: '0 2px 4px rgba(0,0,0,0.9)' }}
                >
                  {node.name}
                </text>
              </g>
            );
          })}
        </svg>

        {/* Selected Node Details Popup */}
        {selectedNode && (
          <div style={{
            position: 'absolute',
            bottom: '15px',
            left: '15px',
            background: 'rgba(15, 23, 42, 0.95)',
            border: '1px solid var(--border-accent)',
            borderRadius: 'var(--radius-md)',
            padding: '1rem',
            maxWidth: '320px',
            backdropFilter: 'blur(12px)',
            boxShadow: '0 10px 25px rgba(0,0,0,0.6)',
            zIndex: 10,
          }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '0.4rem' }}>
              <h4 style={{ fontSize: '1rem', color: '#fff' }}>{selectedNode.name}</h4>
              <button
                onClick={() => setSelectedNode(null)}
                style={{ background: 'none', border: 'none', color: 'var(--text-dim)', cursor: 'pointer', fontSize: '1rem' }}
              >
                ✕
              </button>
            </div>
            <div style={{ fontSize: '0.78rem', color: '#38bdf8', marginBottom: '0.4rem', fontWeight: 600 }}>
              {selectedNode.type}
            </div>
            <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginBottom: '0.75rem' }}>
              {selectedNode.description}
            </p>

            {selectedNode.hasEvCharging && (
              <div style={{ fontSize: '0.78rem', color: '#fbbf24', display: 'flex', alignItems: 'center', gap: '0.35rem', marginBottom: '0.75rem' }}>
                <Zap size={13} />
                <span>Fast EV Charging Hubs Available</span>
              </div>
            )}

            <div style={{ display: 'flex', gap: '0.5rem' }}>
              <button
                type="button"
                className="btn btn-secondary"
                style={{ padding: '0.35rem 0.65rem', fontSize: '0.75rem' }}
                onClick={() => { setStartLocation(selectedNode.name); setSelectedNode(null); }}
              >
                Set as Start
              </button>
              <button
                type="button"
                className="btn btn-primary"
                style={{ padding: '0.35rem 0.65rem', fontSize: '0.75rem' }}
                onClick={() => { setDestination(selectedNode.name); setSelectedNode(null); }}
              >
                Set as Dest
              </button>
            </div>
          </div>
        )}
          </div>

          {/* Legend */}
          <div style={{
        display: 'flex',
        gap: '1.25rem',
        flexWrap: 'wrap',
        marginTop: '0.85rem',
        fontSize: '0.8rem',
        color: 'var(--text-dim)',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
          <span style={{ width: '12px', height: '12px', borderRadius: '50%', background: '#10b981', display: 'inline-block' }}></span>
          <span>Start Point</span>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
          <span style={{ width: '12px', height: '12px', borderRadius: '50%', background: '#f43f5e', display: 'inline-block' }}></span>
          <span>Destination</span>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
          <span style={{ width: '20px', height: '4px', background: '#38bdf8', display: 'inline-block' }}></span>
          <span>Recommended (Modified Dijkstra)</span>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
          <span style={{ width: '20px', height: '3px', borderTop: '2px dashed #38bdf8', display: 'inline-block' }}></span>
          <span>Shortest (Dijkstra)</span>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
          <span style={{ width: '20px', height: '3px', background: '#c084fc', display: 'inline-block' }}></span>
          <span>Fastest (A*)</span>
        </div>
          </div>
        </>
      )}
    </div>
  );
};

export default GraphView;
