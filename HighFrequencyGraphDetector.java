package strategies;

import graph.TransactionEdge;
import graph.UserNode;
import models.FraudDataModel;

import java.sql.Timestamp;
import java.util.*;

public class HighFrequencyGraphDetector implements GraphFraudDetectionStrategy {

    @Override
    public List<FraudDataModel.Transaction> detectFraud(Map<String, UserNode> userNodes) {
        List<FraudDataModel.Transaction> frauds = new ArrayList<>();

        for (UserNode node : userNodes.values()) {
            List<Long> timestamps = new ArrayList<>();
            for (TransactionEdge edge : node.getEdges()) {
                FraudDataModel.Transaction transaction = edge.getTransaction();
                double amount = transaction.getAmount();
                String accountType = "personal"; // default

                double threshold = getThreshold(accountType);
                if (amount > threshold) {
                    frauds.add(transaction);
                    continue;
                }

                long time = parseTimestamp(transaction.getTimestamp());
                timestamps.add(time);
                timestamps.removeIf(t -> t < time - 5 * 60 * 1000);
                if (timestamps.size() > 3) {
                    frauds.add(transaction);
                }
            }
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

