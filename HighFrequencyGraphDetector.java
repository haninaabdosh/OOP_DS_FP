package strategies;

import graph.TransactionEdge;
import graph.UserNode;
import models.FraudDataModel;
import java.sql.Timestamp;
import java.util.*;

//detects fraud based on transaction frequency and amount thresholds
public class HighFrequencyGraphDetector implements GraphFraudDetectionStrategy {

    @Override
    //returns a list of Transaction objects and takes a map of UserNode objects as input
    public List<FraudDataModel.Transaction> detectFraud(Map<String, UserNode> userNodes) {
        //creates an empty list called frauds to store fraudulent transactions
        List<FraudDataModel.Transaction> frauds = new ArrayList<>();

        //starts a loop that iterates through each UserNode in the map
        for (UserNode node : userNodes.values()) {
            //creates a list called timestamps for each UserNode to track timestamps of each transaction
            List<Long> timestamps = new ArrayList<>();

            //starts a loop that goes through each edge connected to current node
            for (TransactionEdge edge : node.getEdges()) {
                //gets actual transaction object linked to current edge and store it in transaction variable
                FraudDataModel.Transaction transaction = edge.getTransaction();
                //gets amount of current transaction and stores it in double variable
                double amount = transaction.getAmount();
                //gets account type from UserNode (matches updated UserNode class)
                String accountType = node.getAccountType();

                //checks if transaction amount exceeds threshold for account type
                double threshold = getThreshold(accountType);
                if (amount > threshold) {
                    frauds.add(transaction);
                    continue;
                }

                //converts timestamp to milliseconds and checks frequency
                long time = parseTimestamp(transaction.getTimestamp());
                timestamps.add(time);
                timestamps.removeIf(t -> t < time - 5 * 60 * 1000);
                if (timestamps.size() > 3) {//more than 3 transactions in 5 minutes= fraud
                    frauds.add(transaction);
                }
            }
        }
        return frauds;
    }

    //returns threshold amount based on account type
    private double getThreshold(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
        return switch (type.toUpperCase()) {  // changed to uppercase comparison for csv
            case "PERSONAL" -> 10000;
            case "BUSINESS", "MERCHANT" -> 500000;
            case "PREPAID" -> 1000;
            default -> throw new IllegalArgumentException("Unknown account type: " + type);
        };
    }

    //converts timestamp string to milliseconds
    private long parseTimestamp(String timestamp) {
        return Timestamp.valueOf(timestamp.replace("T", " ")).getTime();
    }
}