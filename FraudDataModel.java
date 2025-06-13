package models;

import java.util.List;

public class FraudDataModel {

    public static class Account {
        private final String accountId;
        private final String bank;
        private final String accountType;
        private final double balance;

        public Account(String accountId, String bank, String accountType, double balance) {
            this.accountId = accountId;
            this.bank = bank;
            this.accountType = accountType;
            this.balance = balance;
        }

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

    public static class Transaction {
        private final String transactionId;
        private final String fromAccountId;
        private final String toAccountId;
        private final double amount;
        private final String timestamp;

        public Transaction(String transactionId, String fromAccountId, String toAccountId,
                           double amount, String timestamp) {
            this.transactionId = transactionId;
            this.fromAccountId = fromAccountId;
            this.toAccountId = toAccountId;
            this.amount = amount;
            this.timestamp = timestamp;
        }

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
    }

    public static class FraudDataset {
        private final List<Transaction> transactions;

        public FraudDataset(List<Transaction> transactions) {
            this.transactions = transactions;
        }

        public List<Transaction> getTransactions() {
            return transactions;
        }
    }
}