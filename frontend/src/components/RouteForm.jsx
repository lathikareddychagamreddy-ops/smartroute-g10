import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useRoute } from '../context/RouteContext';
import PreferenceSlider from './PreferenceSlider';
import { 
  Navigation, MapPin, Compass, Clock, Shield, Fuel, CircleDollarSign, 
  Car, CloudRain, Sparkles, Zap, AlertTriangle, CheckCircle2, RotateCcw
} from 'lucide-react';

const HYDERABAD_LOCATIONS = [
  'Financial District',
  'Gachibowli',
  'Hitech City',
  'Madhapur',
  'Jubilee Hills',
  'Banjara Hills',
  'Mehdipatnam',
  'Kukatpally',
  'Secunderabad',
  'Begumpet',
  'Panjagutta',
  'Kondapur',
  'Miyapur',
  'Charminar',
  'Shamshabad Airport',
  'Ameerpet',
  'LB Nagar',
  'Dilsukhnagar'
];

const PRESETS = [
  {
    name: 'Fastest Commute',
    icon: Clock,
    apply: (setPref) => {
      setPref('timeWeight', 0.95);
      setPref('distanceWeight', 0.4);
      setPref('trafficWeight', 0.9);
      setPref('tollWeight', 0.1);
      setPref('avoidTolls', false);
      setPref('avoidHeavyTraffic', true);
    }
  },
  {
    name: 'Maximum Safety',
    icon: Shield,
    apply: (setPref) => {
      setPref('safetyWeight', 1.0);
      setPref('weatherWeight', 0.85);
      setPref('preferSafeRoads', true);
      setPref('weatherSensitive', true);
    }
  },
  {
    name: 'Lowest Cost / No Toll',
    icon: CircleDollarSign,
    apply: (setPref) => {
      setPref('tollWeight', 1.0);
      setPref('fuelWeight', 0.9);
      setPref('avoidTolls', true);
    }
  },
  {
    name: 'Scenic Explorer',
    icon: Sparkles,
    apply: (setPref) => {
      setPref('scenicWeight', 1.0);
      setPref('preferScenicRoads', true);
      setPref('timeWeight', 0.3);
    }
  },
  {
    name: 'EV Eco Cruiser',
    icon: Zap,
    apply: (setPref, setVehicle) => {
      setPref('requireEvCharging', true);
      setPref('fuelWeight', 0.8);
      if (setVehicle) setVehicle('Electric');
    }
  }
];

