/**
 * SmartRoute AI - Interactive Map & Simulation Module (Leaflet.js)
 */

let mapInstance = null;
let nodeMarkers = {};
let networkPolyLines = [];
let routePolyLine = null;
let vehicleMarker = null;
let simulationInterval = null;

const DEFAULT_MAP_CENTER = [16.5, 78.8]; // Central South India
const DEFAULT_MAP_ZOOM = 6;

function initMap() {
    const mapContainer = document.getElementById('leafletMap');
    if (!mapContainer || mapInstance) return;

    mapInstance = L.map('leafletMap', {
        center: DEFAULT_MAP_CENTER,
        zoom: DEFAULT_MAP_ZOOM,
        zoomControl: true,
        attributionControl: false
    });

    // Dark Map Tiles (CartoDB Dark Matter)
    L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
        maxZoom: 19,
        subdomains: 'abcd',
        attribution: '&copy; OpenStreetMap contributors &copy; CARTO'
    }).addTo(mapInstance);

    // Initial load of graph roads & locations on map
    loadNetworkOnMap();
}

async function loadNetworkOnMap() {
    if (!mapInstance) return;

    try {
        const [locRes, roadRes] = await Promise.all([
            fetch(`${API_BASE_URL}/locations`),
            fetch(`${API_BASE_URL}/roads`)
        ]);

        if (locRes.ok && roadRes.ok) {
            const locations = await locRes.json();
            const roads = await roadRes.json();
            renderNetworkGraph(locations, roads);
        }
    } catch (e) {
        console.warn("Backend not yet ready for map network loading.");
    }
}

function renderNetworkGraph(locations, roads) {
    if (!mapInstance) return;

    // Clear existing
    Object.values(nodeMarkers).forEach(m => mapInstance.removeLayer(m));
    networkPolyLines.forEach(pl => mapInstance.removeLayer(pl));
    nodeMarkers = {};
    networkPolyLines = [];

    const locMap = {};
    locations.forEach(loc => {
        locMap[loc.name] = loc;

        // Create Circle Marker
        const marker = L.circleMarker([loc.latitude, loc.longitude], {
            radius: 7,
            fillColor: '#38bdf8',
            color: '#ffffff',
            weight: 2,
            opacity: 0.9,
            fillOpacity: 0.9
        }).addTo(mapInstance);

        marker.bindPopup(`
            <div style="font-family: sans-serif; color: #111;">
                <strong>${loc.name}</strong><br>
                <small style="color: #666;">${loc.state || 'India'}</small><br>
                <span>${loc.description || ''}</span>
            </div>
        `);

        nodeMarkers[loc.name] = marker;
    });

    // Draw Roads (Edges)
    roads.forEach(road => {
        const src = locMap[road.source];
        const dest = locMap[road.destination];
        if (src && dest) {
            const line = L.polyline([[src.latitude, src.longitude], [dest.latitude, dest.longitude]], {
                color: '#334155',
                weight: 2.5,
                opacity: 0.75,
                dashArray: '4, 4'
            }).addTo(mapInstance);

            line.bindTooltip(`${road.source} ↔ ${road.destination}<br>Dist: ${road.distance} km | Time: ${road.travelTime}m`, {
                sticky: true
            });

            networkPolyLines.push(line);
        }
    });
}

