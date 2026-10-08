import React from 'react';
import { Cpu, Zap, Compass, Clock, Sparkles, Layers, Box, CheckCircle2 } from 'lucide-react';

const AlgorithmCard = ({ algo }) => {
  if (!algo) return null;

  const { id, name, purpose, complexity, measuredTimeMs, dataStructures, description, sampleRoute, keyOptimization } = algo;

  const getTheme = (algoId) => {
    switch (algoId) {
      case 'dijkstra':
        return { color: '#38bdf8', badgeClass: 'badge-dijkstra', icon: Compass };
      case 'aStar':
        return { color: '#c084fc', badgeClass: 'badge-astar', icon: Clock };
      case 'modifiedDijkstra':
        return { color: '#34d399', badgeClass: 'badge-modified', icon: Layers };
      case 'bfs':
        return { color: '#fbbf24', badgeClass: 'badge-bfs', icon: Zap };
      case 'dfs':
        return { color: '#fb7185', badgeClass: 'badge-dfs', icon: Sparkles };
      default:
        return { color: '#38bdf8', badgeClass: 'badge-dijkstra', icon: Cpu };
    }
  };

  const theme = getTheme(id);
  const Icon = theme.icon;

  return (
    <div className="card" style={{ borderLeft: `4px solid ${theme.color}`, display: 'flex', flexDirection: 'column', height: '100%' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '0.75rem' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem' }}>
          <div style={{
            width: '36px',
            height: '36px',
            borderRadius: '10px',
            background: `rgba(${id === 'dijkstra' ? '56,189,248' : id === 'aStar' ? '168,85,247' : id === 'modifiedDijkstra' ? '16,185,129' : id === 'bfs' ? '245,158,11' : '244,63,94'}, 0.15)`,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: theme.color,
          }}>
            <Icon size={20} />
          </div>
          <div>
            <h3 style={{ fontSize: '1.15rem', color: '#fff' }}>{name}</h3>
            <span className={`badge ${theme.badgeClass}`} style={{ fontSize: '0.7rem', padding: '0.15rem 0.5rem' }}>
              {purpose}
            </span>
          </div>
        </div>
      </div>

      <p style={{ fontSize: '0.88rem', color: 'var(--text-muted)', marginBottom: '1.25rem', lineHeight: '1.5' }}>
        {description}
      </p>

      {/* Metrics & Complexity Box */}
      <div style={{
        background: 'rgba(11, 17, 32, 0.7)',
        padding: '0.85rem 1rem',
        borderRadius: 'var(--radius-md)',
        border: '1px solid rgba(255, 255, 255, 0.05)',
        marginBottom: '1rem',
      }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.4rem' }}>
          <span style={{ fontSize: '0.78rem', color: 'var(--text-dim)', textTransform: 'uppercase', fontWeight: 600 }}>Theoretical Complexity:</span>
          <span style={{ fontFamily: 'var(--font-mono)', fontSize: '0.85rem', color: '#fff', fontWeight: 700 }}>{complexity}</span>
        </div>

        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
          <span style={{ fontSize: '0.78rem', color: 'var(--text-dim)', textTransform: 'uppercase', fontWeight: 600 }}>Measured Demo Time:</span>
          <span style={{ fontFamily: 'var(--font-mono)', fontSize: '0.88rem', color: theme.color, fontWeight: 700 }}>
            {measuredTimeMs} ms
          </span>
        </div>
      </div>

      {/* Data Structures */}
      <div style={{ marginBottom: '1rem' }}>
        <div style={{ fontSize: '0.75rem', color: 'var(--text-dim)', textTransform: 'uppercase', fontWeight: 600, marginBottom: '0.4rem' }}>
          Java Data Structures:
        </div>
        <div style={{ display: 'flex', gap: '0.35rem', flexWrap: 'wrap' }}>
          {dataStructures && dataStructures.map((ds, idx) => (
            <span key={idx} style={{
              background: 'rgba(255, 255, 255, 0.05)',
              border: '1px solid rgba(255, 255, 255, 0.08)',
              padding: '0.2rem 0.5rem',
              borderRadius: '6px',
              fontSize: '0.75rem',
              color: '#cbd5e1',
              fontFamily: 'var(--font-mono)'
            }}>
              {ds}
            </span>
          ))}
        </div>
      </div>

      {/* Optimization note */}
      {keyOptimization && (
        <div style={{
          marginTop: 'auto',
          fontSize: '0.8rem',
          color: 'var(--text-dim)',
          borderTop: '1px solid rgba(255, 255, 255, 0.06)',
          paddingTop: '0.65rem'
        }}>
          <strong style={{ color: theme.color }}>Key DSA Function:</strong> {keyOptimization}
        </div>
      )}
    </div>
  );
};

export default AlgorithmCard;
