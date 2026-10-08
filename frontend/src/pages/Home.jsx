import React from 'react';
import { Link } from 'react-router-dom';
import { 
  Navigation, Cpu, Compass, Clock, Shield, Fuel, CircleDollarSign, 
  Zap, Sparkles, Car, CloudRain, ArrowRight, CheckCircle2, Award, Layers
} from 'lucide-react';

const FEATURES = [
  {
    title: 'Shortest Distance Route',
    algo: 'Dijkstra (Min-Heap)',
    desc: 'Strictly minimizes geographical road kilometers using non-negative edge weight PriorityQueue relaxations.',
    icon: Compass,
    color: '#38bdf8'
  },
  {
    title: 'Fastest Travel Time',
    algo: 'A* Heuristic Search',
    desc: 'Minimizes duration by combining actual accumulated delay with an admissible Haversine speed heuristic.',
    icon: Clock,
    color: '#c084fc'
  },
  {
    title: 'Safest Route',
    algo: 'Multi-Criteria Engine',
    desc: 'Prioritizes multi-lane divided expressways, illuminated corridors, and avoids high-accident intersections.',
    icon: Shield,
    color: '#10b981'
  },
  {
    title: 'Lowest Cost & Toll-Free',
    algo: 'Cost Optimization',
    desc: 'Bypasses expensive highway toll gates and selects fuel-optimal arterial paths to minimize travel expense.',
    icon: CircleDollarSign,
    color: '#f59e0b'
  },
  {
    title: 'EV Charging Availability',
    algo: 'BFS Layer Traversal',
    desc: 'Discovers verified high-speed DC fast charging plazas and battery swap stations along route corridors.',
    icon: Zap,
    color: '#fbbf24'
  },
  {
    title: 'Scenic Green Corridors',
    algo: 'DFS Path Exploration',
    desc: 'Deeply explores nature parks, lake views (Durgam Cheruvu, Hussain Sagar), and aesthetic ridge corridors.',
    icon: Sparkles,
    color: '#f43f5e'
  },
  {
    title: 'Dynamic Traffic Adaptation',
    algo: 'Modified Dijkstra',
    desc: 'Automatically penalizes congested choke points, dynamically routing around bottlenecks in real-time.',
    icon: Car,
    color: '#38bdf8'
  },
  {
    title: 'Weather-Resilient Routing',
    algo: 'Risk Penalization',
    desc: 'Reroutes away from low-lying flood zones and storm-affected roads to secure safe, elevated transit.',
    icon: CloudRain,
    color: '#818cf8'
  }
];

