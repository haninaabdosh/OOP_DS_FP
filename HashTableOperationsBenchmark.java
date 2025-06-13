package benchmark;

import models.FraudDataModel;
import repository.FraudDataRepository;
import utils.BloomFilter;
import utils.MemoryUtils;

import java.util.*;

public class HashTableOperationsBenchmark {

    public static void main(String[] args) {
        benchmarkHashTableOperations();
        benchmarkBloomFilter();
    }

    private static void benchmarkHashTableOperations() {
        int[] datasetSizes = {10, 50, 100, 500, 1000};

        for (int size : datasetSizes) {
            String basePath = "C:/Users/Elias/IdeaProjects/OOP_DS_FP/data/";
            String accountPath = basePath + "accounts_" + size + ".csv";
            String transactionPath = basePath + "transactions_" + size + ".csv";

            FraudDataRepository repository = new FraudDataRepository();
            List<FraudDataModel.Account> accounts = repository.loadAccounts(accountPath);
            List<FraudDataModel.Transaction> transactions = repository.loadTransactions(transactionPath);

            Map<String, FraudDataModel.Account> accountMap = new HashMap<>();
            for (FraudDataModel.Account acc : accounts) {
                accountMap.put(acc.getAccountId(), acc);
            }

            Map<String, FraudDataModel.Transaction> transactionMap = new HashMap<>();
            for (FraudDataModel.Transaction tr : transactions) {
                transactionMap.put(tr.getTransactionId(), tr);
            }

            long usedMemoryKB = MemoryUtils.getUsedMemoryInKB();

            // Add operation
            String newAccountId = "ACC_NEW_" + size;
            FraudDataModel.Account newAcc = new FraudDataModel.Account(newAccountId, "BankX", "personal", 1000);
            long addStart = System.nanoTime();
            accountMap.put(newAccountId, newAcc);
            long addEnd = System.nanoTime();

            // Search operation
            long searchStart = System.nanoTime();
            accountMap.get(newAccountId);
            long searchEnd = System.nanoTime();

            // Remove operation
            long removeStart = System.nanoTime();
            accountMap.remove(newAccountId);
            long removeEnd = System.nanoTime();

            System.out.printf("HashTable | Size: %d | Mem: %d KB | Add: %.3f µs | Search: %.3f µs | Remove: %.3f µs\n",
                    size, usedMemoryKB,
                    (addEnd - addStart) / 1000.0,
                    (searchEnd - searchStart) / 1000.0,
                    (removeEnd - removeStart) / 1000.0
            );
        }
    }

    private static void benchmarkBloomFilter() {
        System.out.println("\n=== Bloom Filter Benchmark ===");

        // Test with different sizes to show false positive rate changes
        int[] filterSizes = {100, 1000, 10000};
        int testItemCount = 1000;
        int knownNonMembers = 1000; // Items we know aren't in the filter

        for (int size : filterSizes) {
            BloomFilter filter = new BloomFilter(size);
            Random random = new Random();

            // Add random items to filter
            for (int i = 0; i < testItemCount; i++) {
                String item = "item_" + random.nextInt(10000);
                filter.add(item);
            }

            // Test for false positives
            int falsePositives = 0;
            for (int i = 0; i < knownNonMembers; i++) {
                String testItem = "non_member_" + i; // These definitely weren't added
                if (filter.mightContain(testItem)) {
                    falsePositives++;
                }
            }

            double falsePositiveRate = (double) falsePositives / knownNonMembers * 100;
            System.out.printf("BloomFilter | Size: %d | False Positive Rate: %.2f%%\n",
                    size, falsePositiveRate);

            // Benchmark mightContain performance
            String testKey = "item_" + random.nextInt(10000);
            long start = System.nanoTime();
            filter.mightContain(testKey);
            long end = System.nanoTime();
            System.out.printf("BloomFilter | Size: %d | Lookup Time: %.3f µs\n",
                    size, (end - start) / 1000.0);
        }
    }
}