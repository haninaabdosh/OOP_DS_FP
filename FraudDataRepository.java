package repository;

import models.FraudDataModel;
import java.io.*;
import java.util.*;

public class FraudDataRepository {

    public List<FraudDataModel.Account> loadAccounts(String filename) {
        List<FraudDataModel.Account> accounts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String accountId = data[0];
                String bankName = data[1];
                String accountType = data[2].toLowerCase();
                double balance = Double.parseDouble(data[3]);

                accounts.add(new FraudDataModel.Account(accountId, bankName, accountType, balance));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public List<FraudDataModel.Transaction> loadTransactions(String filename) {
        List<FraudDataModel.Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String transactionId = data[0];
                String fromAccountId = data[1];
                String toAccountId = data[2];
                double amount = Double.parseDouble(data[3]);
                String timestamp = data[4];

                transactions.add(new FraudDataModel.Transaction(transactionId, fromAccountId, toAccountId, amount, timestamp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
}

