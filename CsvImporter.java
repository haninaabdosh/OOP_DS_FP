package utils;

import models.FraudDataModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvImporter {

    public static List<FraudDataModel.Transaction> importTransactions(String filename) {
        List<FraudDataModel.Transaction> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                String fraudType = parts[0];
                String transactionId = parts[1];
                String fromAccount = parts[2];
                String toAccount = parts[3];
                double amount = Double.parseDouble(parts[4]);
                String timestamp = parts[5];

                FraudDataModel.Transaction t = new FraudDataModel.Transaction(transactionId, fromAccount, toAccount, amount, timestamp);
                transactions.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
}

