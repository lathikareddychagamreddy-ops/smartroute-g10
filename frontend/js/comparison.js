/**
 * SmartRoute AI - Route Comparison Dashboard Module (Chart.js)
 */

let radarChartInstance = null;
let barChartInstance = null;

function renderComparisonDashboard(alternativeRoutes, selectedPreference) {
    const emptyState = document.getElementById('comparisonEmptyState');
    const content = document.getElementById('comparisonContent');
    const cardsGrid = document.getElementById('comparisonCardsGrid');

    if (!alternativeRoutes || alternativeRoutes.length === 0) {
        if (emptyState) emptyState.classList.remove('hidden');
        if (content) content.classList.add('hidden');
        return;
    }

    if (emptyState) emptyState.classList.add('hidden');
    if (content) content.classList.remove('hidden');

    // 1. Render Comparison Cards
    cardsGrid.innerHTML = alternativeRoutes.map(opt => {
        const isSelected = opt.preference.toUpperCase() === selectedPreference.toUpperCase();
        return `
            <div class="compare-card ${isSelected ? 'selected-route-card' : ''}">
                <div class="compare-card-header">
                    <span class="compare-card-title">${opt.name}</span>
                    <span class="tag-badge ${isSelected ? 'selected' : 'alt'}">
                        ${isSelected ? 'RECOMMENDED' : opt.algorithm}
                    </span>
                </div>

                <div class="compare-route-path">
                    <i class="fa-solid fa-route" style="color: var(--accent-blue); margin-right: 4px;"></i>
                    <strong>Path:</strong> ${opt.route.join(' → ')}
                </div>

                <div class="compare-metrics-row">
                    <div class="cmetric-box">
                        <div class="cmetric-val">${opt.distance} <small style="font-size:0.65rem;">km</small></div>
                        <div class="cmetric-lbl">Distance</div>
                    </div>
                    <div class="cmetric-box">
                        <div class="cmetric-val">${opt.estimatedTime} <small style="font-size:0.65rem;">m</small></div>
                        <div class="cmetric-lbl">Time</div>
                    </div>
                    <div class="cmetric-box">
                        <div class="cmetric-val">₹${opt.toll}</div>
                        <div class="cmetric-lbl">Toll Cost</div>
                    </div>
                </div>

                <div class="compare-metrics-row">
                    <div class="cmetric-box">
                        <div class="cmetric-val">${opt.fuelConsumption} <small style="font-size:0.65rem;">L</small></div>
                        <div class="cmetric-lbl">Fuel</div>
                    </div>
                    <div class="cmetric-box">
                        <div class="cmetric-val">${opt.safetyScore} <small style="font-size:0.65rem;">/10</small></div>
                        <div class="cmetric-lbl">Safety</div>
                    </div>
                    <div class="cmetric-box">
                        <div class="cmetric-val">${opt.evChargingStops}</div>
                        <div class="cmetric-lbl">EV Chargers</div>
                    </div>
                </div>

                <button class="btn btn-block ${isSelected ? 'btn-primary' : 'btn-secondary'} btn-sm mt-4" 
                        onclick="selectComparisonRoute('${opt.preference}')">
                    ${isSelected ? '<i class="fa-solid fa-check"></i> Selected Option' : '<i class="fa-solid fa-arrow-right"></i> Choose This Route'}
                </button>
            </div>
        `;
    }).join('');

    // 2. Render Charts
    renderComparisonCharts(alternativeRoutes);
}

