package strategies;

import graph.TransactionEdge;
import graph.UserNode;
import models.FraudDataModel;
import utils.LocationUtils;
import java.time.*;
import java.time.format.*;
import java.util.*;

// detects physically impossible travel between transaction locations
// compares time between transactions with geographical distance
// flags transactions requiring impossible travel speeds (>1000 km/h)
public class ImpossibleTravelGraphDetector implements GraphFraudDetectionStrategy {

    // matches the exact timestamp format in the csv file
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // analyzes transaction graph for impossible travel patterns
    // userNodes - map of account nodes and their transaction history
    // returns - list of transactions requiring impossible travel speeds
    @Override
    public List<FraudDataModel.Transaction> detectFraud(Map<String, UserNode> userNodes) {
        // stores all detected fraudulent transactions
        List<FraudDataModel.Transaction> frauds = new ArrayList<>();

        // tracks each user's last transaction location and time
        Map<String, LastTransactionInfo> userHistory = new HashMap<>();

        // processes each account in the transaction graph
        for (UserNode node : userNodes.values()) {
            // checks every outgoing transaction from this account
            for (TransactionEdge edge : node.getEdges()) {
                FraudDataModel.Transaction t = edge.getTransaction();

                try {
                    // converts csv timestamp to machine-readable format
                    LocalDateTime transactionTime = LocalDateTime.parse(t.getTimestamp(), TIMESTAMP_FORMATTER);

                    // converts to instant for precise time calculations
                    // uses system timezone for conversion
                    Instant currentTime = transactionTime.atZone(ZoneId.systemDefault()).toInstant();

                    // extracts transaction coordinates
                    double currentLat = t.getFromLatitude();
                    double currentLon = t.getFromLongitude();
                    String userId = t.getFromAccountId();

                    // checks if we have previous transaction data for this user
                    if (userHistory.containsKey(userId)) {
                        LastTransactionInfo last = userHistory.get(userId);

                        // calculates time difference between transactions in milliseconds
                        // then converts to hours (3600000 ms = 1 hour)
                        long millisDiff = currentTime.toEpochMilli() - last.timestamp.toEpochMilli();
                        double hoursDiff = millisDiff / 3600000.0;

                        // only checks transactions in correct chronological order
                        if (hoursDiff > 0) {
                            // calculates distance between locations in kilometers
                            double distance = LocationUtils.haversine(
                                    last.latitude, last.longitude,
                                    currentLat, currentLon
                            );

                            // calculates speed in km/h (distance / time in hours)
                            // flags if speed exceeds 1000 km/h (impossible for normal travel)
                            if (distance / hoursDiff > 1000) {
                                frauds.add(t);
                            }
                        }
                    }

                    // updates the last known transaction for this user
                    userHistory.put(userId,
                            new LastTransactionInfo(currentLat, currentLon, currentTime));

                } catch (DateTimeParseException e) {
                    // skips transactions with malformed timestamps
                    System.err.println("skipping invalid timestamp in transaction: " + t.getTransactionId());
                }
            }
        }
        return frauds;
    }

    // stores location and time of a user's last transaction
    // used to compare with subsequent transactions
    private static class LastTransactionInfo {
        final double latitude;   // last transaction's latitude
        final double longitude; // last transaction's longitude
        final Instant timestamp; // exact time of last transaction

        LastTransactionInfo(double latitude, double longitude, Instant timestamp) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.timestamp = timestamp;
        }
    }
}