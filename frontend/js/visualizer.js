/**
 * SmartRoute AI - DSA Algorithm Playground & Step Visualizer
 */

let currentVisualizerTab = 'EDIT_DISTANCE';
let activeAlgorithmSteps = [];
let currentStepIndex = 0;
let autoPlayTimer = null;
let currentVisGraphData = null;

function switchVisualizerTab(tab) {
    currentVisualizerTab = tab;
    document.querySelectorAll('.algo-tab-btn').forEach(b => b.classList.remove('active'));
    event?.target?.closest('.algo-tab-btn')?.classList.add('active');

    const dpPane = document.getElementById('visEditDistance');
    const graphPane = document.getElementById('visGraphAlgo');
    const prefGroup = document.getElementById('visPrefGroup');

    if (tab === 'EDIT_DISTANCE') {
        dpPane.classList.remove('hidden');
        graphPane.classList.add('hidden');
    } else {
        dpPane.classList.add('hidden');
        graphPane.classList.remove('hidden');
        if (prefGroup) {
            prefGroup.style.display = (tab === 'BFS' || tab === 'DFS') ? 'none' : 'block';
        }
        initVisDropdowns();
    }
}

async function initVisDropdowns() {
    const srcSelect = document.getElementById('visSourceSelect');
    const destSelect = document.getElementById('visDestSelect');
    if (!srcSelect || !destSelect || srcSelect.children.length > 0) return;

    try {
        const res = await fetch(`${API_BASE_URL}/locations`);
        if (res.ok) {
            const locs = await res.json();
            srcSelect.innerHTML = locs.map(l => `<option value="${l.name}">${l.name}</option>`).join('');
            destSelect.innerHTML = locs.map(l => `<option value="${l.name}">${l.name}</option>`).join('');
            destSelect.selectedIndex = Math.min(2, locs.length - 1); // Select a different default dest
        }
    } catch (e) {
        console.warn("Could not load dropdown locations.");
    }
}

// 1. EDIT DISTANCE DP MATRIX VISUALIZER
async function runEditDistanceVisualizer() {
    const s1 = document.getElementById('dpStr1').value.trim() || 'Hyderbad';
    const s2 = document.getElementById('dpStr2').value.trim() || 'Hyderabad';

    try {
        const res = await fetch(`${API_BASE_URL}/algorithms/visualize`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ algorithm: 'EDIT_DISTANCE', string1: s1, string2: s2 })
        });

        if (res.ok) {
            const data = await res.json();
            renderDPMatrixTable(data);
        }
    } catch (e) {
        // Local fallback computation for instant offline preview
        const matrix = computeLocalDPMatrix(s1, s2);
        renderDPMatrixTable({
            str1: s1, str2: s2, dpMatrix: matrix,
            editDistance: matrix[s1.length][s2.length],
            description: `Dynamic programming table for converting '${s1}' to '${s2}' with min cost ${matrix[s1.length][s2.length]}`,
            timeComplexity: 'O(m × n)', spaceComplexity: 'O(m × n)'
        });
    }
}

function renderDPMatrixTable(data) {
    const expl = document.getElementById('dpExplanation');
    const container = document.getElementById('dpMatrixTableContainer');
    const s1 = data.str1;
    const s2 = data.str2;
    const matrix = data.dpMatrix;

    expl.innerHTML = `
        <strong>${data.description}</strong><br>
        <span style="color: var(--accent-cyan); font-family: var(--font-mono); font-size: 0.85rem;">
            Time Complexity: ${data.timeComplexity} | Space Complexity: ${data.spaceComplexity} | Edit Distance = <strong>${data.editDistance}</strong>
        </span>
    `;

    let html = `<table class="dp-table"><thead><tr><th>DP</th><th>ε</th>`;
    for (let j = 0; j < s2.length; j++) {
        html += `<th>${s2.charAt(j)}</th>`;
    }
    html += `</tr></thead><tbody>`;

    for (let i = 0; i <= s1.length; i++) {
        html += `<tr><th>${i === 0 ? 'ε' : s1.charAt(i - 1)}</th>`;
        for (let j = 0; j <= s2.length; j++) {
            const val = matrix[i][j];
            const isMatch = (i > 0 && j > 0 && s1.charAt(i - 1).toLowerCase() === s2.charAt(j - 1).toLowerCase());
            const isFinal = (i === s1.length && j === s2.length);

            let cellClass = 'dp-cell';
            if (isFinal) cellClass += ' dp-final';
            else if (isMatch) cellClass += ' dp-match';

            html += `<td class="${cellClass}">${val}</td>`;
        }
        html += `</tr>`;
    }
    html += `</tbody></table>`;
    container.innerHTML = html;
}

