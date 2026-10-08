import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useRoute } from '../context/RouteContext';
import RouteCard from '../components/RouteCard';
import GraphView from '../components/GraphView';
import { 
  Navigation, RefreshCw, BarChart2, MapPin, ArrowRight, 
  Layers, Zap, Sparkles, AlertCircle, Compass, Clock
} from 'lucide-react';

const Results = () => {
  const { 
    startLocation, destination, routeResults, selectedRoute, 
    setSelectedRoute, calculateRoutes, loading, error 
  } = useRoute();

  const [activeTab, setActiveTab] = useState('all');

  // Trigger initial calculation if routeResults is null
  useEffect(() => {
    if (!routeResults && !loading) {
      calculateRoutes().catch(() => {});
    }
  }, []);

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: '5rem 1rem' }}>
        <div style={{
          width: '50px',
          height: '50px',
          border: '4px solid rgba(56, 189, 248, 0.2)',
          borderTopColor: '#38bdf8',
          borderRadius: '50%',
          margin: '0 auto 1.5rem',
          animation: 'spin 1s linear infinite'
        }}></div>
        <h2 style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>Executing Java Routing Engine...</h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>
          Running Dijkstra (Shortest), A* (Fastest), Modified Dijkstra (Multi-Criteria), BFS (EV Hubs), and DFS (Scenic) on Spring Boot backend.
        </p>
      </div>
    );
  }

  if (error && !routeResults) {
    return (
      <div className="card" style={{ textAlign: 'center', padding: '3.5rem 1.5rem', maxWidth: '600px', margin: '2rem auto' }}>
        <AlertCircle size={48} style={{ color: '#f43f5e', marginBottom: '1rem' }} />
        <h2 style={{ marginBottom: '0.75rem' }}>Route Calculation Error</h2>
        <p style={{ color: 'var(--text-muted)', marginBottom: '1.5rem' }}>{error}</p>
        <Link to="/plan" className="btn btn-primary">
          Back to Route Planner
        </Link>
      </div>
    );
  }

  const allRoutes = routeResults?.allRoutes || [];
  const recommendedRoute = routeResults?.recommendedRoute;
  const shortestRoute = routeResults?.shortestRoute;
  const fastestRoute = routeResults?.fastestRoute;
  const evRoute = routeResults?.evChargingRoute;
  const scenicRoute = routeResults?.scenicRoute;

  const displayedRoutes = activeTab === 'all'
    ? allRoutes
    : activeTab === 'recommended'
    ? [recommendedRoute].filter(Boolean)
    : activeTab === 'shortest'
    ? [shortestRoute].filter(Boolean)
    : activeTab === 'fastest'
    ? [fastestRoute].filter(Boolean)
    : activeTab === 'ev'
    ? [evRoute].filter(Boolean)
    : [scenicRoute].filter(Boolean);

  return (
    <div className="results-page">
      {/* Header bar */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1.75rem', flexWrap: 'wrap', gap: '1rem' }}>
        <div>
          <div className="badge badge-modified" style={{ marginBottom: '0.4rem' }}>
            MULTI-CRITERIA RESULTS GENERATED
          </div>
          <h1 style={{ fontSize: '1.9rem', display: 'flex', alignItems: 'center', gap: '0.5rem', flexWrap: 'wrap' }}>
            <span>{startLocation}</span>
            <ArrowRight size={20} style={{ color: '#38bdf8' }} />
            <span>{destination}</span>
          </h1>
          <p style={{ fontSize: '0.85rem', color: 'var(--text-dim)' }}>
            Evaluated {allRoutes.length} algorithmic route alternatives on the Hyderabad demonstrator graph.
          </p>
        </div>

        <div style={{ display: 'flex', gap: '0.6rem', flexWrap: 'wrap' }}>
          <button
            type="button"
            className="btn btn-secondary"
            onClick={() => calculateRoutes()}
            style={{ fontSize: '0.85rem', padding: '0.6rem 1rem' }}
          >
            <RefreshCw size={15} />
            <span>Recalculate</span>
          </button>
          <Link to="/comparison" className="btn btn-primary" style={{ fontSize: '0.85rem', padding: '0.6rem 1rem' }}>
            <BarChart2 size={15} />
            <span>View Comparison Table</span>
          </Link>
          <Link to="/plan" className="btn btn-secondary" style={{ fontSize: '0.85rem', padding: '0.6rem 1rem' }}>
            <span>Adjust Weights</span>
          </Link>
        </div>
      </div>

      {/* Main Grid: Left Route Cards, Right Map Visualizer */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(420px, 1fr))', gap: '1.75rem', alignItems: 'start' }}>
        
        {/* Left Column: Route Cards */}
        <div>
          {/* Tab Filter */}
          <div style={{ display: 'flex', gap: '0.35rem', overflowX: 'auto', paddingBottom: '0.75rem', marginBottom: '1rem' }}>
            <button
              onClick={() => setActiveTab('all')}
              className={`btn ${activeTab === 'all' ? 'btn-primary' : 'btn-secondary'}`}
              style={{ padding: '0.4rem 0.8rem', fontSize: '0.8rem', borderRadius: '9999px' }}
            >
              All Routes ({allRoutes.length})
            </button>
            <button
              onClick={() => setActiveTab('recommended')}
              className={`btn ${activeTab === 'recommended' ? 'btn-primary' : 'btn-secondary'}`}
              style={{ padding: '0.4rem 0.8rem', fontSize: '0.8rem', borderRadius: '9999px' }}
            >
              ⭐ Recommended
            </button>
            <button
              onClick={() => setActiveTab('shortest')}
              className={`btn ${activeTab === 'shortest' ? 'btn-primary' : 'btn-secondary'}`}
              style={{ padding: '0.4rem 0.8rem', fontSize: '0.8rem', borderRadius: '9999px' }}
            >
              Shortest (Dijkstra)
            </button>
            <button
              onClick={() => setActiveTab('fastest')}
              className={`btn ${activeTab === 'fastest' ? 'btn-primary' : 'btn-secondary'}`}
              style={{ padding: '0.4rem 0.8rem', fontSize: '0.8rem', borderRadius: '9999px' }}
            >
              Fastest (A*)
            </button>
            <button
              onClick={() => setActiveTab('ev')}
              className={`btn ${activeTab === 'ev' ? 'btn-primary' : 'btn-secondary'}`}
              style={{ padding: '0.4rem 0.8rem', fontSize: '0.8rem', borderRadius: '9999px' }}
            >
              ⚡ EV Charging (BFS)
            </button>
            <button
              onClick={() => setActiveTab('scenic')}
              className={`btn ${activeTab === 'scenic' ? 'btn-primary' : 'btn-secondary'}`}
              style={{ padding: '0.4rem 0.8rem', fontSize: '0.8rem', borderRadius: '9999px' }}
            >
              🌿 Scenic (DFS)
            </button>
          </div>

          {/* Cards List */}
          {displayedRoutes.map((route, idx) => (
            <RouteCard
              key={route.id || `${route.algorithm}-${idx}`}
              route={route}
              isSelected={selectedRoute?.id === route.id || selectedRoute?.algorithm === route.algorithm}
              onSelect={(r) => setSelectedRoute(r)}
            />
          ))}
        </div>

        {/* Right Column: Interactive Graph Map */}
        <div style={{ position: 'sticky', top: '90px' }}>
          <GraphView routes={allRoutes} activeRoute={selectedRoute} />
        </div>
      </div>
    </div>
  );
};

export default Results;
