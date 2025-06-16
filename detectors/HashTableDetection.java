package detectors;

import models.FraudDataModel;
import utils.BloomFilter;
import java.sql.Timestamp;
import java.util.*;

public class HashTableDetection implements FraudDetector {

    //list of accounts + transactions to analyze
    private final List<FraudDataModel.Account> accounts;
    private final List<FraudDataModel.Transaction> transactions;
    //bloom filter for quick suspicious account checks
    private final BloomFilter bloomFilter;

    // constructor initializing all fields
    public HashTableDetection(List<FraudDataModel.Account> accounts, List<FraudDataModel.Transaction> transactions, BloomFilter bloomFilter) {
        this.accounts = accounts;
        this.transactions = transactions;
        this.bloomFilter = bloomFilter;
    }

    @Override
    public List<FraudDataModel.Transaction> detectFraud() {
        //list to store fraudulent transactions
        List<FraudDataModel.Transaction> frauds = new ArrayList<>();
        //Map to store account types for quick lookup
        Map<String, String> accountTypes = new HashMap<>();

        //stores each account's type in a map for quick lookup
        for (FraudDataModel.Account acc : accounts)
            accountTypes.put(acc.getAccountId(), acc.getAccountType());

        //map tracks transactions timestamps by account
        Map<String, List<Long>> timeMap = new HashMap<>();

        //loop analyzes each transaction tx in the transactions collection
        //each tx is an object of the type FraudDataModel.Transaction
        for (FraudDataModel.Transaction tx : transactions) {
            //this gets the id of the account and stores it as a string in fromId
            String fromId = tx.getFromAccountId();
            //gets transaction amount value for current account
            double amount = tx.getAmount();

            // check bloom filter for known suspicious accounts
            // account might be present; add tx to frauds list
            if (bloomFilter.mightContain(fromId)) {
                frauds.add(tx);
                continue; //continues even if transaction is flagged or not
            }

            // account type checking
            String accountType = accountTypes.get(fromId);
            if (accountType == null) {
                throw new IllegalStateException("Missing account type for: " + fromId);
            }

            //check if amount> threshold for each account type
            double threshold = getThreshold(accountType);
            if (amount > threshold) {
                frauds.add(tx);
                continue;
            }

            // check for rapid transactions >3 in 5 minutes
            timeMap.putIfAbsent(fromId, new ArrayList<>());

            // a map; key is fromID and value= list of long integers representing transaction timestamps
            // checks if present in timeMap; if not inserts.

            long time = parseTimestamp(tx.getTimestamp());
            // method call- it converts timestamp string into a long value

            List<Long> timestamps = timeMap.get(fromId);
            //retrieves list of timestamps linked to current account id
            timestamps.add(time);

            //remove older timestamps
            //stream operation; iterates through the list and removes any timestamp t older than 5 minutes
            timestamps.removeIf(t -> t < time - 5 * 60 * 1000);

            //for remaining timestamps; transactions>3 add to fraud list
            if (timestamps.size() > 3)
                frauds.add(tx);
        }

        return frauds;
    }
// get threshold method; returns max transaction for each account type
    private double getThreshold(String type) {
            if (type == null) { //only executed if type is null
                throw new IllegalArgumentException("Account type cannot be null");
            }
            // the program can create an instance of IllegalArgumentException; gives error message
            return switch (type.toUpperCase()) {  // changed to uppercase comparison for csv files
                case "PERSONAL" -> 10000;
                case "BUSINESS", "MERCHANT" -> 500000;
                case "PREPAID" -> 1000;
                default -> throw new IllegalArgumentException(
                        String.format("Unsupported account type '%s'. Expected: PERSONAL, BUSINESS, MERCHANT, or PREPAID", type)
                );
            };
        }
// converts string timestamp to milliseconds
    private long parseTimestamp(String timestamp) {
        return Timestamp.valueOf(timestamp.replace("T", " ")).getTime();
    }
}


