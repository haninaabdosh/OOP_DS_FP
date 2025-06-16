package demo;

import detectors.BFSDetector;
import graph.GraphBuilder;
import graph.UserNode;
import models.FraudDataModel;
import repository.FraudDataRepository;
import strategies.HighFrequencyGraphDetector;
import strategies.ImpossibleTravelGraphDetector;
import utils.CsvExporter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GraphDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n🌐 Welcome to Graph Fraud Detection System 🌐");
        System.out.println("---------------------------------------------");

        // 1. Get CSV paths from user
        System.out.print("➡ Enter path to Transactions CSV: ");
        String txPath = scanner.nextLine().trim();

        System.out.print("➡ Enter path to Accounts CSV: ");
        String accPath = scanner.nextLine().trim();

        // 2. Load data
        System.out.println("\n🔍 Loading and analyzing data...");
        try {
            FraudDataRepository repo = new FraudDataRepository();
            List<FraudDataModel.Transaction> transactions = repo.loadTransactions(txPath);
            List<FraudDataModel.Account> accounts = repo.loadAccounts(accPath);

            // 3. Build graph and detect fraud
            Map<String, UserNode> graph = new GraphBuilder(accounts).buildGraph(transactions);

            System.out.println("\n🛡️ Running detectors:");
            System.out.println("- BFS Cluster Detection");
            System.out.println("- Impossible Travel Detection");
            System.out.println("- High Frequency Detection");

            List<FraudDataModel.Transaction> frauds = new ArrayList<>();
            frauds.addAll(new BFSDetector(graph).detectFraudClusters());
            frauds.addAll(new ImpossibleTravelGraphDetector().detectFraud(graph));
            frauds.addAll(new HighFrequencyGraphDetector().detectFraud(graph));

            // 4. Save results
            String outputPath = "graph_fraud_results.csv";
            CsvExporter.export(frauds, "GRAPH_FRAUDS", outputPath);

            System.out.println("\n✅ Success! Results saved to: " + outputPath);
            System.out.println("⚠ Found " + frauds.size() + " suspicious transactions");

        } catch (Exception e) {
            System.out.println("\n❌ Error: " + e.getMessage());
            System.out.println("Please check your CSV files and try again.");
        } finally {
            scanner.close();
        }
    }
}
