import graph.GraphBuilder;
import graph.UserNode;
import models.FraudDataModel;
import strategies.HighFrequencyGraphDetector;
import detectors.BFSDetector;
import utils.BloomFilter;
import utils.CsvExporter;
import utils.CsvImporter;

import java.util.*;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, UserNode> userNodes = new HashMap<>();
    private static BloomFilter bloomFilter = new BloomFilter(100000);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nChoose data structure:");
            System.out.println("1. Graph");
            System.out.println("2. Hash Table + Bloom Filter");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> graphMenu();
                case "2" -> hashTableMenu();
                case "0" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void graphMenu() {
        System.out.println("Load transactions from CSV file (full path):");
        String path = scanner.nextLine();
        List<FraudDataModel.Transaction> transactions = CsvImporter.importTransactions(path);
        userNodes = GraphBuilder.buildGraph(transactions);
        System.out.println("Graph loaded with " + userNodes.size() + " users.");

        while (true) {
            System.out.println("\nGraph Operations:");
            System.out.println("1. Add Account");
            System.out.println("2. Remove Account");
            System.out.println("3. Search Account");
            System.out.println("4. Detect Fraud (High Frequency)");
            System.out.println("5. Detect Fraud (BFS Clusters)");
            System.out.println("0. Back to main menu");
            System.out.print("Choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addAccount();
                case "2" -> removeAccount();
                case "3" -> searchAccount();
                case "4" -> detectHighFrequencyFraud();
                case "5" -> detectBFSFraud();
                case "0" -> {
                    System.out.println("Returning to main menu.");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void hashTableMenu() {
        // Stub example, implement similar for hash table + bloom filter
        System.out.println("Hash Table + Bloom Filter feature coming soon.");
    }

    private static void addAccount() {
        System.out.print("Enter Account ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Latitude (double): ");
        double lat = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Longitude (double): ");
        double lon = Double.parseDouble(scanner.nextLine());
        if (userNodes.containsKey(id)) {
            System.out.println("Account already exists.");
            return;
        }
        userNodes.put(id, new UserNode(id, lat, lon));
        System.out.println("Account added.");
    }

    private static void removeAccount() {
        System.out.print("Enter Account ID to remove: ");
        String id = scanner.nextLine();
        if (userNodes.remove(id) != null) {
            System.out.println("Account removed.");
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void searchAccount() {
        System.out.print("Enter Account ID to search: ");
        String id = scanner.nextLine();
        UserNode node = userNodes.get(id);
        if (node != null) {
            System.out.println("Account found: " + node.getAccountId() +
                    " Lat: " + node.getLatitude() + " Lon: " + node.getLongitude());
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void detectHighFrequencyFraud() {
        HighFrequencyGraphDetector detector = new HighFrequencyGraphDetector();
        List<FraudDataModel.Transaction> frauds = detector.detectFraud(userNodes);
        System.out.println("Detected " + frauds.size() + " fraudulent transactions (High Frequency).");
        CsvExporter.export(frauds, "HighFrequency", "high_freq_fraud.csv");
    }

    private static void detectBFSFraud() {
        BFSDetector detector = new BFSDetector(userNodes);
        List<FraudDataModel.Transaction> frauds = detector.detectFraudClusters();
        System.out.println("Detected " + frauds.size() + " fraudulent transactions (BFS Clusters).");
        CsvExporter.export(frauds, "BFSCluster", "bfs_cluster_fraud.csv");
    }
}