function highlightCalculatedRouteOnMap(routeCoordinates, routeNames) {
    if (!mapInstance) initMap();
    if (!routeCoordinates || routeCoordinates.length < 2) return;

    // Reset previous route line
    if (routePolyLine) {
        mapInstance.removeLayer(routePolyLine);
        routePolyLine = null;
    }
    if (vehicleMarker) {
        mapInstance.removeLayer(vehicleMarker);
        vehicleMarker = null;
    }
    if (simulationInterval) {
        clearInterval(simulationInterval);
        simulationInterval = null;
    }

    const latLngs = routeCoordinates.map(c => [c.latitude, c.longitude]);

    // Draw active glowing route polyline
    routePolyLine = L.polyline(latLngs, {
        color: '#38bdf8',
        weight: 6,
        opacity: 0.95,
        lineJoin: 'round',
        shadowColor: '#38bdf8',
        shadowBlur: 15
    }).addTo(mapInstance);

    // Highlight source and destination markers
    const srcName = routeNames[0];
    const destName = routeNames[routeNames.length - 1];

    Object.keys(nodeMarkers).forEach(name => {
        const marker = nodeMarkers[name];
        if (name === srcName) {
            marker.setStyle({ fillColor: '#10b981', radius: 10, weight: 3 });
        } else if (name === destName) {
            marker.setStyle({ fillColor: '#f43f5e', radius: 10, weight: 3 });
        } else if (routeNames.includes(name)) {
            marker.setStyle({ fillColor: '#a855f7', radius: 8, weight: 2 });
        } else {
            marker.setStyle({ fillColor: '#475569', radius: 6, weight: 1 });
        }
    });

    // Fit map bounds to show full route
    mapInstance.fitBounds(routePolyLine.getBounds(), { padding: [50, 50] });

    // Show Overlay Info
    const overlay = document.getElementById('mapOverlayInfo');
    if (overlay) {
        overlay.innerHTML = `
            <div style="padding: 0.75rem 1rem; background: rgba(17,24,39,0.9); border: 1px solid rgba(56,189,248,0.4); border-radius: 12px; font-size: 0.85rem;">
                <strong style="color: #38bdf8;">Optimal Path:</strong> ${routeNames.join(' → ')}
            </div>
        `;
        overlay.classList.remove('hidden');
    }
}

function simulateVehicleTrip() {
    if (!routePolyLine || !window.currentCalculatedRoute) {
        alert("Please calculate a route first before starting the simulation.");
        return;
    }

    const coords = window.currentCalculatedRoute.routeCoordinates;
    if (!coords || coords.length < 2) return;

    if (simulationInterval) clearInterval(simulationInterval);
    if (vehicleMarker) mapInstance.removeLayer(vehicleMarker);

    // Custom glowing vehicle icon
    const carIcon = L.divIcon({
        html: `<div style="background: #38bdf8; width: 22px; height: 22px; border-radius: 50%; border: 3px solid #fff; box-shadow: 0 0 15px #38bdf8; display:flex; align-items:center; justify-content:center; color:#0b1120; font-size:10px;"><i class="fa-solid fa-car"></i></div>`,
        className: 'vehicle-div-icon',
        iconSize: [22, 22],
        iconAnchor: [11, 11]
    });

    let currentSegmentIndex = 0;
    let stepRatio = 0;
    const stepsPerSegment = 40;

    vehicleMarker = L.marker([coords[0].latitude, coords[0].longitude], { icon: carIcon }).addTo(mapInstance);

    const simBtn = document.getElementById('simBtn');
    if (simBtn) simBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Simulating...';

    simulationInterval = setInterval(() => {
        if (currentSegmentIndex >= coords.length - 1) {
            clearInterval(simulationInterval);
            simulationInterval = null;
            if (simBtn) simBtn.innerHTML = '<i class="fa-solid fa-play"></i> Replay Trip Simulation';
            return;
        }

        const p1 = coords[currentSegmentIndex];
        const p2 = coords[currentSegmentIndex + 1];

        stepRatio += 1.0 / stepsPerSegment;
        const currentLat = p1.latitude + (p2.latitude - p1.latitude) * stepRatio;
        const currentLng = p1.longitude + (p2.longitude - p1.longitude) * stepRatio;

        vehicleMarker.setLatLng([currentLat, currentLng]);

        if (stepRatio >= 1.0) {
            stepRatio = 0;
            currentSegmentIndex++;
        }
    }, 50);
}

function resetMapView() {
    if (!mapInstance) return;
    mapInstance.setView(DEFAULT_MAP_CENTER, DEFAULT_MAP_ZOOM);
}