function computeLocalDPMatrix(s1, s2) {
    const m = s1.length, n = s2.length;
    const dp = Array.from({ length: m + 1 }, () => Array(n + 1).fill(0));
    for (let i = 0; i <= m; i++) dp[i][0] = i;
    for (let j = 0; j <= n; j++) dp[0][j] = j;
    for (let i = 1; i <= m; i++) {
        for (let j = 1; j <= n; j++) {
            if (s1.charAt(i - 1).toLowerCase() === s2.charAt(j - 1).toLowerCase()) {
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                dp[i][j] = 1 + Math.min(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]);
            }
        }
    }
    return dp;
}

// 2. GRAPH ALGORITHM STEP VISUALIZER
async function runGraphAlgoVisualizer() {
    const src = document.getElementById('visSourceSelect').value;
    const dest = document.getElementById('visDestSelect').value;
    const pref = document.getElementById('visPrefSelect')?.value || 'FASTEST';

    if (src === dest) {
        alert("Please select distinct source and destination cities.");
        return;
    }

    try {
        const res = await fetch(`${API_BASE_URL}/algorithms/visualize`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                algorithm: currentVisualizerTab,
                source: src,
                destination: dest,
                preference: pref
            })
        });

        if (res.ok) {
            const data = await res.json();
            setupGraphSteps(data, src, dest);
        }
    } catch (e) {
        alert("Could not connect to algorithm endpoint on backend.");
    }
}

function setupGraphSteps(data, src, dest) {
    activeAlgorithmSteps = data.steps || [];
    currentStepIndex = 0;

    // Meta bar
    const metaBar = document.getElementById('visMetaBar');
    metaBar.innerHTML = `
        <div class="complexity-pill"><i class="fa-solid fa-clock"></i> Execution: ${data.executionTimeMs} ms</div>
        <div class="complexity-pill"><i class="fa-solid fa-bolt"></i> Time: ${data.timeComplexity}</div>
        <div class="complexity-pill"><i class="fa-solid fa-memory"></i> Space: ${data.spaceComplexity}</div>
        <div class="complexity-pill"><i class="fa-solid fa-flag-checkered"></i> Total Steps: ${activeAlgorithmSteps.length}</div>
    `;

    document.getElementById('prevStepBtn').disabled = true;
    document.getElementById('nextStepBtn').disabled = activeAlgorithmSteps.length <= 1;

    renderStepLog(activeAlgorithmSteps, 0);
    renderStepCanvas(activeAlgorithmSteps[0], src, dest, data.path);
}

