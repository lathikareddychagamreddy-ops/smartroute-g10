import React, { useState, useEffect } from 'react';
import { useRoute } from '../context/RouteContext';
import { routeApi } from '../services/api';
import { 
  Activity, Car, CloudRain, AlertTriangle, ArrowRight, 
  CheckCircle2, RefreshCw, Shield, Clock, CircleDollarSign, Compass, ChevronRight
} from 'lucide-react';

const DynamicConditions = () => {
  const { startLocation, destination, preferences, vehicleType } = useRoute();
  
  const [traffic, setTraffic] = useState('Heavy'); // 'Normal', 'Heavy'
  const [weather, setWeather] = useState('Normal'); // 'Normal', 'Bad Weather'
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchRecalculation = async (currentTraffic = traffic, currentWeather = weather) => {
    setLoading(true);
    setError(null);
    try {
      const resp = await routeApi.recalculateConditions({
        startLocation: startLocation || 'Financial District',
        destination: destination || 'Secunderabad',
        trafficCondition: currentTraffic,
        weatherCondition: currentWeather,
        preferences: preferences,
        vehicleType: vehicleType,
      });
      setData(resp);
    } catch (err) {
      setError('Failed to recalculate dynamic route conditions from backend.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRecalculation(traffic, weather);
  }, [traffic, weather]);

  const beforeRoute = data?.beforeRoute;
  const afterRoute = data?.afterRoute;

  return (
    <div className="dynamic-page">
      <div style={{ marginBottom: '2rem' }}>
        <div className="badge badge-modified" style={{ marginBottom: '0.5rem' }}>
          REAL-TIME GRAPH EDGE ADAPTATION
        </div>
        <h1 style={{ fontSize: '2.2rem', marginBottom: '0.5rem' }}>
          Dynamic Environmental Adaptation
        </h1>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.95rem' }}>
          Simulate changing real-world conditions. When traffic bottlenecks or severe weather occur, the Java Modified Dijkstra algorithm updates graph edge penalties and recalculates the optimal route in real-time.
        </p>
      </div>

      {/* Control Panel */}
      <div className="card" style={{ padding: '1.5rem', marginBottom: '2rem' }}>
        <div style={{ fontSize: '0.9rem', fontWeight: 700, color: 'var(--text-muted)', marginBottom: '1rem', textTransform: 'uppercase' }}>
          Simulate Environmental Events:
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '1.25rem' }}>
          {/* Traffic selector */}
          <div style={{ background: '#0b1120', padding: '1rem', borderRadius: 'var(--radius-md)', border: '1px solid var(--border-color)' }}>
            <div style={{ fontSize: '0.85rem', color: '#fff', fontWeight: 600, marginBottom: '0.5rem', display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
              <Car size={16} style={{ color: '#f59e0b' }} />
              <span>Traffic Condition</span>
            </div>
            <div style={{ display: 'flex', gap: '0.5rem' }}>
              <button
                type="button"
                className={`btn ${traffic === 'Normal' ? 'btn-primary' : 'btn-secondary'}`}
                style={{ flex: 1, padding: '0.5rem', fontSize: '0.82rem' }}
                onClick={() => setTraffic('Normal')}
              >
                Normal Traffic
              </button>
              <button
                type="button"
                className={`btn ${traffic === 'Heavy' ? 'btn-primary' : 'btn-secondary'}`}
                style={{ flex: 1, padding: '0.5rem', fontSize: '0.82rem' }}
                onClick={() => setTraffic('Heavy')}
              >
                Heavy Gridlock
              </button>
            </div>
          </div>

          {/* Weather selector */}
          <div style={{ background: '#0b1120', padding: '1rem', borderRadius: 'var(--radius-md)', border: '1px solid var(--border-color)' }}>
            <div style={{ fontSize: '0.85rem', color: '#fff', fontWeight: 600, marginBottom: '0.5rem', display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
              <CloudRain size={16} style={{ color: '#818cf8' }} />
              <span>Weather Condition</span>
            </div>
            <div style={{ display: 'flex', gap: '0.5rem' }}>
              <button
                type="button"
                className={`btn ${weather === 'Normal' ? 'btn-primary' : 'btn-secondary'}`}
                style={{ flex: 1, padding: '0.5rem', fontSize: '0.82rem' }}
                onClick={() => setWeather('Normal')}
              >
                Normal / Clear
              </button>
              <button
                type="button"
                className={`btn ${weather === 'Bad Weather' ? 'btn-primary' : 'btn-secondary'}`}
                style={{ flex: 1, padding: '0.5rem', fontSize: '0.82rem' }}
                onClick={() => setWeather('Bad Weather')}
              >
                Severe Storm / Waterlogging
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* BEFORE vs AFTER Comparison Card */}
      {loading ? (
        <div style={{ textAlign: 'center', padding: '3rem 1rem' }}>
          <div className="spinner" style={{ width: '40px', height: '40px', border: '3px solid rgba(56,189,248,0.2)', borderTopColor: '#38bdf8', borderRadius: '50%', margin: '0 auto 1rem', animation: 'spin 1s linear infinite' }}></div>
          <p style={{ color: 'var(--text-muted)' }}>Recalculating edge weights and Modified Dijkstra path...</p>
        </div>
      ) : error ? (
        <div className="alert alert-danger">{error}</div>
      ) : data ? (
        <div>
          {/* Explanation Alert Banner */}
          <div className="card" style={{ background: 'rgba(56, 189, 248, 0.08)', borderColor: 'rgba(56, 189, 248, 0.3)', marginBottom: '2rem' }}>
            <h3 style={{ fontSize: '1.1rem', color: '#38bdf8', marginBottom: '0.4rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Activity size={18} />
              <span>{data.conditionSummary}</span>
            </h3>
            <p style={{ fontSize: '0.9rem', color: '#bae6fd', lineHeight: '1.6' }}>
              {data.tradeOffExplanation}
            </p>
          </div>

          {/* Side by Side BEFORE vs AFTER cards */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(350px, 1fr))', gap: '1.5rem', marginBottom: '2rem' }}>
            
            {/* BEFORE Card */}
            <div className="card" style={{ borderTop: '4px solid #94a3b8' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
                <span className="badge" style={{ background: 'rgba(148, 163, 184, 0.15)', color: '#cbd5e1' }}>
                  BEFORE: NORMAL BASELINE
                </span>
                <span style={{ fontSize: '0.8rem', color: 'var(--text-dim)' }}>Clear Conditions</span>
              </div>

              <h3 style={{ fontSize: '1.2rem', marginBottom: '0.75rem' }}>
                {beforeRoute?.name || 'Balanced Route'}
              </h3>

              {/* Path */}
              <div style={{ background: '#0b1120', padding: '0.75rem', borderRadius: '8px', marginBottom: '1rem', fontSize: '0.82rem' }}>
                <div style={{ color: 'var(--text-dim)', marginBottom: '0.25rem', fontWeight: 600 }}>Path:</div>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.35rem', alignItems: 'center' }}>
                  {beforeRoute?.path?.map((node, i) => (
                    <React.Fragment key={`b-${i}`}>
                      <span style={{ color: '#cbd5e1' }}>{node}</span>
                      {i < beforeRoute.path.length - 1 && <ChevronRight size={12} style={{ color: 'var(--text-dim)' }} />}
                    </React.Fragment>
                  ))}
                </div>
              </div>

              {/* Metrics */}
              <div className="stats-grid">
                <div className="stat-box">
                  <span className="stat-label">Distance</span>
                  <span className="stat-value">{beforeRoute?.metrics?.distance} km</span>
                </div>
                <div className="stat-box">
                  <span className="stat-label">Duration</span>
                  <span className="stat-value">{beforeRoute?.metrics?.travelTime} min</span>
                </div>
                <div className="stat-box">
                  <span className="stat-label">Safety</span>
                  <span className="stat-value">{beforeRoute?.metrics?.safetyScore}/10</span>
                </div>
                <div className="stat-box">
                  <span className="stat-label">Cost</span>
                  <span className="stat-value">₹{Math.round((beforeRoute?.metrics?.fuelCost || 0) + (beforeRoute?.metrics?.tollCost || 0))}</span>
                </div>
              </div>
            </div>

            {/* AFTER Card */}
            <div className="card card-highlighted" style={{ borderTop: '4px solid #10b981' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
                <span className="badge badge-modified">
                  AFTER: DYNAMIC ADAPTED
                </span>
                <span style={{ fontSize: '0.8rem', color: '#34d399', fontWeight: 700 }}>
                  {data.routeChanged ? '⚡ Route Shifted' : 'Route Maintained'}
                </span>
              </div>

              <h3 style={{ fontSize: '1.2rem', marginBottom: '0.75rem', color: '#34d399' }}>
                {afterRoute?.name || 'Adapted Optimal Path'}
              </h3>

              {/* Path */}
              <div style={{ background: '#0b1120', padding: '0.75rem', borderRadius: '8px', marginBottom: '1rem', fontSize: '0.82rem' }}>
                <div style={{ color: 'var(--text-dim)', marginBottom: '0.25rem', fontWeight: 600 }}>Path:</div>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.35rem', alignItems: 'center' }}>
                  {afterRoute?.path?.map((node, i) => (
                    <React.Fragment key={`a-${i}`}>
                      <span style={{ color: '#34d399', fontWeight: 600 }}>{node}</span>
                      {i < afterRoute.path.length - 1 && <ChevronRight size={12} style={{ color: 'var(--text-dim)' }} />}
                    </React.Fragment>
                  ))}
                </div>
              </div>

              {/* Metrics */}
              <div className="stats-grid">
                <div className="stat-box">
                  <span className="stat-label">Distance</span>
                  <span className="stat-value">{afterRoute?.metrics?.distance} km</span>
                </div>
                <div className="stat-box">
                  <span className="stat-label">Duration</span>
                  <span className="stat-value" style={{ color: data.timeDifference > 0 ? '#f59e0b' : '#34d399' }}>
                    {afterRoute?.metrics?.travelTime} min
                    <span style={{ fontSize: '0.7rem', display: 'block', color: 'var(--text-dim)' }}>
                      {data.timeDifference >= 0 ? `+${data.timeDifference} min` : `${data.timeDifference} min`}
                    </span>
                  </span>
                </div>
                <div className="stat-box">
                  <span className="stat-label">Safety</span>
                  <span className="stat-value" style={{ color: '#38bdf8' }}>
                    {afterRoute?.metrics?.safetyScore}/10
                  </span>
                </div>
                <div className="stat-box">
                  <span className="stat-label">Cost</span>
                  <span className="stat-value">
                    ₹{Math.round((afterRoute?.metrics?.fuelCost || 0) + (afterRoute?.metrics?.tollCost || 0))}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      ) : null}
    </div>
  );
};

export default DynamicConditions;
