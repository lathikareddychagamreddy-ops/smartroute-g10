import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { RouteProvider } from './context/RouteContext';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import RoutePlanner from './pages/RoutePlanner';
import Results from './pages/Results';
import Comparison from './pages/Comparison';
import DynamicConditions from './pages/DynamicConditions';
import Algorithms from './pages/Algorithms';
import About from './pages/About';

function App() {
  return (
    <RouteProvider>
      <Router>
        <div className="app-container">
          <div className="ambient-glow-1"></div>
          <div className="ambient-glow-2"></div>

          <Navbar />

          <main className="main-content">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/plan" element={<RoutePlanner />} />
              <Route path="/results" element={<Results />} />
              <Route path="/comparison" element={<Comparison />} />
              <Route path="/dynamic-conditions" element={<DynamicConditions />} />
              <Route path="/algorithms" element={<Algorithms />} />
              <Route path="/about" element={<About />} />
            </Routes>
          </main>

          <footer className="footer">
            <div style={{ maxWidth: '1200px', margin: '0 auto', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
              <div>
                <strong style={{ color: '#fff' }}>SMARTROUTE AI</strong> – Intelligent Multi-Criteria Route Planning and Optimization
                <div style={{ fontSize: '0.78rem', color: 'var(--text-dim)', marginTop: '0.2rem' }}>
                  Pure Java Spring Boot REST API + Native DSA Algorithms (Dijkstra, A*, Mod-Dijkstra, BFS, DFS)
                </div>
              </div>

              <div style={{ fontSize: '0.8rem', color: 'var(--text-dim)' }}>
                Academic Project Demonstrator &copy; 2026
              </div>
            </div>
          </footer>
        </div>
      </Router>
    </RouteProvider>
  );
}

export default App;
