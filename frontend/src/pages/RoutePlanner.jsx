import React from 'react';
import RouteForm from '../components/RouteForm';
import { Sliders, Compass, Layers, ShieldCheck, MapPin } from 'lucide-react';

const RoutePlanner = () => {
  return (
    <div className="planner-page">
      <div style={{ marginBottom: '2rem' }}>
        <div className="badge badge-dijkstra" style={{ marginBottom: '0.5rem' }}>
          MULTI-CRITERIA ROUTE CONFIGURATOR
        </div>
        <h1 style={{ fontSize: '2.2rem', marginBottom: '0.5rem' }}>
          Plan Your Optimal Route
        </h1>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.95rem' }}>
          Select source and destination nodes across the Hyderabad road network. Customize your optimization weights and environmental constraints.
        </p>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'minmax(0, 1fr)', gap: '2rem' }}>
        <div className="card" style={{ padding: '2rem' }}>
          <RouteForm />
        </div>
      </div>
    </div>
  );
};

export default RoutePlanner;
