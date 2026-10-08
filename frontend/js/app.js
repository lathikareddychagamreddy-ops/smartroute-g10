/**
 * SmartRoute AI - Core Application Logic
 */

let searchDebounceTimers = {};
window.currentCalculatedRoute = null;

document.addEventListener('DOMContentLoaded', () => {
    initAuth();
    checkBackendHealth();
    initPreferenceRadioListeners();
    fetchRouteHistory();
    runEditDistanceVisualizer(); // Pre-load DP Matrix demo
});

// Section Navigation
function showSection(sectionId) {
    document.querySelectorAll('.section-view').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));

    const targetSection = document.getElementById(sectionId);
    if (targetSection) {
        targetSection.classList.add('active');
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    // Update Nav Active State
    const navMap = {
        'homeSection': 'navHome',
        'planSection': 'navPlan',
        'mapSection': 'navMap',
        'comparisonSection': 'navCompare',
        'visualizerSection': 'navVisualizer',
        'historySection': 'navHistory',
        'aboutSection': 'navAbout'
    };
    const navBtnId = navMap[sectionId];
    if (navBtnId) document.getElementById(navBtnId)?.classList.add('active');

    // Trigger map invalidation on display
    if (sectionId === 'mapSection') {
        setTimeout(() => {
            if (!mapInstance) initMap();
            else mapInstance.invalidateSize();
        }, 150);
    }
}

function initPreferenceRadioListeners() {
    document.querySelectorAll('input[name="routePref"]').forEach(radio => {
        radio.addEventListener('change', (e) => {
            document.querySelectorAll('.pref-radio-label').forEach(lbl => lbl.classList.remove('active'));
            e.target.closest('.pref-radio-label')?.classList.add('active');
        });
    });
}

// 1. LEVENSHTEIN EDIT DISTANCE LIVE LOCATION SEARCH
function handleLocationSearch(inputId, dropdownId, typoAlertId) {
    const inputEl = document.getElementById(inputId);
    const dropdownEl = document.getElementById(dropdownId);
    const typoAlertEl = document.getElementById(typoAlertId);
    const query = inputEl.value.trim();

    clearTimeout(searchDebounceTimers[inputId]);

    if (!query || query.length < 2) {
        dropdownEl.classList.add('hidden');
        typoAlertEl.classList.add('hidden');
        return;
    }

    searchDebounceTimers[inputId] = setTimeout(async () => {
        try {
            const res = await fetch(`${API_BASE_URL}/search?query=${encodeURIComponent(query)}`);
            if (res.ok) {
                const data = await res.json();
                renderSuggestions(data, inputId, dropdownId, typoAlertId);
            }
        } catch (e) {
            console.warn("Edit distance search offline, using local fallback");
        }
    }, 250);
}

function renderSuggestions(data, inputId, dropdownId, typoAlertId) {
    const dropdownEl = document.getElementById(dropdownId);
    const typoAlertEl = document.getElementById(typoAlertId);
    const inputEl = document.getElementById(inputId);

    // "Did you mean X?" Edit Distance Typo Banner
    if (data.correctedSuggestion && data.correctedSuggestion.toLowerCase() !== inputEl.value.trim().toLowerCase()) {
        typoAlertEl.innerHTML = `
            <i class="fa-solid fa-wand-magic-sparkles"></i> Did you mean 
            <span class="typo-suggestion-link" onclick="applySuggestion('${inputId}', '${dropdownId}', '${typoAlertId}', '${data.correctedSuggestion}')">
                ${data.correctedSuggestion}
            </span>? (Edit Distance: ${data.distance})
        `;
        typoAlertEl.classList.remove('hidden');
    } else {
        typoAlertEl.classList.add('hidden');
    }

    // Autocomplete Dropdown
    if (data.suggestions && data.suggestions.length > 0) {
        dropdownEl.innerHTML = data.suggestions.map(s => `
            <div class="suggestion-item" onclick="applySuggestion('${inputId}', '${dropdownId}', '${typoAlertId}', '${s}')">
                <span><i class="fa-solid fa-location-dot" style="color: var(--accent-blue); margin-right: 6px;"></i> ${s}</span>
                <span class="suggestion-dist-tag">Match</span>
            </div>
        `).join('');
        dropdownEl.classList.remove('hidden');
    } else {
        dropdownEl.classList.add('hidden');
    }
}

function applySuggestion(inputId, dropdownId, typoAlertId, text) {
    document.getElementById(inputId).value = text;
    document.getElementById(dropdownId).classList.add('hidden');
    document.getElementById(typoAlertId).classList.add('hidden');
}