function renderComparisonCharts(routes) {
    const radarCtx = document.getElementById('radarComparisonChart')?.getContext('2d');
    const barCtx = document.getElementById('barComparisonChart')?.getContext('2d');

    if (!radarCtx || !barCtx) return;

    if (radarChartInstance) radarChartInstance.destroy();
    if (barChartInstance) barChartInstance.destroy();

    // Prepare Radar Data (Normalized scores 0-10)
    const maxDist = Math.max(...routes.map(r => r.distance), 1);
    const maxTime = Math.max(...routes.map(r => r.estimatedTime), 1);
    const maxToll = Math.max(...routes.map(r => r.toll), 1);
    const maxFuel = Math.max(...routes.map(r => r.fuelConsumption), 1);

    const colors = [
        { bg: 'rgba(56, 189, 248, 0.2)', border: '#38bdf8' },
        { bg: 'rgba(168, 85, 247, 0.2)', border: '#a855f7' },
        { bg: 'rgba(16, 185, 129, 0.2)', border: '#10b981' },
        { bg: 'rgba(245, 158, 11, 0.2)', border: '#f59e0b' }
    ];

    const radarDatasets = routes.slice(0, 4).map((r, i) => {
        const color = colors[i % colors.length];
        return {
            label: r.name,
            data: [
                Math.max(1, (10 - (r.distance / maxDist) * 10).toFixed(1)), // Shortness score
                Math.max(1, (10 - (r.estimatedTime / maxTime) * 10).toFixed(1)), // Speed score
                r.safetyScore,
                Math.max(1, (10 - (r.fuelConsumption / maxFuel) * 10).toFixed(1)), // Fuel economy
                Math.max(1, (10 - (r.toll / maxToll) * 10).toFixed(1)), // Toll savings
                Math.min(10, r.evChargingStops * 3 + 2) // EV accessibility
            ],
            backgroundColor: color.bg,
            borderColor: color.border,
            borderWidth: 2,
            pointBackgroundColor: color.border
        };
    });

    radarChartInstance = new Chart(radarCtx, {
        type: 'radar',
        data: {
            labels: ['Shortness', 'Speed/Time', 'Safety Rating', 'Fuel Efficiency', 'Toll Economy', 'EV Readiness'],
            datasets: radarDatasets
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                r: {
                    angleLines: { color: 'rgba(255, 255, 255, 0.1)' },
                    grid: { color: 'rgba(255, 255, 255, 0.1)' },
                    pointLabels: { color: '#94a3b8', font: { size: 11 } },
                    ticks: { display: false, max: 10, min: 0 }
                }
            },
            plugins: {
                legend: { labels: { color: '#f8fafc', font: { size: 11 } } }
            }
        }
    });

    // Bar Chart Data (Distance vs Time)
    const barLabels = routes.map(r => r.name.replace('Route ', ''));
    barChartInstance = new Chart(barCtx, {
        type: 'bar',
        data: {
            labels: barLabels,
            datasets: [
                {
                    label: 'Distance (km)',
                    data: routes.map(r => r.distance),
                    backgroundColor: 'rgba(56, 189, 248, 0.7)',
                    borderColor: '#38bdf8',
                    borderWidth: 1,
                    borderRadius: 6
                },
                {
                    label: 'Travel Time (mins)',
                    data: routes.map(r => r.estimatedTime),
                    backgroundColor: 'rgba(99, 102, 241, 0.7)',
                    borderColor: '#6366f1',
                    borderWidth: 1,
                    borderRadius: 6
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: {
                    grid: { color: 'rgba(255, 255, 255, 0.05)' },
                    ticks: { color: '#94a3b8', font: { size: 10 } }
                },
                y: {
                    grid: { color: 'rgba(255, 255, 255, 0.05)' },
                    ticks: { color: '#94a3b8' }
                }
            },
            plugins: {
                legend: { labels: { color: '#f8fafc', font: { size: 11 } } }
            }
        }
    });
}

function selectComparisonRoute(preference) {
    const radio = document.querySelector(`input[name="routePref"][value="${preference}"]`);
    if (radio) {
        radio.checked = true;
        document.querySelectorAll('.pref-radio-label').forEach(lbl => lbl.classList.remove('active'));
        radio.closest('.pref-radio-label')?.classList.add('active');
    }
    showSection('planSection');
    const form = document.getElementById('routePlanForm');
    if (form) form.dispatchEvent(new Event('submit'));
}
