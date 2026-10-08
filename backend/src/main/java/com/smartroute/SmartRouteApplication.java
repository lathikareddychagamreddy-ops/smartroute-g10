package com.smartroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartRouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartRouteApplication.class, args);
        System.out.println("=================================================");
        System.out.println("  SmartRoute AI Backend is running on port 8080 ");
        System.out.println("  DSA Algorithms: Edit Distance, Graph, BFS, DFS, Dijkstra, A*");
        System.out.println("=================================================");
    }
}