const Home = () => {
  return (
    <div className="home-page">
      {/* Hero Section */}
      <section style={{ textAlign: 'center', padding: '3.5rem 1rem 4rem', position: 'relative' }}>
        <div className="badge badge-modified" style={{ marginBottom: '1.25rem', padding: '0.4rem 1rem' }}>
          <Sparkles size={14} />
          <span>REAL WORKING JAVA BACKEND + DSA ROUTING ENGINE</span>
        </div>

        <h1 style={{ fontSize: 'clamp(2.5rem, 5vw, 4.2rem)', fontWeight: 800, lineHeight: 1.15, marginBottom: '1.25rem' }}>
          SMARTROUTE <span className="gradient-text">AI</span>
        </h1>
        <h2 style={{ fontSize: 'clamp(1.15rem, 2.5vw, 1.6rem)', color: '#bae6fd', fontWeight: 600, marginBottom: '1.5rem', maxWidth: '850px', margin: '0 auto 1.5rem' }}>
          Intelligent Multi-Criteria Route Planning & Optimization
        </h2>

        <p style={{ maxWidth: '780px', margin: '0 auto 2.5rem', color: 'var(--text-muted)', fontSize: '1.05rem', lineHeight: '1.7' }}>
          Conventional routing engines return only a single shortest path. <strong>SMARTROUTE AI</strong> evaluates 
          multiple competing transportation objectives simultaneously—synthesizing distance, travel duration, road safety, fuel burn, highway tolls, traffic choke points, weather severity, and EV charging stops into customized Pareto-optimal recommendations.
        </p>

        {/* Hero Actions */}
        <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center', flexWrap: 'wrap', marginBottom: '3.5rem' }}>
          <Link to="/plan" className="btn btn-primary btn-large">
            <Navigation size={20} />
            <span>Start Route Planning</span>
            <ArrowRight size={18} />
          </Link>
          <Link to="/algorithms" className="btn btn-secondary btn-large">
            <Cpu size={20} />
            <span>View DSA Algorithms</span>
          </Link>
        </div>

        {/* Quick Stats Grid */}
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem', maxWidth: '1000px', margin: '0 auto' }}>
          <div className="card" style={{ padding: '1.25rem' }}>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#38bdf8', fontFamily: 'var(--font-mono)' }}>5 Algorithms</div>
            <div style={{ fontSize: '0.82rem', color: 'var(--text-dim)', textTransform: 'uppercase', fontWeight: 600 }}>Dijkstra, A*, Mod-Dijkstra, BFS, DFS</div>
          </div>
          <div className="card" style={{ padding: '1.25rem' }}>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#34d399', fontFamily: 'var(--font-mono)' }}>8 Criteria</div>
            <div style={{ fontSize: '0.82rem', color: 'var(--text-dim)', textTransform: 'uppercase', fontWeight: 600 }}>Distance, Time, Safety, Toll, Fuel, Traffic, Weather, Scenic</div>
          </div>
          <div className="card" style={{ padding: '1.25rem' }}>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#c084fc', fontFamily: 'var(--font-mono)' }}>100% Java</div>
            <div style={{ fontSize: '0.82rem', color: 'var(--text-dim)', textTransform: 'uppercase', fontWeight: 600 }}>Spring Boot + Native Graph DSA</div>
          </div>
          <div className="card" style={{ padding: '1.25rem' }}>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#fbbf24', fontFamily: 'var(--font-mono)' }}>Demo Ready</div>
            <div style={{ fontSize: '0.82rem', color: 'var(--text-dim)', textTransform: 'uppercase', fontWeight: 600 }}>Hyderabad 18-Node Road Network</div>
          </div>
        </div>
      </section>

      {/* Feature Cards Section */}
      <section style={{ marginBottom: '4rem' }}>
        <div style={{ textAlign: 'center', marginBottom: '2.5rem' }}>
          <h2 style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>Multi-Criteria Algorithmic Capabilities</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.95rem' }}>
            Each profile optimizes for distinct real-world transportation priorities.
          </p>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(290px, 1fr))', gap: '1.25rem' }}>
          {FEATURES.map((feat, idx) => {
            const Icon = feat.icon;
            return (
              <div key={idx} className="card" style={{ borderTop: `4px solid ${feat.color}` }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
                  <div style={{
                    width: '42px',
                    height: '42px',
                    borderRadius: '12px',
                    background: `${feat.color}20`,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    color: feat.color,
                  }}>
                    <Icon size={22} />
                  </div>
                  <span style={{ fontSize: '0.72rem', color: 'var(--text-dim)', fontFamily: 'var(--font-mono)', fontWeight: 600 }}>
                    {feat.algo}
                  </span>
                </div>

                <h3 style={{ fontSize: '1.15rem', marginBottom: '0.5rem', color: '#fff' }}>
                  {feat.title}
                </h3>
                <p style={{ fontSize: '0.88rem', color: 'var(--text-muted)', lineHeight: '1.5' }}>
                  {feat.desc}
                </p>
              </div>
            );
          })}
        </div>
      </section>

      {/* Route Comparison Preview */}
      <section className="card" style={{ padding: '2.5rem 2rem', marginBottom: '4rem', background: 'linear-gradient(180deg, rgba(15,23,42,0.9) 0%, rgba(8,13,26,0.95) 100%)' }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '2rem', alignItems: 'center' }}>
          <div>
            <div className="badge badge-dijkstra" style={{ marginBottom: '0.75rem' }}>
              SIDE-BY-SIDE EVALUATION
            </div>
            <h2 style={{ fontSize: '1.8rem', marginBottom: '1rem' }}>
              Why Single Shortest Path Is Not Enough
            </h2>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.92rem', lineHeight: '1.7', marginBottom: '1.5rem' }}>
              A shortest-distance route might take you through narrow, heavily congested city lanes with broken roads and 40 minutes of delay.
              A fastest route might charge ₹95 in expressway tolls.
              <strong> SMARTROUTE AI</strong> presents the full Pareto spectrum so drivers and commuters make informed decisions.
            </p>
            <Link to="/plan" className="btn btn-primary">
              <Navigation size={18} />
              <span>Try Live Demonstrator</span>
            </Link>
          </div>

          <div style={{
            background: '#070b14',
            borderRadius: 'var(--radius-md)',
            border: '1px solid var(--border-color)',
            padding: '1.25rem',
          }}>
            <div style={{ fontSize: '0.8rem', color: 'var(--text-dim)', textTransform: 'uppercase', fontWeight: 700, marginBottom: '0.75rem' }}>
              Sample Multi-Objective Comparison (Financial District → Secunderabad)
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.6rem' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.6rem 0.85rem', background: 'rgba(56, 189, 248, 0.08)', borderRadius: '8px', borderLeft: '3px solid #38bdf8' }}>
                <span style={{ fontSize: '0.85rem', color: '#38bdf8', fontWeight: 600 }}>Dijkstra (Shortest)</span>
                <span style={{ fontFamily: 'var(--font-mono)', fontSize: '0.85rem' }}>24.2 km | 38 min | ₹0 Toll</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.6rem 0.85rem', background: 'rgba(168, 85, 247, 0.08)', borderRadius: '8px', borderLeft: '3px solid #c084fc' }}>
                <span style={{ fontSize: '0.85rem', color: '#c084fc', fontWeight: 600 }}>A* (Fastest)</span>
                <span style={{ fontFamily: 'var(--font-mono)', fontSize: '0.85rem' }}>29.5 km | 28 min | ₹95 Toll</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.6rem 0.85rem', background: 'rgba(16, 185, 129, 0.12)', borderRadius: '8px', borderLeft: '3px solid #10b981' }}>
                <span style={{ fontSize: '0.85rem', color: '#34d399', fontWeight: 700 }}>⭐ Recommended (Mod-Dijkstra)</span>
                <span style={{ fontFamily: 'var(--font-mono)', fontSize: '0.85rem', color: '#34d399', fontWeight: 700 }}>25.6 km | 31 min | ₹0 Toll (9.2 Safety)</span>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
