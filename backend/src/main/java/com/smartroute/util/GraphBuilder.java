package com.smartroute.util;

import com.smartroute.model.ChargingStation;
import com.smartroute.model.Edge;
import com.smartroute.model.Graph;
import com.smartroute.model.Node;

public class GraphBuilder {

    public static Graph buildHyderabadDemonstratorGraph() {
        Graph graph = new Graph();

        // 1. Add Vertices / Nodes
        Node financialDistrict = new Node("N1", "Financial District", 17.4156, 78.3489, "Commercial & IT Hub", "Financial and IT mega-campus with wide arterial corridors", true);
        financialDistrict.addChargingStation(new ChargingStation("CS-FD-1", "Tata Power EZ Mega Charger", "Financial District", "Tata Power", 120, 4, 6, 18.5, "Available"));
        financialDistrict.addChargingStation(new ChargingStation("CS-FD-2", "Jio-bp pulse Hub", "Financial District", "Jio-bp", 60, 2, 4, 17.0, "Available"));
        graph.addNode(financialDistrict);

        Node gachibowli = new Node("N2", "Gachibowli", 17.4401, 78.3489, "Tech Park & Sports Hub", "Major intersection connecting ORR, IT corridor, and Hitech City", true);
        gachibowli.addChargingStation(new ChargingStation("CS-GB-1", "Zeon Superfast Station", "Gachibowli", "Zeon Charging", 150, 3, 4, 19.0, "Available"));
        graph.addNode(gachibowli);

        Node hitechCity = new Node("N3", "Hitech City", 17.4474, 78.3762, "Cyber Towers Corridor", "Primary tech corridor with Cyber Towers and Metro connectivity", true);
        hitechCity.addChargingStation(new ChargingStation("CS-HC-1", "Statiq Ultra Hub", "Hitech City", "Statiq", 120, 2, 4, 18.0, "Available"));
        graph.addNode(hitechCity);

        Node madhapur = new Node("N4", "Madhapur", 17.4483, 78.3915, "Durgam Cheruvu Hub", "Vibrant IT center adjacent to Durgam Cheruvu scenic lake", true);
        madhapur.addChargingStation(new ChargingStation("CS-MD-1", "Ather Grid Fast Charger", "Madhapur", "Ather Energy", 50, 5, 6, 16.5, "Available"));
        graph.addNode(madhapur);

        Node jubileeHills = new Node("N5", "Jubilee Hills", 17.4319, 78.4073, "Scenic Ridge Corridor", "Upscale residential hills with rocky terrain and scenic overlooks", false);
        graph.addNode(jubileeHills);

        Node banjaraHills = new Node("N6", "Banjara Hills", 17.4156, 78.4350, "Green Corridor / KBR Park", "Commercial corridor bordering the dense green canopy of KBR National Park", true);
        banjaraHills.addChargingStation(new ChargingStation("CS-BH-1", "Tata Power Green Hub", "Banjara Hills", "Tata Power", 60, 3, 4, 17.5, "Available"));
        graph.addNode(banjaraHills);

        Node mehdipatnam = new Node("N7", "Mehdipatnam", 17.3916, 78.4420, "Transit Hub / PVNR Link", "High-density transit nexus connecting central city to airport expressway", false);
        graph.addNode(mehdipatnam);

        Node kukatpally = new Node("N8", "Kukatpally", 17.4849, 78.4138, "Northern Corridor / JNTU", "Major residential and commercial highway corridor along NH-65", true);
        kukatpally.addChargingStation(new ChargingStation("CS-KP-1", "Shell Recharge Hub", "Kukatpally", "Shell", 120, 4, 6, 18.0, "Available"));
        graph.addNode(kukatpally);

        Node secunderabad = new Node("N9", "Secunderabad", 17.4399, 78.4983, "Railway & Transit Nexus", "Historic twin-city transit gateway and major railway junction", true);
        secunderabad.addChargingStation(new ChargingStation("CS-SC-1", "IndianOil EV Fast Charging", "Secunderabad", "Indian Oil", 60, 2, 4, 16.0, "Available"));
        graph.addNode(secunderabad);

        Node begumpet = new Node("N10", "Begumpet", 17.4447, 78.4664, "Lakeview Arterial Corridor", "Central corridor bordering scenic Hussain Sagar Lake and flyovers", false);
        graph.addNode(begumpet);

        Node panjagutta = new Node("N11", "Panjagutta", 17.4260, 78.4526, "Central Flyover Grid", "High-capacity commercial junction and flyover interchange", false);
        graph.addNode(panjagutta);

        Node kondapur = new Node("N12", "Kondapur", 17.4699, 78.3578, "Botanical Garden Green Link", "Modern residential node bordering Hyderabad Botanical Gardens", true);
        kondapur.addChargingStation(new ChargingStation("CS-KD-1", "Zeon Botanical Hub", "Kondapur", "Zeon Charging", 60, 2, 2, 17.0, "Available"));
        graph.addNode(kondapur);

        Node miyapur = new Node("N13", "Miyapur", 17.4968, 78.3614, "Metro Terminal & Highway Gateway", "Western terminal of Hyderabad Metro with direct highway access", true);
        miyapur.addChargingStation(new ChargingStation("CS-MY-1", "Statiq Metro Station Hub", "Miyapur", "Statiq", 120, 3, 4, 18.0, "Available"));
        graph.addNode(miyapur);

        Node charminar = new Node("N14", "Charminar", 17.3616, 78.4747, "Historic Heritage Core", "Historic cultural center with narrow heritage lanes and heavy footfall", false);
        graph.addNode(charminar);

        Node shamshabadAirport = new Node("N15", "Shamshabad Airport", 17.2403, 78.4294, "International Airport Hub", "Rajiv Gandhi International Airport connected by high-speed expressways", true);
        shamshabadAirport.addChargingStation(new ChargingStation("CS-AP-1", "RGIA Mega Fast Charging Plaza", "Shamshabad Airport", "Tata Power", 150, 8, 10, 19.5, "Available"));
        graph.addNode(shamshabadAirport);

        Node ameerpet = new Node("N16", "Ameerpet", 17.4375, 78.4482, "Metro Interchange Hub", "Central metro junction with dense urban traffic", false);
        graph.addNode(ameerpet);

        Node lbNagar = new Node("N17", "LB Nagar", 17.3457, 78.5522, "Eastern Ring Gateway", "Major junction on Inner and Outer Ring Road eastern arc", true);
        lbNagar.addChargingStation(new ChargingStation("CS-LB-1", "Tata Power Highway Hub", "LB Nagar", "Tata Power", 120, 4, 4, 17.5, "Available"));
        graph.addNode(lbNagar);

        Node dilsukhnagar = new Node("N18", "Dilsukhnagar", 17.3688, 78.5247, "Commercial Arterial Core", "Bustling retail arterial corridor", false);
        graph.addNode(dilsukhnagar);

        // 2. Add Realistic Roads / Edges (Bidirectional)
        long id = 1L;

        // Financial District <-> Gachibowli (Wide Expressway corridor, fast, safe)
        graph.addEdge(new Edge(id++, "Financial District", "Gachibowli", 5.2, 8.0, 48.0, 0.0, 9.2, 1.1, "LOW", 1.5, "CLEAR", 6.5, true, "Financial District Main Blvd", "Expressway"), true);

        // Financial District <-> Shamshabad Airport (ORR Expressway - Fast, Toll, High Safety)
        graph.addEdge(new Edge(id++, "Financial District", "Shamshabad Airport", 28.5, 22.0, 240.0, 95.0, 9.8, 1.0, "LOW", 1.0, "CLEAR", 7.0, true, "Nehru Outer Ring Road (ORR)", "Expressway"), true);

        // Gachibowli <-> Hitech City (Direct IT Link, Moderate traffic)
        graph.addEdge(new Edge(id++, "Gachibowli", "Hitech City", 4.8, 9.5, 45.0, 0.0, 8.8, 1.4, "MODERATE", 2.0, "CLEAR", 6.0, true, "Gachibowli - Hitech City Main Rd", "Arterial"), true);

        // Gachibowli <-> Kondapur (Botanical Garden Road - Scenic, Low Traffic)
        graph.addEdge(new Edge(id++, "Gachibowli", "Kondapur", 4.5, 8.5, 40.0, 0.0, 8.5, 1.2, "LOW", 2.0, "CLEAR", 8.2, true, "Botanical Garden Scenic Way", "Scenic Corridor"), true);

        // Gachibowli <-> Mehdipatnam (Old Mumbai Highway / Tolichowki - Moderate Toll-free)
        graph.addEdge(new Edge(id++, "Gachibowli", "Mehdipatnam", 11.2, 22.0, 98.0, 0.0, 7.2, 1.8, "HEAVY", 4.5, "CLEAR", 4.0, false, "Old Mumbai Highway (Tolichowki)", "Arterial"), true);

        // Hitech City <-> Madhapur (Cyber Towers - Durgam Cheruvu)
        graph.addEdge(new Edge(id++, "Hitech City", "Madhapur", 2.6, 5.5, 24.0, 0.0, 8.5, 1.3, "MODERATE", 2.0, "CLEAR", 7.5, true, "Hitech City Cyber Way", "Arterial"), true);

        // Hitech City <-> Kukatpally (KPHB Link Road - Straight, Good speed)
        graph.addEdge(new Edge(id++, "Hitech City", "Kukatpally", 7.4, 14.0, 68.0, 0.0, 8.4, 1.5, "MODERATE", 3.0, "CLEAR", 5.0, true, "KPHB - Hitech Flyover Link", "Arterial"), true);

        // Hitech City <-> Kondapur (Tech corridor)
        graph.addEdge(new Edge(id++, "Hitech City", "Kondapur", 3.8, 7.0, 34.0, 0.0, 8.6, 1.2, "LOW", 1.8, "CLEAR", 6.5, true, "Kondapur Main Rd", "City Road"), true);

        // Madhapur <-> Jubilee Hills (Durgam Cheruvu Iconic Cable Bridge - Highly Scenic)
        graph.addEdge(new Edge(id++, "Madhapur", "Jubilee Hills", 4.2, 7.5, 38.0, 0.0, 9.0, 1.2, "LOW", 1.5, "CLEAR", 9.6, true, "Durgam Cheruvu Cable Bridge", "Scenic Corridor"), true);

        // Jubilee Hills <-> Banjara Hills (Road No 36 & KBR Park perimeter - Very Scenic, Safe)
        graph.addEdge(new Edge(id++, "Jubilee Hills", "Banjara Hills", 3.6, 7.0, 32.0, 0.0, 9.2, 1.3, "LOW", 1.8, "CLEAR", 9.4, false, "KBR Park Ridge Parkway", "Scenic Corridor"), true);

        // Jubilee Hills <-> Panjagutta (Via Road No 10 / Flyover)
        graph.addEdge(new Edge(id++, "Jubilee Hills", "Panjagutta", 5.8, 12.0, 52.0, 0.0, 8.0, 1.6, "MODERATE", 3.2, "CLEAR", 6.0, false, "Jubilee Hills Rd 10 Extension", "City Road"), true);

        // Banjara Hills <-> Panjagutta (Nagarjuna Circle Flyover)
        graph.addEdge(new Edge(id++, "Banjara Hills", "Panjagutta", 2.8, 6.0, 26.0, 0.0, 8.2, 1.5, "MODERATE", 2.5, "CLEAR", 7.0, true, "Banjara Hills Road No 1", "Arterial"), true);

        // Banjara Hills <-> Mehdipatnam (Masab Tank Flyover - Busy)
        graph.addEdge(new Edge(id++, "Banjara Hills", "Mehdipatnam", 4.3, 10.0, 39.0, 0.0, 7.5, 1.7, "HEAVY", 4.0, "CLEAR", 5.5, false, "Masab Tank - Banjara Link", "Arterial"), true);

        // Mehdipatnam <-> Shamshabad Airport (PVNR Elevated Expressway - Fast, Toll, High Safety)
        graph.addEdge(new Edge(id++, "Mehdipatnam", "Shamshabad Airport", 21.0, 20.0, 180.0, 75.0, 9.5, 1.1, "LOW", 1.2, "CLEAR", 6.8, false, "PVNR Elevated Expressway", "Expressway"), true);

        // Mehdipatnam <-> Charminar (Old City Arterial - Congested heritage route)
        graph.addEdge(new Edge(id++, "Mehdipatnam", "Charminar", 6.5, 18.0, 58.0, 0.0, 6.0, 2.2, "HEAVY", 6.0, "CLEAR", 6.0, false, "Asif Nagar - City Core Way", "City Road"), true);

        // Kukatpally <-> Miyapur (NH-65 Metro Corridor)
        graph.addEdge(new Edge(id++, "Kukatpally", "Miyapur", 4.6, 8.0, 42.0, 0.0, 8.8, 1.3, "LOW", 2.0, "CLEAR", 5.0, true, "Mumbai Highway (NH-65)", "Expressway"), true);

        // Kukatpally <-> Begumpet (Moosapet - Sanath Nagar Corridor)
        graph.addEdge(new Edge(id++, "Kukatpally", "Begumpet", 9.8, 19.0, 88.0, 0.0, 7.8, 1.7, "HEAVY", 4.5, "CLEAR", 4.5, false, "Sanath Nagar Link Rd", "Arterial"), true);

        // Kukatpally <-> Ameerpet (National Highway Corridor)
        graph.addEdge(new Edge(id++, "Kukatpally", "Ameerpet", 8.2, 16.0, 74.0, 0.0, 7.9, 1.6, "MODERATE", 3.8, "CLEAR", 4.8, true, "NH-65 Metro Expressway", "Arterial"), true);

        // Kondapur <-> Miyapur (Hafeezpet Road)
        graph.addEdge(new Edge(id++, "Kondapur", "Miyapur", 4.1, 7.5, 36.0, 0.0, 8.4, 1.2, "LOW", 2.0, "CLEAR", 6.2, true, "Hafeezpet Connector", "City Road"), true);

        // Panjagutta <-> Ameerpet (Central Metro Arterial)
        graph.addEdge(new Edge(id++, "Panjagutta", "Ameerpet", 2.1, 4.5, 18.0, 0.0, 8.0, 1.5, "MODERATE", 2.8, "CLEAR", 4.5, false, "Ameerpet Arterial Way", "City Road"), true);

        // Panjagutta <-> Begumpet (Raj Bhavan - Prakash Nagar Flyover)
        graph.addEdge(new Edge(id++, "Panjagutta", "Begumpet", 3.4, 6.5, 30.0, 0.0, 8.6, 1.3, "LOW", 2.0, "CLEAR", 6.5, false, "Prakash Nagar Flyover", "Arterial"), true);

        // Ameerpet <-> Begumpet (Direct Metro corridor)
        graph.addEdge(new Edge(id++, "Ameerpet", "Begumpet", 2.5, 5.0, 22.0, 0.0, 8.4, 1.4, "MODERATE", 2.2, "CLEAR", 5.0, false, "Begumpet Airport Link", "City Road"), true);

        // Begumpet <-> Secunderabad (Hussain Sagar Lake View / Paradise Flyover - Scenic)
        graph.addEdge(new Edge(id++, "Begumpet", "Secunderabad", 4.8, 9.0, 42.0, 0.0, 8.9, 1.3, "LOW", 2.0, "CLEAR", 8.8, true, "Paradise - Hussain Sagar Lake Parkway", "Scenic Corridor"), true);

        // Panjagutta <-> Charminar (Moazzam Jahi Market - Heritage corridor)
        graph.addEdge(new Edge(id++, "Panjagutta", "Charminar", 8.5, 24.0, 78.0, 0.0, 6.5, 2.1, "HEAVY", 5.5, "CLEAR", 6.8, false, "MJ Market Heritage Arterial", "City Road"), true);

        // Charminar <-> Dilsukhnagar (Old City Ring Link)
        graph.addEdge(new Edge(id++, "Charminar", "Dilsukhnagar", 5.6, 14.0, 50.0, 0.0, 6.8, 1.8, "MODERATE", 4.0, "CLEAR", 5.2, false, "Chaderghat - Dilsukhnagar Rd", "City Road"), true);

        // Charminar <-> Shamshabad Airport (Chandrayangutta Highway)
        graph.addEdge(new Edge(id++, "Charminar", "Shamshabad Airport", 18.2, 26.0, 160.0, 0.0, 7.4, 1.6, "MODERATE", 3.5, "CLEAR", 6.0, true, "Chandrayangutta Airport Highway", "Arterial"), true);

        // Secunderabad <-> Dilsukhnagar (Musheerabad - Koti - Dilsukhnagar)
        graph.addEdge(new Edge(id++, "Secunderabad", "Dilsukhnagar", 10.5, 24.0, 94.0, 0.0, 7.3, 1.9, "HEAVY", 4.8, "CLEAR", 5.0, true, "Musheerabad Arterial Rd", "Arterial"), true);

        // Dilsukhnagar <-> LB Nagar (National Highway-65 Eastern Arc)
        graph.addEdge(new Edge(id++, "Dilsukhnagar", "LB Nagar", 3.5, 7.0, 32.0, 0.0, 8.6, 1.3, "LOW", 2.0, "CLEAR", 5.5, true, "Vijayawada Highway (NH-65)", "Expressway"), true);

        // LB Nagar <-> Shamshabad Airport (ORR South-East Expressway - Fast, Toll, Top Safety)
        graph.addEdge(new Edge(id++, "LB Nagar", "Shamshabad Airport", 24.0, 19.0, 210.0, 80.0, 9.8, 1.0, "LOW", 1.0, "CLEAR", 7.2, true, "Nehru ORR South-East Section", "Expressway"), true);

        return graph;
    }
}
