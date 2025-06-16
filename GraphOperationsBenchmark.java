package benchmark;

import graph.GraphBuilder;
import graph.UserNode;
import models.FraudDataModel;
import repository.FraudDataRepository;
import detectors.BFSDetector;
import java.util.*;
import java.nio.file.*;
import java.util.logging.Logger;
import java.util.logging.Level;

// measures performance of graph operations across different dataset sizes
// tests basic crud operations and complex graph algorithms
public class GraphOperationsBenchmark {
    // logger for error reporting during benchmark execution
    private static final Logger logger = Logger.getLogger(GraphOperationsBenchmark.class.getName());

    // dataset sizes to test in number of records
    // tests small to medium dataset performance
    private static final int[] SIZES = {10, 50, 100, 500, 1000};

    // entry point for benchmark execution
    public static void main(String[] args) {
        // minimal jvm warmup to reduce cold start effects
        // proper benchmarks would include more thorough warmup
        System.out.println("warming up JVM...");
        for (int i = 0; i < 3; i++) {
            new GraphBuilder(Collections.emptyList()).buildGraph(Collections.emptyList());
        }

        // execute benchmarks for each dataset size
        for (int size : SIZES) {
            System.out.println("\n=== dataset: " + size + " records ===");

            try {
                // verify data files exist before processing
                String basePath = "C:\\Users\\Elias\\IdeaProjects\\OOP_DS_FP\\data\\";
                String accountsFile = basePath + "accounts_" + size + ".csv";
                String transactionsFile = basePath + "transactions_" + size + ".csv";

                // skip missing files without failing entire benchmark
                if (!Files.exists(Paths.get(accountsFile))) {
                    System.out.println("skipping size " + size + " - file not found: " + accountsFile);
                    continue;
                }
                if (!Files.exists(Paths.get(transactionsFile))) {
                    System.out.println("skipping size " + size + " - file not found: " + transactionsFile);
                    continue;
                }

                // load test data from files
                FraudDataRepository repo = new FraudDataRepository();
                List<FraudDataModel.Account> accounts = repo.loadAccounts(accountsFile);
                List<FraudDataModel.Transaction> transactions = repo.loadTransactions(transactionsFile);

                // build graph structure from loaded data
                Map<String, UserNode> graph = new GraphBuilder(accounts).buildGraph(transactions);

                // get first node for crud operation tests
                UserNode firstNode = graph.values().iterator().next();

                // benchmark basic graph operations
                benchmarkOperation("add node", () -> graph.put("test_node", firstNode));
                benchmarkOperation("lookup node", () -> graph.get(firstNode.getAccountId()));
                benchmarkOperation("remove node", () -> graph.remove(firstNode.getAccountId()));

                // benchmark complex graph algorithm
                benchmarkOperation("BFS cluster", () -> new BFSDetector(graph).detectFraudClusters());

            } catch (Exception e) {
                // log errors without stopping entire benchmark
                logger.log(Level.SEVERE, "error processing dataset of size " + size, e);
            }
        }
    }

    // measures execution time of a single operation
    // prints results in appropriate time units
    // operation - the code to benchmark
    private static void benchmarkOperation(String name, Runnable operation) {
        // capture precise start time
        long start = System.nanoTime();
        // execute the operation
        operation.run();
        // calculate duration in nanoseconds
        long duration = System.nanoTime() - start;

        // print results in microseconds or milliseconds
        // based on operation type for better readability
        System.out.printf("%s: %.2f %s\n", name,
                name.equals("BFS cluster") ? duration / 1_000_000.0 : duration / 1000.0,
                name.equals("BFS cluster") ? "ms" : "µs");
    }
}