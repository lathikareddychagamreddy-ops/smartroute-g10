import React from 'react';

const PreferenceSlider = ({ title, icon: Icon, value, onChange, min = 0, max = 1, step = 0.05, unit = '%' }) => {
  const displayVal = unit === '%' ? Math.round(value * 100) + '%' : value;

  return (
    <div className="slider-container">
      <div className="slider-header">
        <div className="slider-title">
          {Icon && <Icon size={16} style={{ color: '#38bdf8' }} />}
          <span>{title}</span>
        </div>
        <span className="slider-val">{displayVal}</span>
      </div>
      <input
        type="range"
        min={min}
        max={max}
        step={step}
        value={value}
        onChange={(e) => onChange(parseFloat(e.target.value))}
        className="range-slider"
      />
    </div>
  );
};

export default PreferenceSlider;
