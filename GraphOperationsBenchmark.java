package benchmark;

import graph.UserNode;
import graph.TransactionEdge;
import models.FraudDataModel;

import java.util.*;

public class GraphOperationsBenchmark {

    public static void main(String[] args) {
        int[] sizes = {10, 50, 100, 500, 1000};

        for (int size : sizes) {
            Map<String, UserNode> graph = generateGraph(size);
            System.out.println("\n=== Testing Graph Size: " + size + " ===");

            // Benchmark search
            long start = System.nanoTime();
            for (int i = 0; i < size; i++) {
                String id = "Node-" + i;
                graph.get(id);
            }
            long end = System.nanoTime();
            System.out.println("Search Time: " + (end - start) / 1e6 + " ms");

            // Benchmark insertion
            start = System.nanoTime();
            for (int i = size; i < size + 10; i++) {
                UserNode node = new UserNode("Node-" + i, randomLat(), randomLon());
                graph.put(node.getAccountId(), node);
            }
            end = System.nanoTime();
            System.out.println("Insertion Time: " + (end - start) / 1e6 + " ms");

            // Benchmark deletion
            start = System.nanoTime();
            for (int i = 0; i < 10; i++) {
                graph.remove("Node-" + i);
            }
            end = System.nanoTime();
            System.out.println("Deletion Time: " + (end - start) / 1e6 + " ms");
        }
    }

    private static Map<String, UserNode> generateGraph(int size) {
        Map<String, UserNode> graph = new HashMap<>();
        for (int i = 0; i < size; i++) {
            UserNode node = new UserNode("Node-" + i, randomLat(), randomLon());
            graph.put(node.getAccountId(), node);
        }
        return graph;
    }

    private static double randomLat() {
        return -90 + Math.random() * 180;
    }

    private static double randomLon() {
        return -180 + Math.random() * 360;
    }
}

