import React from 'react';
import { 
  BarChart2, Award, CheckCircle, Clock, Compass, Shield, 
  Fuel, CircleDollarSign, Zap, Sparkles, Car, CloudRain
} from 'lucide-react';

const RouteComparison = ({ comparisonData, routes = [] }) => {
  if (!comparisonData || !comparisonData.rows) {
    return (
      <div className="card" style={{ textAlign: 'center', padding: '3rem 1.5rem' }}>
        <BarChart2 size={36} style={{ color: 'var(--text-dim)', marginBottom: '1rem' }} />
        <h3 style={{ color: 'var(--text-muted)' }}>No comparison data generated yet</h3>
        <p style={{ color: 'var(--text-dim)', fontSize: '0.9rem' }}>
          Execute a route search to view side-by-side Java algorithm comparison metrics.
        </p>
      </div>
    );
  }

  const { headers, rows } = comparisonData;

  // Find shortest, fastest, recommended from routes array
  const shortest = routes.find(r => r.algorithm === 'Dijkstra');
  const fastest = routes.find(r => r.algorithm === 'A*');
  const recommended = routes.find(r => r.algorithm === 'Modified Dijkstra');

  return (
    <div className="route-comparison-wrapper">
      {/* Overview Cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '1rem', marginBottom: '1.75rem' }}>
        <div className="card" style={{ borderLeft: '4px solid #38bdf8' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}>
            <span className="badge badge-dijkstra">Dijkstra</span>
            <span style={{ fontSize: '0.8rem', color: 'var(--text-dim)' }}>Distance Minimized</span>
          </div>
          <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#fff', fontFamily: 'var(--font-mono)' }}>
            {shortest?.metrics?.distance || 'N/A'} <span style={{ fontSize: '0.9rem', color: 'var(--text-dim)' }}>km</span>
          </div>
          <div style={{ fontSize: '0.82rem', color: 'var(--text-muted)', marginTop: '0.25rem' }}>
            Travel Time: {shortest?.metrics?.travelTime} min | Toll: ₹{shortest?.metrics?.tollCost}
          </div>
        </div>

        <div className="card" style={{ borderLeft: '4px solid #c084fc' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}>
            <span className="badge badge-astar">A* Search</span>
            <span style={{ fontSize: '0.8rem', color: 'var(--text-dim)' }}>Time Minimized</span>
          </div>
          <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#fff', fontFamily: 'var(--font-mono)' }}>
            {fastest?.metrics?.travelTime || 'N/A'} <span style={{ fontSize: '0.9rem', color: 'var(--text-dim)' }}>min</span>
          </div>
          <div style={{ fontSize: '0.82rem', color: 'var(--text-muted)', marginTop: '0.25rem' }}>
            Distance: {fastest?.metrics?.distance} km | Toll: ₹{fastest?.metrics?.tollCost}
          </div>
        </div>

        <div className="card card-highlighted" style={{ borderLeft: '4px solid #34d399' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}>
            <span className="badge badge-modified">Modified Dijkstra</span>
            <span style={{ fontSize: '0.8rem', color: '#34d399', fontWeight: 700 }}>⭐ BEST BALANCED</span>
          </div>
          <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#34d399', fontFamily: 'var(--font-mono)' }}>
            {recommended?.metrics?.overallScore || 'N/A'} <span style={{ fontSize: '0.9rem', color: 'var(--text-dim)' }}>/100 Utility</span>
          </div>
          <div style={{ fontSize: '0.82rem', color: '#bae6fd', marginTop: '0.25rem' }}>
            Safety: {recommended?.metrics?.safetyScore}/10 | Toll: ₹{recommended?.metrics?.tollCost}
          </div>
        </div>
      </div>

      {/* Side-by-Side Comparison Table */}
      <div className="table-responsive" style={{ marginBottom: '2rem' }}>
        <table className="custom-table">
          <thead>
            <tr>
              {headers && headers.map((h, i) => (
                <th key={i} className={i === 3 ? 'highlight-col' : ''}>
                  {h}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {rows && rows.map((row, idx) => (
              <tr key={idx}>
                <td style={{ fontWeight: 600, color: '#e2e8f0' }}>{row.metric}</td>
                <td>{row.dijkstra}</td>
                <td>{row.aStar}</td>
                <td className="highlight-col" style={{ fontWeight: 700 }}>
                  {row.recommended}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Trade-off Analysis Cards */}
      <div className="card" style={{ background: 'rgba(15, 23, 42, 0.9)' }}>
        <h3 style={{ fontSize: '1.1rem', marginBottom: '0.75rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <Award size={18} style={{ color: '#38bdf8' }} />
          Multi-Criteria Algorithmic Evaluation Summary
        </h3>
        <p style={{ fontSize: '0.88rem', color: 'var(--text-muted)', lineHeight: '1.6' }}>
          While traditional single-objective routers select solely on geographical length (<strong>Dijkstra</strong>) or fastest time (<strong>A*</strong>), 
          <strong> SMARTROUTE AI's Modified Dijkstra</strong> evaluates the multidimensional Pareto frontier across fuel burn, road safety barriers, highway toll avoidance, 
          and scenic ratings. It normalizes individual scales so that user priority sliders dynamically influence the optimal path.
        </p>
      </div>
    </div>
  );
};

export default RouteComparison;
