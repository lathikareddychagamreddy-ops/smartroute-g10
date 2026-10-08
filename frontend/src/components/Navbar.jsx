import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Route, MapPin, Sliders, BarChart2, Activity, Cpu, Info } from 'lucide-react';

const Navbar = () => {
  const location = useLocation();

  const navItems = [
    { path: '/', label: 'Home', icon: Route },
    { path: '/plan', label: 'Route Planner', icon: Sliders },
    { path: '/results', label: 'Route Results', icon: MapPin },
    { path: '/comparison', label: 'Comparison', icon: BarChart2 },
    { path: '/dynamic-conditions', label: 'Dynamic Conditions', icon: Activity },
    { path: '/algorithms', label: 'DSA Algorithms', icon: Cpu },
    { path: '/about', label: 'About Project', icon: Info },
  ];

  return (
    <header className="navbar">
      <div className="nav-inner">
        <Link to="/" className="brand-logo">
          <div className="brand-icon">
            <Route size={22} />
          </div>
          <div>
            <div className="brand-title">
              SMARTROUTE <span className="gradient-text">AI</span>
            </div>
            <div className="brand-subtitle">Java DSA Multi-Criteria Routing Engine</div>
          </div>
        </Link>

        <nav>
          <ul className="nav-links">
            {navItems.map((item) => {
              const Icon = item.icon;
              const isActive = location.pathname === item.path;
              return (
                <li key={item.path}>
                  <Link to={item.path} className={`nav-link ${isActive ? 'active' : ''}`}>
                    <Icon size={16} />
                    <span>{item.label}</span>
                  </Link>
                </li>
              );
            })}
          </ul>
        </nav>

        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
          <Link to="/plan" className="btn btn-primary" style={{ padding: '0.5rem 1rem', fontSize: '0.85rem' }}>
            Plan Route
          </Link>
        </div>
      </div>
    </header>
  );
};

export default Navbar;
