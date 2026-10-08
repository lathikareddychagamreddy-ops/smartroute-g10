package com.smartroute.service;

import com.smartroute.model.LocationEntity;
import com.smartroute.model.RoadEntity;
import com.smartroute.model.User;
import com.smartroute.repository.LocationRepository;
import com.smartroute.repository.RoadRepository;
import com.smartroute.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DataInitializerService {

    private final LocationRepository locationRepository;
    private final RoadRepository roadRepository;
    private final UserRepository userRepository;

    public DataInitializerService(LocationRepository locationRepository,
                                  RoadRepository roadRepository,
                                  UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.roadRepository = roadRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initData() {
        if (locationRepository.count() > 0) {
            return; // Data already populated
        }

        // 1. Seed Demo User
        userRepository.save(new User("Demo Student", "demo@smartroute.ai", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918")); // admin123

        // 2. Seed Locations (Vertices)
        List<LocationEntity> locations = Arrays.asList(
            new LocationEntity("Hyderabad", 17.3850, 78.4867, "Telangana", "Capital tech hub of Telangana"),
            new LocationEntity("Suryapet", 17.1439, 79.6239, "Telangana", "Key junction town on NH-65"),
            new LocationEntity("Vijayawada", 16.5062, 80.6480, "Andhra Pradesh", "Major commercial hub on Krishna River"),
            new LocationEntity("Guntur", 16.3067, 80.4365, "Andhra Pradesh", "Educational and agricultural center"),
            new LocationEntity("Warangal", 17.9689, 79.5941, "Telangana", "Historic heritage city with expressway link"),
            new LocationEntity("Kurnool", 15.8281, 78.0373, "Andhra Pradesh", "Gateway city on NH-44"),
            new LocationEntity("Anantapur", 14.6819, 77.6006, "Andhra Pradesh", "NH-44 transit corridor hub"),
            new LocationEntity("Bengaluru", 12.9716, 77.5946, "Karnataka", "Silicon Valley of India"),
            new LocationEntity("Tirupati", 13.6288, 79.4192, "Andhra Pradesh", "Spiritual city & foothill transport hub"),
            new LocationEntity("Chennai", 13.0827, 80.2707, "Tamil Nadu", "Major coastal metropolitan port city"),
            new LocationEntity("Visakhapatnam", 17.6868, 83.2185, "Andhra Pradesh", "Port city & financial capital of AP"),
            new LocationEntity("Rajahmundry", 17.0005, 81.8040, "Andhra Pradesh", "Cultural capital on Godavari River"),
            new LocationEntity("Mahabubnagar", 16.7488, 77.9840, "Telangana", "Fast corridor city on NH-44"),
            new LocationEntity("Pune", 18.5204, 73.8567, "Maharashtra", "Automotive & education capital"),
            new LocationEntity("Mumbai", 19.0760, 72.8777, "Maharashtra", "Financial capital of India")
        );
        locationRepository.saveAll(locations);

        // 3. Seed Roads (Edges with multi-criteria parameters)
        List<RoadEntity> roads = Arrays.asList(
            // Hyderabad <-> Suryapet (NH 65 expressway)
            new RoadEntity("Hyderabad", "Suryapet", 135.0, 110.0, 160.0, 9.2, 9.1, "LOW", "CLEAR", 7.5, true, true),
            
            // Suryapet <-> Vijayawada (NH 65 corridor)
            new RoadEntity("Suryapet", "Vijayawada", 140.0, 125.0, 190.0, 9.5, 8.8, "MEDIUM", "CLEAR", 8.0, true, true),
            
            // Hyderabad <-> Warangal (NH 163 4-lane expressway)
            new RoadEntity("Hyderabad", "Warangal", 148.0, 130.0, 120.0, 9.8, 8.9, "LOW", "CLEAR", 8.6, true, true),
            
            // Warangal <-> Suryapet (State Highway link)
            new RoadEntity("Warangal", "Suryapet", 112.0, 115.0, 40.0, 7.5, 7.8, "LOW", "CLEAR", 7.0, false, false),
            
            // Warangal <-> Vijayawada via Khammam (Alternate bypass route)
            new RoadEntity("Warangal", "Vijayawada", 240.0, 260.0, 90.0, 15.8, 7.6, "LOW", "CLEAR", 8.4, false, false),
            
            // Vijayawada <-> Guntur (NH 16 6-lane bypass)
            new RoadEntity("Vijayawada", "Guntur", 34.0, 35.0, 60.0, 2.4, 9.4, "MEDIUM", "CLEAR", 6.5, true, true),
            
            // Hyderabad <-> Mahabubnagar (NH 44 high speed corridor)
            new RoadEntity("Hyderabad", "Mahabubnagar", 102.0, 85.0, 110.0, 6.9, 9.3, "LOW", "CLEAR", 7.2, true, true),
            
            // Mahabubnagar <-> Kurnool (NH 44 corridor)
            new RoadEntity("Mahabubnagar", "Kurnool", 115.0, 95.0, 130.0, 7.8, 9.2, "LOW", "CLEAR", 7.8, true, true),
            
            // Kurnool <-> Anantapur (NH 44)
            new RoadEntity("Kurnool", "Anantapur", 150.0, 120.0, 160.0, 10.2, 9.0, "LOW", "CLEAR", 7.4, true, true),
            
            // Anantapur <-> Bengaluru (NH 44 direct Bengaluru access)
            new RoadEntity("Anantapur", "Bengaluru", 215.0, 180.0, 220.0, 14.5, 9.5, "MEDIUM", "CLEAR", 8.2, true, true),
            
            // Hyderabad <-> Kurnool (Direct highway)
            new RoadEntity("Hyderabad", "Kurnool", 217.0, 175.0, 240.0, 14.6, 9.1, "LOW", "CLEAR", 7.6, true, true),
            
            // Kurnool <-> Guntur (Via Nandyal / Vinukonda - Non-toll scenic state route)
            new RoadEntity("Kurnool", "Guntur", 260.0, 290.0, 0.0, 16.8, 7.2, "LOW", "CLEAR", 9.2, false, false),
            
            // Kurnool <-> Tirupati (Via Kadapa / NH 40 - Scenic hills & low toll)
            new RoadEntity("Kurnool", "Tirupati", 340.0, 330.0, 85.0, 22.5, 8.4, "LOW", "CLEAR", 9.5, true, false),
            
            // Guntur <-> Tirupati (Via Ongole / Nellore NH 16)
            new RoadEntity("Guntur", "Tirupati", 370.0, 350.0, 310.0, 24.8, 8.9, "MEDIUM", "CLEAR", 8.0, true, true),
            
            // Tirupati <-> Chennai (Direct corridor)
            new RoadEntity("Tirupati", "Chennai", 135.0, 150.0, 90.0, 9.1, 8.5, "HIGH", "RAINY", 7.8, true, true),
            
            // Tirupati <-> Bengaluru (NH 69 / NH 75 via Chittoor)
            new RoadEntity("Tirupati", "Bengaluru", 250.0, 260.0, 140.0, 16.5, 8.7, "MEDIUM", "CLEAR", 8.8, true, true),
            
            // Chennai <-> Bengaluru (NH 48 / Bangalore-Chennai Expressway corridor)
            new RoadEntity("Chennai", "Bengaluru", 345.0, 330.0, 380.0, 23.0, 9.2, "HIGH", "CLEAR", 8.1, true, true),
            
            // Vijayawada <-> Rajahmundry (NH 16 Godavari coastal corridor)
            new RoadEntity("Vijayawada", "Rajahmundry", 160.0, 150.0, 180.0, 10.8, 8.8, "LOW", "CLEAR", 8.7, true, true),
            
            // Rajahmundry <-> Visakhapatnam (NH 16 coastal expressway)
            new RoadEntity("Rajahmundry", "Visakhapatnam", 195.0, 180.0, 210.0, 13.0, 9.1, "LOW", "CLEAR", 9.3, true, true),
            
            // Hyderabad <-> Pune (NH 65 Solapur-Pune expressway)
            new RoadEntity("Hyderabad", "Pune", 560.0, 520.0, 520.0, 37.5, 8.9, "MEDIUM", "CLEAR", 8.0, true, true),
            
            // Pune <-> Mumbai (Mumbai-Pune Expressway - World-class 6 lane)
            new RoadEntity("Pune", "Mumbai", 150.0, 120.0, 320.0, 10.5, 9.7, "HIGH", "RAINY", 9.4, true, true),
            
            // Pune <-> Bengaluru (NH 48 Golden Quadrilateral corridor)
            new RoadEntity("Pune", "Bengaluru", 840.0, 780.0, 720.0, 56.0, 9.0, "LOW", "CLEAR", 8.5, true, true)
        );
        roadRepository.saveAll(roads);
    }
}
