import React from 'react';
import { 
  CheckCircle2, Clock, Compass, Shield, Fuel, CircleDollarSign, 
  Car, Sparkles, Zap, CloudRain, Star, ChevronRight
} from 'lucide-react';

const RouteCard = ({ route, isSelected, onSelect }) => {
  if (!route) return null;

  const { algorithm, name, path, metrics, recommendationReason, executionTimeMs, isRecommended, chargingStops, scenicHighlights } = route;

  const getBadgeClass = (algo) => {
    switch (algo) {
      case 'Dijkstra': return 'badge-dijkstra';
      case 'A*': return 'badge-astar';
      case 'Modified Dijkstra': return 'badge-modified';
      case 'BFS': return 'badge-bfs';
      case 'DFS': return 'badge-dfs';
      default: return 'badge-dijkstra';
    }
  };

  return (
    <div 
      className={`card ${isRecommended ? 'card-highlighted' : ''}`}
      style={{
        cursor: 'pointer',
        borderWidth: isSelected ? '2px' : '1px',
        borderColor: isSelected ? '#38bdf8' : (isRecommended ? 'rgba(56, 189, 248, 0.4)' : 'var(--border-color)'),
        position: 'relative',
        marginBottom: '1.25rem',
      }}
      onClick={() => onSelect && onSelect(route)}
    >
      {isRecommended && (
        <div style={{
          position: 'absolute',
          top: '-12px',
          right: '20px',
          background: 'linear-gradient(135deg, #10b981 0%, #059669 100%)',
          color: '#fff',
          padding: '0.25rem 0.85rem',
          borderRadius: '9999px',
          fontSize: '0.75rem',
          fontWeight: 700,
          letterSpacing: '0.05em',
          display: 'flex',
          alignItems: 'center',
          gap: '0.35rem',
          boxShadow: '0 4px 12px rgba(16, 185, 129, 0.4)'
        }}>
          <Star size={13} fill="#fff" />
          <span>TOP AI RECOMMENDATION</span>
        </div>
      )}

      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '0.85rem', flexWrap: 'wrap', gap: '0.5rem' }}>
        <div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem', marginBottom: '0.35rem' }}>
            <span className={`badge ${getBadgeClass(algorithm)}`}>{algorithm}</span>
            <span style={{ fontSize: '0.75rem', color: 'var(--text-dim)', fontFamily: 'var(--font-mono)' }}>
              ⏱️ {executionTimeMs} ms
            </span>
          </div>
          <h3 style={{ fontSize: '1.2rem', fontWeight: 700 }}>{name}</h3>
        </div>

        {metrics?.overallScore !== undefined && (
          <div style={{ textAlign: 'right' }}>
            <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)', textTransform: 'uppercase', fontWeight: 600 }}>Utility Score</div>
            <div style={{ fontSize: '1.4rem', fontWeight: 800, color: isRecommended ? '#34d399' : '#38bdf8', fontFamily: 'var(--font-mono)' }}>
              {metrics.overallScore}<span style={{ fontSize: '0.85rem', color: 'var(--text-dim)' }}>/100</span>
            </div>
          </div>
        )}
      </div>

      {/* Path Breadcrumbs */}
      <div style={{
        background: 'rgba(11, 17, 32, 0.6)',
        padding: '0.75rem 1rem',
        borderRadius: 'var(--radius-md)',
        border: '1px solid rgba(255, 255, 255, 0.04)',
        marginBottom: '1rem',
        fontSize: '0.86rem',
        display: 'flex',
        alignItems: 'center',
        flexWrap: 'wrap',
        gap: '0.4rem',
      }}>
        <span style={{ color: 'var(--text-dim)', fontWeight: 600, marginRight: '0.25rem' }}>Path:</span>
        {path && path.map((nodeName, idx) => (
          <React.Fragment key={`${nodeName}-${idx}`}>
            <span style={{
              color: idx === 0 ? '#10b981' : (idx === path.length - 1 ? '#f43f5e' : '#e2e8f0'),
              fontWeight: (idx === 0 || idx === path.length - 1) ? 700 : 500,
            }}>
              {nodeName}
            </span>
            {idx < path.length - 1 && (
              <ChevronRight size={13} style={{ color: 'var(--text-dim)' }} />
            )}
          </React.Fragment>
        ))}
      </div>

      {/* Primary Metrics Grid */}
      <div className="stats-grid" style={{ marginBottom: '1rem' }}>
        <div className="stat-box">
          <span className="stat-label">
            <Compass size={12} style={{ display: 'inline', marginRight: '3px' }} />
            Distance
          </span>
          <span className="stat-value">{metrics?.distance} <span style={{ fontSize: '0.75rem', color: 'var(--text-dim)' }}>km</span></span>
        </div>

        <div className="stat-box">
          <span className="stat-label">
            <Clock size={12} style={{ display: 'inline', marginRight: '3px' }} />
            Travel Time
          </span>
          <span className="stat-value">{metrics?.travelTime} <span style={{ fontSize: '0.75rem', color: 'var(--text-dim)' }}>min</span></span>
        </div>

        <div className="stat-box">
          <span className="stat-label">
            <Fuel size={12} style={{ display: 'inline', marginRight: '3px' }} />
            Fuel Cost
          </span>
          <span className="stat-value">₹{Math.round(metrics?.fuelCost || 0)}</span>
        </div>

        <div className="stat-box">
          <span className="stat-label">
            <CircleDollarSign size={12} style={{ display: 'inline', marginRight: '3px' }} />
            Toll Cost
          </span>
          <span className="stat-value" style={{ color: metrics?.tollCost === 0 ? '#34d399' : '#fff' }}>
            {metrics?.tollCost === 0 ? '₹0 (Free)' : `₹${Math.round(metrics?.tollCost || 0)}`}
          </span>
        </div>

        <div className="stat-box">
          <span className="stat-label">
            <Shield size={12} style={{ display: 'inline', marginRight: '3px' }} />
            Safety
          </span>
          <span className="stat-value" style={{ color: '#38bdf8' }}>{metrics?.safetyScore}<span style={{ fontSize: '0.75rem' }}>/10</span></span>
        </div>

        <div className="stat-box">
          <span className="stat-label">
            <Car size={12} style={{ display: 'inline', marginRight: '3px' }} />
            Traffic
          </span>
          <span className="stat-value" style={{ 
            fontSize: '0.95rem',
            color: metrics?.trafficLevel === 'HEAVY' ? '#f43f5e' : (metrics?.trafficLevel === 'MODERATE' ? '#f59e0b' : '#34d399')
          }}>
            {metrics?.trafficLevel}
          </span>
        </div>

        <div className="stat-box">
          <span className="stat-label">
            <Sparkles size={12} style={{ display: 'inline', marginRight: '3px' }} />
            Scenic Score
          </span>
          <span className="stat-value" style={{ color: '#c084fc' }}>{metrics?.scenicScore}<span style={{ fontSize: '0.75rem' }}>/10</span></span>
        </div>

        <div className="stat-box">
          <span className="stat-label">
            <Zap size={12} style={{ display: 'inline', marginRight: '3px' }} />
            EV Hubs
          </span>
          <span className="stat-value" style={{ color: '#fbbf24' }}>{metrics?.evChargingStationsCount} <span style={{ fontSize: '0.75rem' }}>stations</span></span>
        </div>
      </div>

      {/* Recommendation Explanation */}
      {recommendationReason && (
        <div style={{
          background: 'rgba(56, 189, 248, 0.06)',
          borderLeft: '3px solid #38bdf8',
          padding: '0.65rem 0.85rem',
          borderRadius: '0 8px 8px 0',
          fontSize: '0.84rem',
          color: '#bae6fd',
          marginBottom: '0.75rem'
        }}>
          <strong>Insight:</strong> {recommendationReason}
        </div>
      )}

      {/* EV Charging Stops details if present */}
      {chargingStops && chargingStops.length > 0 && (
        <div style={{ marginTop: '0.75rem', fontSize: '0.82rem', color: 'var(--text-muted)' }}>
          <strong style={{ color: '#fbbf24' }}>⚡ EV Charging Stations along path:</strong>
          <ul style={{ paddingLeft: '1.2rem', marginTop: '0.25rem' }}>
            {chargingStops.map((cs) => (
              <li key={cs.id}>
                {cs.name} ({cs.operator}) – {cs.powerKw} kW Fast DC ({cs.availableSlots}/{cs.totalSlots} Slots Free)
              </li>
            ))}
          </ul>
        </div>
      )}

      {/* Scenic highlights */}
      {scenicHighlights && scenicHighlights.length > 0 && (
        <div style={{ marginTop: '0.75rem', fontSize: '0.82rem', color: 'var(--text-muted)' }}>
          <strong style={{ color: '#c084fc' }}>🌿 Scenic Points along corridor:</strong>
          <div style={{ display: 'flex', gap: '0.4rem', flexWrap: 'wrap', marginTop: '0.35rem' }}>
            {scenicHighlights.map((sh, idx) => (
              <span key={idx} style={{
                background: 'rgba(168, 85, 247, 0.12)',
                border: '1px solid rgba(168, 85, 247, 0.25)',
                color: '#e9d5ff',
                padding: '0.2rem 0.55rem',
                borderRadius: '6px',
                fontSize: '0.75rem',
              }}>
                {sh}
              </span>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default RouteCard;
