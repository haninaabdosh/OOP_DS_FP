package models;

// this file contains all DS definitions for the fraud detection system
public class FraudDataModel {
    // this represents bank account information
    public static class Account {
        private final String accountId;
        private final String bank;
        private final String accountType;
        private final double balance;

        // Constructors for Account class
        public Account(String accountId, String bank, String accountType, double balance) {
            this.accountId = accountId;
            this.bank = bank;
            this.accountType = accountType;
            this.balance = balance;
        }

        // getters method for account properties
        public String getAccountId() {
            return accountId;
        }

        public String getBank() {
            return bank;
        }

        public String getAccountType() {
            return accountType;
        }

        public double getBalance() {
            return balance;
        }
    }

    // represents transactions b/n accounts
    public static class Transaction {
        private final String transactionId;
        private final String fromAccountId;
        private final String toAccountId;
        private final double amount;
        private final String timestamp;
        private final double fromLatitude;
        private final double fromLongitude;
        private final double toLatitude;
        private final double toLongitude;

        // Constructor for Transaction class
        public Transaction(String transactionId, String fromAccountId, String toAccountId,
                           double amount, String timestamp,
                           double fromLatitude, double fromLongitude,
                           double toLatitude, double toLongitude) {
            this.transactionId = transactionId;
            this.fromAccountId = fromAccountId;
            this.toAccountId = toAccountId;
            this.amount = amount;
            this.timestamp = timestamp;
            this.fromLatitude = fromLatitude;
            this.fromLongitude = fromLongitude;
            this.toLatitude = toLatitude;
            this.toLongitude = toLongitude;
        }

        // getters method for transaction properties
        public String getTransactionId() {
            return transactionId;
        }

        public String getFromAccountId() {
            return fromAccountId;
        }

        public String getToAccountId() {
            return toAccountId;
        }

        public double getAmount() {
            return amount;
        }

        public String getTimestamp() {
            return timestamp;
        }

        // getters for coordinates
        public double getFromLatitude() {
            return fromLatitude;
        }

        public double getFromLongitude() {
            return fromLongitude;
        }

        public double getToLatitude() {
            return toLatitude;
        }

        public double getToLongitude() {
            return toLongitude;
        }
    }
}