const RouteForm = () => {
  const navigate = useNavigate();
  const {
    startLocation,
    setStartLocation,
    destination,
    setDestination,
    preferences,
    updatePreference,
    setPreferences,
    trafficCondition,
    setTrafficCondition,
    weatherCondition,
    setWeatherCondition,
    vehicleType,
    setVehicleType,
    calculateRoutes,
    loading,
    error,
    setError,
  } = useRoute();

  const handleSwap = () => {
    const temp = startLocation;
    setStartLocation(destination);
    setDestination(temp);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!startLocation || !destination) {
      setError('Please select both a start location and a destination.');
      return;
    }
    if (startLocation === destination) {
      setError('Start location and destination must be different.');
      return;
    }

    try {
      await calculateRoutes();
      navigate('/results');
    } catch (err) {
      // Error handled in context
    }
  };

  return (
    <form onSubmit={handleSubmit} className="route-form">
      {error && (
        <div className="alert alert-danger">
          <AlertTriangle size={18} />
          <div>{error}</div>
        </div>
      )}

      {/* Preset Profiles */}
      <div style={{ marginBottom: '1.5rem' }}>
        <div style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginBottom: '0.6rem', fontWeight: 600 }}>
          QUICK PREFERENCE PROFILES
        </div>
        <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
          {PRESETS.map((preset) => {
            const Icon = preset.icon;
            return (
              <button
                type="button"
                key={preset.name}
                onClick={() => preset.apply(updatePreference, setVehicleType)}
                className="btn btn-secondary"
                style={{ padding: '0.45rem 0.85rem', fontSize: '0.82rem', borderRadius: '9999px' }}
              >
                <Icon size={14} style={{ color: '#38bdf8' }} />
                <span>{preset.name}</span>
              </button>
            );
          })}
        </div>
      </div>

      {/* Location Selectors */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr auto 1fr', gap: '0.75rem', alignItems: 'center', marginBottom: '1.5rem' }}>
        <div className="form-group" style={{ margin: 0 }}>
          <label className="form-label">
            <MapPin size={14} style={{ display: 'inline', marginRight: '0.35rem', color: '#10b981' }} />
            START LOCATION (Source)
          </label>
          <select
            value={startLocation}
            onChange={(e) => setStartLocation(e.target.value)}
            className="form-select"
            required
          >
            {HYDERABAD_LOCATIONS.map((loc) => (
              <option key={`src-${loc}`} value={loc}>
                {loc}
              </option>
            ))}
          </select>
        </div>

        <button
          type="button"
          onClick={handleSwap}
          className="btn btn-secondary"
          title="Swap Start and Destination"
          style={{ padding: '0.75rem', borderRadius: '50%', marginTop: '1.25rem', height: '42px', width: '42px' }}
        >
          <RotateCcw size={16} />
        </button>

        <div className="form-group" style={{ margin: 0 }}>
          <label className="form-label">
            <Navigation size={14} style={{ display: 'inline', marginRight: '0.35rem', color: '#f43f5e' }} />
            DESTINATION (Target)
          </label>
          <select
            value={destination}
            onChange={(e) => setDestination(e.target.value)}
            className="form-select"
            required
          >
            {HYDERABAD_LOCATIONS.map((loc) => (
              <option key={`dest-${loc}`} value={loc}>
                {loc}
              </option>
            ))}
          </select>
        </div>
      </div>

      {/* Environmental Selectors & Vehicle Type */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '1rem', marginBottom: '1.75rem' }}>
        <div className="form-group" style={{ margin: 0 }}>
          <label className="form-label">
            <Car size={14} style={{ display: 'inline', marginRight: '0.35rem', color: '#38bdf8' }} />
            Vehicle Type
          </label>
          <select
            value={vehicleType}
            onChange={(e) => setVehicleType(e.target.value)}
            className="form-select"
          >
            <option value="Petrol">Petrol Car (15 km/L)</option>
            <option value="Diesel">Diesel Car (18 km/L)</option>
            <option value="Electric">Electric Vehicle (EV)</option>
          </select>
        </div>

        <div className="form-group" style={{ margin: 0 }}>
          <label className="form-label">
            <Car size={14} style={{ display: 'inline', marginRight: '0.35rem', color: '#f59e0b' }} />
            Traffic Condition
          </label>
          <select
            value={trafficCondition}
            onChange={(e) => setTrafficCondition(e.target.value)}
            className="form-select"
          >
            <option value="Normal">Normal Traffic (Free Flow)</option>
            <option value="Moderate">Moderate Traffic (Slowdown)</option>
            <option value="Heavy">Heavy Traffic (Congested Gridlock)</option>
          </select>
        </div>

        <div className="form-group" style={{ margin: 0 }}>
          <label className="form-label">
            <CloudRain size={14} style={{ display: 'inline', marginRight: '0.35rem', color: '#818cf8' }} />
            Weather Condition
          </label>
          <select
            value={weatherCondition}
            onChange={(e) => setWeatherCondition(e.target.value)}
            className="form-select"
          >
            <option value="Clear">Clear Weather (Ideal)</option>
            <option value="Rain">Rain (Wet Surfaces)</option>
            <option value="Storm">Severe Storm / Waterlogging</option>
            <option value="Fog">Dense Fog / Low Visibility</option>
          </select>
        </div>
      </div>

      {/* Sliders Grid */}
      <div style={{ marginBottom: '1.5rem' }}>
        <h3 style={{ fontSize: '1rem', marginBottom: '0.75rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <Compass size={18} style={{ color: '#38bdf8' }} />
          Multi-Criteria Optimization Weights (Modified Dijkstra)
        </h3>
        <p style={{ fontSize: '0.82rem', color: 'var(--text-dim)', marginBottom: '1rem' }}>
          Adjust the relative weight percentage for each physical metric. The Java algorithm will normalize these scales and optimize the global cost function.
        </p>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '0.75rem' }}>
          <PreferenceSlider
            title="Distance Importance"
            icon={Compass}
            value={preferences.distanceWeight}
            onChange={(val) => updatePreference('distanceWeight', val)}
          />
          <PreferenceSlider
            title="Travel Time Importance"
            icon={Clock}
            value={preferences.timeWeight}
            onChange={(val) => updatePreference('timeWeight', val)}
          />
          <PreferenceSlider
            title="Safety Rating Importance"
            icon={Shield}
            value={preferences.safetyWeight}
            onChange={(val) => updatePreference('safetyWeight', val)}
          />
          <PreferenceSlider
            title="Fuel Economy Importance"
            icon={Fuel}
            value={preferences.fuelWeight}
            onChange={(val) => updatePreference('fuelWeight', val)}
          />
          <PreferenceSlider
            title="Toll Cost Avoidance"
            icon={CircleDollarSign}
            value={preferences.tollWeight}
            onChange={(val) => updatePreference('tollWeight', val)}
          />
          <PreferenceSlider
            title="Traffic Avoidance"
            icon={Car}
            value={preferences.trafficWeight}
            onChange={(val) => updatePreference('trafficWeight', val)}
          />
          <PreferenceSlider
            title="Weather Resilience"
            icon={CloudRain}
            value={preferences.weatherWeight}
            onChange={(val) => updatePreference('weatherWeight', val)}
          />
          <PreferenceSlider
            title="Scenic Corridors"
            icon={Sparkles}
            value={preferences.scenicWeight}
            onChange={(val) => updatePreference('scenicWeight', val)}
          />
        </div>
      </div>

      {/* Additional Constraints Checkboxes */}
      <div style={{ marginBottom: '2rem' }}>
        <div style={{ fontSize: '0.88rem', fontWeight: 600, color: 'var(--text-muted)', marginBottom: '0.5rem' }}>
          SPECIALIZED ROUTING CONSTRAINTS
        </div>
        <div className="toggle-grid">
          <label className={`toggle-label ${preferences.avoidTolls ? 'active' : ''}`}>
            <input
              type="checkbox"
              checked={preferences.avoidTolls}
              onChange={(e) => updatePreference('avoidTolls', e.target.checked)}
              style={{ display: 'none' }}
            />
            <CircleDollarSign size={16} style={{ color: preferences.avoidTolls ? '#38bdf8' : 'var(--text-dim)' }} />
            <span>Avoid Toll Roads</span>
          </label>

          <label className={`toggle-label ${preferences.avoidHeavyTraffic ? 'active' : ''}`}>
            <input
              type="checkbox"
              checked={preferences.avoidHeavyTraffic}
              onChange={(e) => updatePreference('avoidHeavyTraffic', e.target.checked)}
              style={{ display: 'none' }}
            />
            <Car size={16} style={{ color: preferences.avoidHeavyTraffic ? '#38bdf8' : 'var(--text-dim)' }} />
            <span>Avoid Heavy Traffic</span>
          </label>

          <label className={`toggle-label ${preferences.preferSafeRoads ? 'active' : ''}`}>
            <input
              type="checkbox"
              checked={preferences.preferSafeRoads}
              onChange={(e) => updatePreference('preferSafeRoads', e.target.checked)}
              style={{ display: 'none' }}
            />
            <Shield size={16} style={{ color: preferences.preferSafeRoads ? '#38bdf8' : 'var(--text-dim)' }} />
            <span>Prefer Divided Expressways</span>
          </label>

          <label className={`toggle-label ${preferences.preferScenicRoads ? 'active' : ''}`}>
            <input
              type="checkbox"
              checked={preferences.preferScenicRoads}
              onChange={(e) => updatePreference('preferScenicRoads', e.target.checked)}
              style={{ display: 'none' }}
            />
            <Sparkles size={16} style={{ color: preferences.preferScenicRoads ? '#38bdf8' : 'var(--text-dim)' }} />
            <span>Prefer Scenic Routes</span>
          </label>

          <label className={`toggle-label ${preferences.requireEvCharging ? 'active' : ''}`}>
            <input
              type="checkbox"
              checked={preferences.requireEvCharging}
              onChange={(e) => updatePreference('requireEvCharging', e.target.checked)}
              style={{ display: 'none' }}
            />
            <Zap size={16} style={{ color: preferences.requireEvCharging ? '#38bdf8' : 'var(--text-dim)' }} />
            <span>Require EV Charging (BFS)</span>
          </label>

          <label className={`toggle-label ${preferences.weatherSensitive ? 'active' : ''}`}>
            <input
              type="checkbox"
              checked={preferences.weatherSensitive}
              onChange={(e) => updatePreference('weatherSensitive', e.target.checked)}
              style={{ display: 'none' }}
            />
            <CloudRain size={16} style={{ color: preferences.weatherSensitive ? '#38bdf8' : 'var(--text-dim)' }} />
            <span>Weather-Sensitive Routing</span>
          </label>
        </div>
      </div>

      {/* Submit Button */}
      <button
        type="submit"
        disabled={loading}
        className="btn btn-primary btn-large btn-block"
        style={{ letterSpacing: '0.05em' }}
      >
        {loading ? (
          <>
            <div className="spinner" style={{ width: '20px', height: '20px', border: '3px solid rgba(255,255,255,0.3)', borderTopColor: '#fff', borderRadius: '50%', animation: 'spin 1s linear infinite' }}></div>
            <span>RUNNING JAVA ROUTING ALGORITHMS...</span>
          </>
        ) : (
          <>
            <Navigation size={20} />
            <span>FIND BEST ROUTES</span>
          </>
        )}
      </button>
    </form>
  );
};

export default RouteForm;
