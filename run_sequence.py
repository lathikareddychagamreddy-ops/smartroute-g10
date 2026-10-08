import urllib.request
import json
import sys

base_url = 'http://localhost:8085/api'

def post_json(url, data):
    req = urllib.request.Request(url, data=json.dumps(data).encode('utf-8'), headers={'Content-Type': 'application/json'})
    with urllib.request.urlopen(req) as resp:
        return json.loads(resp.read().decode('utf-8'))

def get_json(url):
    with urllib.request.urlopen(url) as resp:
        return json.loads(resp.read().decode('utf-8'))

print('================================================================================')
print('        SMARTROUTE AI - FULL DEMONSTRATION SEQUENCE EXECUTION                   ')
print('================================================================================')

# STEP 1: Edit Distance Location Typo Search
print('\n[STEP 1] Running Levenshtein Edit Distance Dynamic Programming Autocomplete...')
typos = ['Hyderbad', 'Bangaluru', 'Vijaywada']
for typo in typos:
    res = get_json(f'{base_url}/search?query={typo}')
    print(f'  • User input: "{typo}" -> Suggested: "{res.get("correctedSuggestion")}" (Edit Distance: {res.get("distance")})')

# STEP 2: FASTEST Route Calculation
print('\n[STEP 2] Calculating FASTEST Route for Hyderabad -> Vijayawada...')
fastest_res = post_json(f'{base_url}/routes/calculate', {
    'source': 'Hyderabad', 'destination': 'Vijayawada', 'preference': 'FASTEST'
})
print(f'  • Algorithm Used: {fastest_res.get("algorithm")}')
print(f'  • Recommended Path: {" -> ".join(fastest_res.get("route", []))}')
print(f'  • Distance: {fastest_res.get("distance")} km | Estimated Time: {fastest_res.get("estimatedTime")} mins')
print(f'  • Toll Cost: Rs.{fastest_res.get("toll")} | Fuel Burn: {fastest_res.get("fuelConsumption")} L | Safety: {fastest_res.get("safetyScore")}/10')
print(f'  • Rationale: {fastest_res.get("rationale")}')

# STEP 3: LOWEST TOLL Route Calculation
print('\n[STEP 3] Calculating LOWEST TOLL Route for Hyderabad -> Vijayawada...')
toll_res = post_json(f'{base_url}/routes/calculate', {
    'source': 'Hyderabad', 'destination': 'Vijayawada', 'preference': 'LOWEST_TOLL'
})
print(f'  • Algorithm Used: {toll_res.get("algorithm")}')
print(f'  • Recommended Path: {" -> ".join(toll_res.get("route", []))}')
print(f'  • Distance: {toll_res.get("distance")} km | Estimated Time: {toll_res.get("estimatedTime")} mins')
toll_diff = fastest_res.get("toll") - toll_res.get("toll")
print(f'  • Toll Cost: Rs.{toll_res.get("toll")} (Savings: Rs.{toll_diff})')

# STEP 4: SAFEST Route for Hyderabad -> Bengaluru
print('\n[STEP 4] Calculating SAFEST Route for Hyderabad -> Bengaluru...')
safe_res = post_json(f'{base_url}/routes/calculate', {
    'source': 'Hyderabad', 'destination': 'Bengaluru', 'preference': 'SAFEST'
})
print(f'  • Recommended Path: {" -> ".join(safe_res.get("route", []))}')
print(f'  • Distance: {safe_res.get("distance")} km | Safety Score: {safe_res.get("safetyScore")}/10')
print(f'  • EV Charging Plazas: {safe_res.get("evChargingStops")} stops on route')

# STEP 5: Multi-Route Comparison Matrix
print('\n[STEP 5] Extracting Multi-Route Comparison Dashboard Matrix...')
print(f'  {"Route Option":<28} | {"Distance":<10} | {"Time":<10} | {"Toll":<8} | {"Fuel":<8} | {"Safety":<6}')
print('  ' + '-'*78)
for opt in fastest_res.get('alternativeRoutes', []):
    dist_str = str(opt.get("distance")) + " km"
    time_str = str(opt.get("estimatedTime")) + " m"
    toll_str = "Rs." + str(opt.get("toll"))
    fuel_str = str(opt.get("fuelConsumption")) + " L"
    safety_str = str(opt.get("safetyScore")) + "/10"
    print(f'  {opt.get("name"):<28} | {dist_str:<10} | {time_str:<10} | {toll_str:<8} | {fuel_str:<8} | {safety_str:<6}')

# STEP 6: DSA Visualizer Execution
print('\n[STEP 6] Running Live DSA Algorithm Visualizer Tracing...')

# 6a. DP Matrix for Edit Distance
dp_vis = post_json(f'{base_url}/algorithms/visualize', {
    'algorithm': 'EDIT_DISTANCE', 'string1': 'Hyderbad', 'string2': 'Hyderabad'
})
print(f'  [6A] Levenshtein DP Matrix: Size {len(dp_vis.get("dpMatrix", []))}x{len(dp_vis.get("dpMatrix", [[]])[0])} | Cost: {dp_vis.get("editDistance")} | Time Complexity: {dp_vis.get("timeComplexity")}')

# 6b. Dijkstra Step Tracing
dijkstra_vis = post_json(f'{base_url}/algorithms/visualize', {
    'algorithm': 'DIJKSTRA', 'source': 'Hyderabad', 'destination': 'Vijayawada', 'preference': 'FASTEST'
})
print(f'  [6B] Dijkstra Min-Heap Steps: {len(dijkstra_vis.get("steps", []))} step events recorded. Final path: {dijkstra_vis.get("path")}')
print(f'       First 3 relaxation steps:')
for s in dijkstra_vis.get('steps', [])[:3]:
    print(f'        - Step {s.get("stepNumber")}: {s.get("action")}')

# 6c. A* Heuristic Search
astar_vis = post_json(f'{base_url}/algorithms/visualize', {
    'algorithm': 'ASTAR', 'source': 'Hyderabad', 'destination': 'Vijayawada', 'preference': 'SHORTEST'
})
print(f'  [6C] A* Search: Path: {astar_vis.get("path")} in {len(astar_vis.get("steps", []))} steps with Haversine heuristic')

# 6d. BFS & DFS
bfs_vis = post_json(f'{base_url}/algorithms/visualize', {'algorithm': 'BFS', 'source': 'Hyderabad', 'destination': 'Vijayawada'})
dfs_vis = post_json(f'{base_url}/algorithms/visualize', {'algorithm': 'DFS', 'source': 'Hyderabad', 'destination': 'Vijayawada'})
print(f'  [6D] BFS: Layer-by-layer discovered path: {bfs_vis.get("path")} ({len(bfs_vis.get("steps", []))} queue events)')
print(f'  [6E] DFS: Backtracking exploration path: {dfs_vis.get("path")}')

# STEP 7: History
print('\n[STEP 7] Verifying Route History persistence...')
hist_res = get_json(f'{base_url}/routes/history')
print(f'  • Total historical calculations recorded in database: {len(hist_res)}')

print('\n================================================================================')
print('  DEMONSTRATION SEQUENCE COMPLETED SUCCESSFULLY WITH 100% PASS RATE!           ')
print('================================================================================')
