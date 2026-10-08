import React from 'react';
import { 
  Info, Cpu, Layers, Server, Globe, ShieldCheck, 
  Database, GitBranch, ArrowRight, Zap, CloudLightning
} from 'lucide-react';
import { Link } from 'react-router-dom';

const About = () => {
  return (
    <div className="about-page">
      <div style={{ marginBottom: '2rem' }}>
        <div className="badge badge-modified" style={{ marginBottom: '0.5rem' }}>
          PROJECT SPECIFICATIONS & ARCHITECTURE
        </div>
        <h1 style={{ fontSize: '2.2rem', marginBottom: '0.5rem' }}>
          About SMARTROUTE AI
        </h1>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.95rem' }}>
          An academic full-stack transportation intelligence system demonstrating multi-objective graph optimization through native Java Data Structures and Algorithms.
        </p>
      </div>

      {/* System Architecture Diagram */}
      <div className="card" style={{ padding: '2rem', marginBottom: '2rem' }}>
        <h2 style={{ fontSize: '1.4rem', marginBottom: '1.25rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <Layers size={20} style={{ color: '#38bdf8' }} />
          <span>System Architecture & Pipeline</span>
        </h2>

        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
          gap: '1rem',
          textAlign: 'center',
          marginBottom: '1.5rem',
        }}>
          <div style={{ background: '#0b1120', padding: '1.25rem', borderRadius: 'var(--radius-md)', border: '1px solid var(--border-color)' }}>
            <Globe size={24} style={{ color: '#38bdf8', margin: '0 auto 0.5rem' }} />
            <div style={{ fontWeight: 700, fontSize: '0.95rem', color: '#fff' }}>React Frontend</div>
            <div style={{ fontSize: '0.78rem', color: 'var(--text-dim)', marginTop: '0.25rem' }}>Vite + React Router + Axios</div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <ArrowRight size={24} style={{ color: 'var(--text-dim)' }} />
          </div>

          <div style={{ background: '#0b1120', padding: '1.25rem', borderRadius: 'var(--radius-md)', border: '1px solid var(--border-color)' }}>
            <Server size={24} style={{ color: '#34d399', margin: '0 auto 0.5rem' }} />
            <div style={{ fontWeight: 700, fontSize: '0.95rem', color: '#fff' }}>Spring Boot REST API</div>
            <div style={{ fontSize: '0.78rem', color: 'var(--text-dim)', marginTop: '0.25rem' }}>Port 8085 Controllers</div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <ArrowRight size={24} style={{ color: 'var(--text-dim)' }} />
          </div>

          <div style={{ background: '#0b1120', padding: '1.25rem', borderRadius: 'var(--radius-md)', border: '1px solid var(--border-color)' }}>
            <Cpu size={24} style={{ color: '#c084fc', margin: '0 auto 0.5rem' }} />
            <div style={{ fontWeight: 700, fontSize: '0.95rem', color: '#fff' }}>Java Routing Engine</div>
            <div style={{ fontSize: '0.78rem', color: 'var(--text-dim)', marginTop: '0.25rem' }}>Dijkstra / A* / Mod-Dijkstra / BFS / DFS</div>
          </div>
        </div>
      </div>

      {/* Academic Demonstrator Note */}
      <div className="card" style={{ padding: '2rem', marginBottom: '2rem', borderLeft: '4px solid #f59e0b' }}>
        <h3 style={{ fontSize: '1.2rem', color: '#fbbf24', marginBottom: '0.75rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <ShieldCheck size={20} />
          <span>Academic Demonstrator Notice</span>
        </h3>
        <p style={{ fontSize: '0.9rem', color: 'var(--text-muted)', lineHeight: '1.7', marginBottom: '0.75rem' }}>
          This software serves as a verified academic demonstrator illustrating graph data structures and algorithmic trade-offs. 
          It operates on a realistic, curated Hyderabad transportation network consisting of 18 vertices, high-speed arterial expressways (Nehru ORR, PVNR Elevated corridor), scenic parkways (KBR Park, Durgam Cheruvu bridge), and verified high-speed EV charging stations.
        </p>
        <p style={{ fontSize: '0.85rem', color: 'var(--text-dim)' }}>
          *The demonstrator utilizes simulated environmental attributes to guarantee reproducible, zero-external-dependency execution without requiring paid commercial API subscriptions.
        </p>
      </div>

      {/* Future Scope */}
      <div className="card" style={{ padding: '2rem' }}>
        <h3 style={{ fontSize: '1.2rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <Zap size={20} style={{ color: '#38bdf8' }} />
          <span>Future Roadmap & Enterprise Scope</span>
        </h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: '1rem' }}>
          <div style={{ background: '#0b1120', padding: '1rem', borderRadius: 'var(--radius-md)' }}>
            <strong style={{ color: '#38bdf8', fontSize: '0.9rem', display: 'block', marginBottom: '0.25rem' }}>
              Real-Time Traffic APIs
            </strong>
            <span style={{ fontSize: '0.82rem', color: 'var(--text-muted)' }}>
              Direct ingestion of live GPS fleet speeds and incident feeds via TomTom or HERE Traffic REST webhooks.
            </span>
          </div>

          <div style={{ background: '#0b1120', padding: '1rem', borderRadius: 'var(--radius-md)' }}>
            <strong style={{ color: '#34d399', fontSize: '0.9rem', display: 'block', marginBottom: '0.25rem' }}>
              Live Weather Radar Feeds
            </strong>
            <span style={{ fontSize: '0.82rem', color: 'var(--text-muted)' }}>
              Integration with OpenWeatherMap radar polygons to dynamically detect flash flooding and storm bands.
            </span>
          </div>

          <div style={{ background: '#0b1120', padding: '1rem', borderRadius: 'var(--radius-md)' }}>
            <strong style={{ color: '#fbbf24', fontSize: '0.9rem', display: 'block', marginBottom: '0.25rem' }}>
              OCPI EV Roaming Protocols
            </strong>
            <span style={{ fontSize: '0.82rem', color: 'var(--text-muted)' }}>
              Live connector availability, socket reservation, and battery state-of-charge (SoC) consumption modeling.
            </span>
          </div>

          <div style={{ background: '#0b1120', padding: '1rem', borderRadius: 'var(--radius-md)' }}>
            <strong style={{ color: '#c084fc', fontSize: '0.9rem', display: 'block', marginBottom: '0.25rem' }}>
              ML Preference Personalization
            </strong>
            <span style={{ fontSize: '0.82rem', color: 'var(--text-muted)' }}>
              Reinforcement learning to automatically learn user weight preferences from historical trip choices.
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default About;