function clearInput(inputId, dropdownId, typoAlertId) {
    document.getElementById(inputId).value = '';
    document.getElementById(dropdownId).classList.add('hidden');
    document.getElementById(typoAlertId).classList.add('hidden');
}

function swapLocations() {
    const src = document.getElementById('sourceInput');
    const dest = document.getElementById('destInput');
    const temp = src.value;
    src.value = dest.value;
    dest.value = temp;
}

function quickPlanRoute(preference) {
    const radio = document.querySelector(`input[name="routePref"][value="${preference}"]`);
    if (radio) {
        radio.checked = true;
        document.querySelectorAll('.pref-radio-label').forEach(lbl => lbl.classList.remove('active'));
        radio.closest('.pref-radio-label')?.classList.add('active');
    }
    document.getElementById('sourceInput').value = 'Hyderabad';
    document.getElementById('destInput').value = 'Vijayawada';
    showSection('planSection');
    const form = document.getElementById('routePlanForm');
    if (form) form.dispatchEvent(new Event('submit'));
}

function applyPreset(src, dest, pref) {
    document.getElementById('sourceInput').value = src;
    document.getElementById('destInput').value = dest;
    const radio = document.querySelector(`input[name="routePref"][value="${pref}"]`);
    if (radio) {
        radio.checked = true;
        document.querySelectorAll('.pref-radio-label').forEach(lbl => lbl.classList.remove('active'));
        radio.closest('.pref-radio-label')?.classList.add('active');
    }
    const form = document.getElementById('routePlanForm');
    if (form) form.dispatchEvent(new Event('submit'));
}

// 2. ROUTE CALCULATION HANDLER
async function handleCalculateRoute(event) {
    if (event) event.preventDefault();

    const src = document.getElementById('sourceInput').value.trim();
    const dest = document.getElementById('destInput').value.trim();
    const pref = document.querySelector('input[name="routePref"]:checked')?.value || 'FASTEST';
    const algo = document.getElementById('algoSelect')?.value || 'AUTO';
    const btn = document.getElementById('calculateBtn');

    if (!src || !dest) {
        alert("Please enter both Source and Destination locations.");
        return;
    }

    btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Running Java DSA Routing...';
    btn.disabled = true;

    try {
        const payload = {
            source: src,
            destination: dest,
            preference: pref,
            algorithm: algo,
            userId: currentUser?.userId || null
        };

        const res = await fetch(`${API_BASE_URL}/routes/calculate`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            const data = await res.json();
            window.currentCalculatedRoute = data;
            renderRouteResultCard(data);

            // Update Map & Comparison
            if (data.routeCoordinates && data.route) {
                highlightCalculatedRouteOnMap(data.routeCoordinates, data.route);
            }
            if (data.alternativeRoutes) {
                renderComparisonDashboard(data.alternativeRoutes, pref);
            }
            if (typeof fetchRouteHistory === 'function') {
                fetchRouteHistory();
            }
        } else {
            alert("Error calculating route. Please verify that the city names are valid.");
        }
    } catch (e) {
        alert("Could not communicate with Java backend at " + API_BASE_URL + ". Ensure Spring Boot is running.");
    } finally {
        btn.innerHTML = '<i class="fa-solid fa-magnifying-glass-location"></i> Find Best Route';
        btn.disabled = false;
    }
}

