package detectors;

import models.FraudDataModel;
import utils.BloomFilter;
import java.sql.Timestamp;
import java.util.*;

public class HashTableDetection implements FraudDetector {

    private final List<FraudDataModel.Account> accounts;
    private final List<FraudDataModel.Transaction> transactions;
    private final BloomFilter bloomFilter;

    public HashTableDetection(List<FraudDataModel.Account> accounts, List<FraudDataModel.Transaction> transactions, BloomFilter bloomFilter) {
        this.accounts = accounts;
        this.transactions = transactions;
        this.bloomFilter = bloomFilter;
    }

    @Override
    public List<FraudDataModel.Transaction> detectFraud() {
        List<FraudDataModel.Transaction> frauds = new ArrayList<>();
        Map<String, String> accountTypes = new HashMap<>();

        for (FraudDataModel.Account acc : accounts)
            accountTypes.put(acc.getAccountId(), acc.getAccountType());

        Map<String, List<Long>> timeMap = new HashMap<>();

        for (FraudDataModel.Transaction tx : transactions) {
            String fromId = tx.getFromAccountId();
            double amount = tx.getAmount();

            if (bloomFilter.mightContain(fromId)) {
                frauds.add(tx);
                continue;
            }

            double threshold = getThreshold(accountTypes.getOrDefault(fromId, "personal"));
            if (amount > threshold) {
                frauds.add(tx);
                continue;
            }

            timeMap.putIfAbsent(fromId, new ArrayList<>());
            long time = parseTimestamp(tx.getTimestamp());
            List<Long> timestamps = timeMap.get(fromId);
            timestamps.add(time);
            timestamps.removeIf(t -> t < time - 5 * 60 * 1000);
            if (timestamps.size() > 3)
                frauds.add(tx);
        }

        return frauds;
    }

    private double getThreshold(String type) {
        return switch (type) {
            case "personal" -> 10000;
            case "business", "merchant" -> 500000;
            case "prepaid" -> 1000;
            default -> 10000;
        };
    }

    private long parseTimestamp(String timestamp) {
        return Timestamp.valueOf(timestamp.replace("T", " ")).getTime();
    }
}


