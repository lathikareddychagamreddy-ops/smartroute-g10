import React, { useEffect } from 'react';
import { useRoute } from '../context/RouteContext';
import RouteComparison from '../components/RouteComparison';
import { BarChart2, Layers, Award, ArrowRight } from 'lucide-react';
import { Link } from 'react-router-dom';

const Comparison = () => {
  const { startLocation, destination, routeResults, calculateRoutes, loading } = useRoute();

  useEffect(() => {
    if (!routeResults && !loading) {
      calculateRoutes().catch(() => {});
    }
  }, []);

  return (
    <div className="comparison-page">
      <div style={{ marginBottom: '2rem' }}>
        <div className="badge badge-modified" style={{ marginBottom: '0.5rem' }}>
          SIDE-BY-SIDE ROUTE COMPARISON
        </div>
        <h1 style={{ fontSize: '2.2rem', marginBottom: '0.5rem' }}>
          Multi-Criteria Algorithmic Matrix
        </h1>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.95rem' }}>
          Comparing path distance, travel duration, fuel burn, highway tolls, safety ratings, and EV charging stops between{' '}
          <strong style={{ color: '#fff' }}>{startLocation}</strong> and <strong style={{ color: '#fff' }}>{destination}</strong>.
        </p>
      </div>

      <RouteComparison
        comparisonData={routeResults?.comparison}
        routes={routeResults?.allRoutes || []}
      />

      <div style={{ marginTop: '2rem', textAlign: 'center' }}>
        <Link to="/results" className="btn btn-secondary" style={{ marginRight: '0.75rem' }}>
          View Map & Route Cards
        </Link>
        <Link to="/dynamic-conditions" className="btn btn-primary">
          <span>Test Dynamic Conditions</span>
          <ArrowRight size={16} />
        </Link>
      </div>
    </div>
  );
};

export default Comparison;