function renderRouteResultCard(data) {
    const placeholder = document.getElementById('routeResultPlaceholder');
    const resultCard = document.getElementById('routeResultCard');

    if (placeholder) placeholder.classList.add('hidden');
    if (resultCard) {
        resultCard.classList.remove('hidden');

        const pathHtml = data.route.map((node, i) => {
            const isSrc = i === 0;
            const isDest = i === data.route.length - 1;
            const nodeClass = isSrc ? 'src-node' : (isDest ? 'dest-node' : '');
            return `
                <span class="path-node ${nodeClass}">
                    <i class="fa-solid ${isSrc ? 'fa-location-dot' : (isDest ? 'fa-flag-checkered' : 'fa-circle-dot')}"></i>
                    ${node}
                </span>
                ${i < data.route.length - 1 ? '<i class="fa-solid fa-arrow-right path-arrow"></i>' : ''}
            `;
        }).join('');

        resultCard.innerHTML = `
            <div class="result-card-header">
                <div>
                    <div class="result-badge-row">
                        <span class="algo-used-badge"><i class="fa-solid fa-code-fork"></i> ${data.algorithm}</span>
                        <span class="pref-badge"><i class="fa-solid fa-filter"></i> ${data.preference}</span>
                    </div>
                    <h3 style="font-size: 1.4rem;">Optimal Route Recommendation</h3>
                </div>
                <button class="btn btn-glass btn-sm" onclick="showSection('mapSection')">
                    <i class="fa-solid fa-map-location-dot"></i> View on Map
                </button>
            </div>

            <div class="route-path-flow">
                ${pathHtml}
            </div>

            <!-- Metrics Grid -->
            <div class="metrics-grid">
                <div class="metric-item">
                    <div class="metric-icon" style="color: #38bdf8;"><i class="fa-solid fa-road"></i></div>
                    <div class="metric-val">${data.distance} <small style="font-size: 0.75rem;">km</small></div>
                    <div class="metric-lbl">Total Distance</div>
                </div>

                <div class="metric-item">
                    <div class="metric-icon" style="color: #6366f1;"><i class="fa-solid fa-clock"></i></div>
                    <div class="metric-val">${data.estimatedTime} <small style="font-size: 0.75rem;">mins</small></div>
                    <div class="metric-lbl">Travel Time</div>
                </div>

                <div class="metric-item">
                    <div class="metric-icon" style="color: #ec4899;"><i class="fa-solid fa-indian-rupee-sign"></i></div>
                    <div class="metric-val">₹${data.toll}</div>
                    <div class="metric-lbl">Toll Charges</div>
                </div>

                <div class="metric-item">
                    <div class="metric-icon" style="color: #f59e0b;"><i class="fa-solid fa-gas-pump"></i></div>
                    <div class="metric-val">${data.fuelConsumption} <small style="font-size: 0.75rem;">L</small></div>
                    <div class="metric-lbl">Fuel Burn</div>
                </div>

                <div class="metric-item">
                    <div class="metric-icon" style="color: #10b981;"><i class="fa-solid fa-shield-halved"></i></div>
                    <div class="metric-val">${data.safetyScore} <small style="font-size: 0.75rem;">/ 10</small></div>
                    <div class="metric-lbl">Safety Rating</div>
                </div>

                <div class="metric-item">
                    <div class="metric-icon" style="color: #06b6d4;"><i class="fa-solid fa-charging-station"></i></div>
                    <div class="metric-val">${data.evChargingStops}</div>
                    <div class="metric-lbl">EV Charging Stops</div>
                </div>

                <div class="metric-item">
                    <div class="metric-icon" style="color: #ef4444;"><i class="fa-solid fa-traffic-light"></i></div>
                    <div class="metric-val" style="font-size: 1.1rem; text-transform: uppercase;">${data.trafficLevel || 'LOW'}</div>
                    <div class="metric-lbl">Traffic Level</div>
                </div>

                <div class="metric-item">
                    <div class="metric-icon" style="color: #3b82f6;"><i class="fa-solid fa-cloud"></i></div>
                    <div class="metric-val" style="font-size: 1.1rem; text-transform: uppercase;">${data.weatherCondition || 'CLEAR'}</div>
                    <div class="metric-lbl">Weather Forecast</div>
                </div>
            </div>

            <div class="rationale-box">
                <i class="fa-solid fa-circle-info" style="color: var(--accent-blue); margin-right: 6px;"></i>
                <strong>Algorithm Rationale:</strong> ${data.rationale}
            </div>

            <div class="result-actions">
                <button class="btn btn-primary" onclick="showSection('comparisonSection')">
                    <i class="fa-solid fa-chart-simple"></i> Compare With 8 Alternatives
                </button>
                <button class="btn btn-secondary" onclick="showSection('mapSection'); simulateVehicleTrip();">
                    <i class="fa-solid fa-play"></i> Simulate Trip on Map
                </button>
            </div>
        `;
    }
}

// 3. BACKEND HEALTH CHECKER
async function checkBackendHealth() {
    const badgeText = document.getElementById('backendStatusText');
    const badge = document.getElementById('backendStatusBadge');
    try {
        const res = await fetch(`${API_BASE_URL}/locations`, { method: 'GET' });
        if (res.ok) {
            badge.style.background = 'rgba(16, 185, 129, 0.1)';
            badge.style.borderColor = 'rgba(16, 185, 129, 0.3)';
            badge.style.color = '#34d399';
            badgeText.textContent = 'Backend: Online (Port 8080)';
        } else {
            throw new Error();
        }
    } catch (e) {
        badge.style.background = 'rgba(245, 158, 11, 0.1)';
        badge.style.borderColor = 'rgba(245, 158, 11, 0.3)';
        badge.style.color = '#fbbf24';
        badgeText.textContent = 'Backend: Connecting... (Port 8080)';
    }
}

// Polling for backend health
setInterval(checkBackendHealth, 8000);