function renderStepLog(steps, currentIndex) {
    const logBox = document.getElementById('visLogBox');
    const counterLabel = document.getElementById('stepCounterLabel');

    counterLabel.textContent = `Step ${currentIndex + 1} / ${steps.length}`;

    logBox.innerHTML = steps.map((s, idx) => `
        <div class="vis-log-entry ${idx === currentIndex ? 'active-step' : ''}">
            <strong>Step ${s.stepNumber || idx + 1}:</strong> ${s.action}
        </div>
    `).join('');

    const activeEl = logBox.children[currentIndex];
    if (activeEl) activeEl.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

function nextVisStep() {
    if (currentStepIndex < activeAlgorithmSteps.length - 1) {
        currentStepIndex++;
        updateStepperState();
    }
}

function prevVisStep() {
    if (currentStepIndex > 0) {
        currentStepIndex--;
        updateStepperState();
    }
}

function updateStepperState() {
    document.getElementById('prevStepBtn').disabled = currentStepIndex === 0;
    document.getElementById('nextStepBtn').disabled = currentStepIndex >= activeAlgorithmSteps.length - 1;

    renderStepLog(activeAlgorithmSteps, currentStepIndex);
    const src = document.getElementById('visSourceSelect').value;
    const dest = document.getElementById('visDestSelect').value;
    renderStepCanvas(activeAlgorithmSteps[currentStepIndex], src, dest);
}

function toggleAutoPlayVis() {
    const btn = document.getElementById('autoPlayBtn');
    if (autoPlayTimer) {
        clearInterval(autoPlayTimer);
        autoPlayTimer = null;
        btn.innerHTML = '<i class="fa-solid fa-play"></i> Auto Play';
    } else {
        btn.innerHTML = '<i class="fa-solid fa-pause"></i> Pause';
        autoPlayTimer = setInterval(() => {
            if (currentStepIndex < activeAlgorithmSteps.length - 1) {
                nextVisStep();
            } else {
                clearInterval(autoPlayTimer);
                autoPlayTimer = null;
                btn.innerHTML = '<i class="fa-solid fa-play"></i> Auto Play';
            }
        }, 800);
    }
}

function renderStepCanvas(step, src, dest, finalPath) {
    const canvas = document.getElementById('graphVisCanvas');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Simplified 2D node coordinates for topological visualizer
    const nodes = {
        "Hyderabad": { x: 220, y: 140 },
        "Suryapet": { x: 330, y: 150 },
        "Vijayawada": { x: 440, y: 190 },
        "Guntur": { x: 420, y: 250 },
        "Warangal": { x: 310, y: 70 },
        "Kurnool": { x: 210, y: 270 },
        "Anantapur": { x: 180, y: 350 },
        "Bengaluru": { x: 180, y: 400 },
        "Tirupati": { x: 340, y: 360 },
        "Chennai": { x: 440, y: 380 },
        "Rajahmundry": { x: 520, y: 150 },
        "Visakhapatnam": { x: 560, y: 100 },
        "Mahabubnagar": { x: 180, y: 200 },
        "Pune": { x: 80, y: 100 },
        "Mumbai": { x: 40, y: 60 }
    };

    const edges = [
        ["Hyderabad", "Suryapet"], ["Suryapet", "Vijayawada"], ["Hyderabad", "Warangal"],
        ["Warangal", "Suryapet"], ["Vijayawada", "Guntur"], ["Hyderabad", "Mahabubnagar"],
        ["Mahabubnagar", "Kurnool"], ["Kurnool", "Anantapur"], ["Anantapur", "Bengaluru"],
        ["Hyderabad", "Kurnool"], ["Kurnool", "Tirupati"], ["Guntur", "Tirupati"],
        ["Tirupati", "Chennai"], ["Tirupati", "Bengaluru"], ["Chennai", "Bengaluru"],
        ["Vijayawada", "Rajahmundry"], ["Rajahmundry", "Visakhapatnam"], ["Hyderabad", "Pune"],
        ["Pune", "Mumbai"]
    ];

    // Draw Edges
    edges.forEach(([u, v]) => {
        const p1 = nodes[u], p2 = nodes[v];
        if (p1 && p2) {
            ctx.beginPath();
            ctx.moveTo(p1.x, p1.y);
            ctx.lineTo(p2.x, p2.y);
            ctx.strokeStyle = '#334155';
            ctx.lineWidth = 2;
            ctx.stroke();
        }
    });

    // Active visited node from current step
    const activeNode = step ? (step.currentNode || step.vertex) : null;

    // Draw Nodes
    Object.keys(nodes).forEach(name => {
        const p = nodes[name];
        ctx.beginPath();
        ctx.arc(p.x, p.y, name === activeNode ? 14 : 9, 0, Math.PI * 2);

        if (name === src) {
            ctx.fillStyle = '#10b981';
        } else if (name === dest) {
            ctx.fillStyle = '#f43f5e';
        } else if (name === activeNode) {
            ctx.fillStyle = '#38bdf8';
        } else {
            ctx.fillStyle = '#475569';
        }
        ctx.fill();
        ctx.strokeStyle = '#fff';
        ctx.lineWidth = name === activeNode ? 3 : 1;
        ctx.stroke();

        // Node Label
        ctx.fillStyle = '#e2e8f0';
        ctx.font = '10px Inter, sans-serif';
        ctx.fillText(name, p.x + 12, p.y + 4);
    });
}
