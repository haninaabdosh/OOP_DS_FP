package benchmark;

import models.FraudDataModel;
import repository.FraudDataRepository;
import utils.BloomFilter;
import java.util.*;
import java.nio.file.*;
import java.util.logging.Logger;
import java.util.logging.Level;

// benchmarks performance of hash table and bloom filter operations
// tests different dataset sizes to measure scaling behavior
public class HashTableOperationsBenchmark {
    // logger for recording errors during benchmark execution
    private static final Logger logger = Logger.getLogger(HashTableOperationsBenchmark.class.getName());

    // dataset sizes to test in number of records
    // covers small to medium dataset performance
    private static final int[] SIZES = {10, 50, 100, 500, 1000};

    // main benchmark execution method
    public static void main(String[] args) {
        // test each dataset size sequentially
        for (int size : SIZES) {
            System.out.println("\n=== dataset: " + size + " records ===");

            try {
                // verify data file exists before loading
                String basePath = "C:\\Users\\Elias\\IdeaProjects\\OOP_DS_FP\\data\\";
                String accountsFile = basePath + "accounts_" + size + ".csv";
                if (!Files.exists(Paths.get(accountsFile))) {
                    System.out.println("skipping size " + size + " - file not found: " + accountsFile);
                    continue;
                }

                // load test data from files
                FraudDataRepository repo = new FraudDataRepository();
                List<FraudDataModel.Account> accounts = repo.loadAccounts(accountsFile);
                List<String> knownFraudAccounts = loadKnownFraudAccounts();

                // initialize data structures for benchmarking
                // hash map for account storage
                Map<String, FraudDataModel.Account> accountMap = new HashMap<>();
                accounts.forEach(acc -> accountMap.put(acc.getAccountId(), acc));

                // bloom filter for fast membership testing
                BloomFilter filter = new BloomFilter(1000);
                knownFraudAccounts.forEach(filter::add);

                // execute benchmark tests using first account
                FraudDataModel.Account firstAccount = accounts.getFirst();
                benchmarkAddExisting(accountMap, firstAccount);
                benchmarkLookup(accountMap, firstAccount.getAccountId());
                benchmarkRemove(accountMap, firstAccount.getAccountId());
                benchmarkBloomFilterLookup(filter, knownFraudAccounts.getFirst());

            } catch (Exception e) {
                // log errors without stopping entire benchmark
                logger.log(Level.SEVERE, "error processing dataset of size " + size, e);
            }
        }
    }

    // loads known fraudulent accounts from file
    // skips header row in csv file
    private static List<String> loadKnownFraudAccounts() {
        try {
            String knownAccountsFile = "C:\\Users\\Elias\\IdeaProjects\\OOP_DS_FP\\data\\known_accounts.csv";
            return Files.readAllLines(Paths.get(knownAccountsFile))
                    .subList(1, Files.readAllLines(Paths.get(knownAccountsFile)).size());
        } catch (Exception e) {
            throw new RuntimeException("failed to load known_accounts.csv", e);
        }
    }

    // benchmarks adding an existing entry to hash map
    // measures hash collision handling performance
    private static void benchmarkAddExisting(Map<String, FraudDataModel.Account> map,
                                             FraudDataModel.Account account) {
        long start = System.nanoTime();
        map.put(account.getAccountId(), account);
        System.out.printf("add existing: %.2f µs\n", (System.nanoTime() - start) / 1000.0);
    }

    // benchmarks hash map lookup operation
    // measures time to find existing key
    private static void benchmarkLookup(Map<String, FraudDataModel.Account> map, String accountId) {
        long start = System.nanoTime();
        map.get(accountId);
        System.out.printf("lookup: %.2f µs\n", (System.nanoTime() - start) / 1000.0);
    }

    // benchmarks hash map removal operation
    // measures time to delete existing entry
    private static void benchmarkRemove(Map<String, FraudDataModel.Account> map, String accountId) {
        long start = System.nanoTime();
        map.remove(accountId);
        System.out.printf("remove: %.2f µs\n", (System.nanoTime() - start) / 1000.0);
    }

    // benchmarks bloom filter membership test
    // measures probabilistic lookup speed
    private static void benchmarkBloomFilterLookup(BloomFilter filter, String accountId) {
        long start = System.nanoTime();
        filter.mightContain(accountId);
        System.out.printf("bloom filter lookup: %.2f µs\n", (System.nanoTime() - start) / 1000.0); //nanoseconds to microseconds
    }
}
