package demo;

import detectors.HashTableDetection;
import models.FraudDataModel;
import repository.FraudDataRepository;
import utils.BloomFilter;
import utils.CsvExporter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class HashTableDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n🔢 Welcome to Hash Table Fraud Detection 🔢");
        System.out.println("-------------------------------------------");

        // 1. Get CSV paths from user
        System.out.print("➡ Enter path to Transactions CSV: ");
        String txPath = scanner.nextLine().trim();

        System.out.print("➡ Enter path to Accounts CSV: ");
        String accPath = scanner.nextLine().trim();

        System.out.print("➡ Enter path to Known Fraud Accounts CSV: ");
        String fraudAccountsPath = scanner.nextLine().trim();

        // 2. Load data
        System.out.println("\n🔍 Loading and analyzing data...");
        try {
            FraudDataRepository repo = new FraudDataRepository();
            List<FraudDataModel.Account> accounts = repo.loadAccounts(accPath);
            List<FraudDataModel.Transaction> transactions = repo.loadTransactions(txPath);

            // 3. Set up Bloom filter with known fraud accounts from CSV
            BloomFilter filter = new BloomFilter(1000);
            System.out.println("\n🔍 Loading known fraud accounts...");

            // Load fraud accounts from CSV
            List<String> fraudAccounts = Files.readAllLines(Paths.get(fraudAccountsPath));
            // Skip header by starting at 1 and add each account ID
            for (int i = 1; i < fraudAccounts.size(); i++) {
                String accountId = fraudAccounts.get(i).trim(); //clean up whitespace
                if (!accountId.isEmpty()) {
                    filter.add(accountId);
                }
            }

            // 4. Detect fraud
            System.out.println("\n🛡️ Running hash table detection...");
            List<FraudDataModel.Transaction> frauds = new HashTableDetection(accounts, transactions, filter).detectFraud();

            // 5. Save results
            String outputPath = "hashtable_fraud_results.csv";
            CsvExporter.export(frauds, "HASHTABLE_FRAUDS", outputPath);

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
