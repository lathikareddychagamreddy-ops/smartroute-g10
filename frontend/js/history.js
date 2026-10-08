/**
 * SmartRoute AI - Route History Module
 */

async function fetchRouteHistory() {
    const tableBody = document.getElementById('historyTableBody');
    if (!tableBody) return;

    try {
        const url = currentUser ? `${API_BASE_URL}/routes/history?userId=${currentUser.userId}` : `${API_BASE_URL}/routes/history`;
        const res = await fetch(url);

        if (res.ok) {
            const records = await res.json();
            renderHistoryTable(records);
            // Update profile stats if modal is open
            const profileCount = document.getElementById('profileRoutesCount');
            if (profileCount) profileCount.textContent = records.length;
        } else {
            tableBody.innerHTML = `<tr><td colspan="12" class="text-center py-4 text-muted">No search history recorded yet.</td></tr>`;
        }
    } catch (e) {
        tableBody.innerHTML = `<tr><td colspan="12" class="text-center py-4 text-muted">Backend connection offline. Showing local session history.</td></tr>`;
    }
}

function renderHistoryTable(records) {
    const tableBody = document.getElementById('historyTableBody');
    if (!tableBody) return;

    if (!records || records.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="12" class="text-center py-4 text-muted">No route history found. Plan your first route!</td></tr>`;
        return;
    }

    tableBody.innerHTML = records.map((rec, index) => {
        const formattedDate = rec.timestamp ? new Date(rec.timestamp).toLocaleString([], { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' }) : 'Just now';
        return `
            <tr>
                <td>${index + 1}</td>
                <td><strong style="color: #34d399;">${rec.source}</strong></td>
                <td><strong style="color: #fb7185;">${rec.destination}</strong></td>
                <td><span class="table-badge">${rec.preference || 'FASTEST'}</span></td>
                <td><span style="font-family: var(--font-mono); font-size: 0.8rem; color: #38bdf8;">${rec.algorithm || 'Dijkstra'}</span></td>
                <td>${rec.distance} km</td>
                <td>${rec.estimatedTime} mins</td>
                <td>₹${rec.toll}</td>
                <td>${rec.fuelConsumption} L</td>
                <td>${rec.safetyScore}/10</td>
                <td><small style="color: var(--text-muted);">${formattedDate}</small></td>
                <td>
                    <button class="btn btn-glass btn-sm" onclick="replayHistoryRoute('${rec.source}', '${rec.destination}', '${rec.preference}')">
                        <i class="fa-solid fa-arrow-rotate-right"></i> Replay
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}

function replayHistoryRoute(src, dest, pref) {
    document.getElementById('sourceInput').value = src;
    document.getElementById('destInput').value = dest;

    const radio = document.querySelector(`input[name="routePref"][value="${pref}"]`);
    if (radio) {
        radio.checked = true;
        document.querySelectorAll('.pref-radio-label').forEach(lbl => lbl.classList.remove('active'));
        radio.closest('.pref-radio-label')?.classList.add('active');
    }

    showSection('planSection');
    const form = document.getElementById('routePlanForm');
    if (form) form.dispatchEvent(new Event('submit'));
}
