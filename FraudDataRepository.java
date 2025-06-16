package repository;

import models.FraudDataModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

// handles loading data from CSV files
public class FraudDataRepository {

    // loads account data from specified CSV file
    // returns - list of parsed account objects
    // throws - exception if file reading fails
    public List<FraudDataModel.Account> loadAccounts(String path) throws Exception {
        List<FraudDataModel.Account> accounts = new ArrayList<>(); //initialize empty list to store account objects
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine(); // skip header row
            String line;
            while ((line = reader.readLine()) != null) {//no more lines to read;returns null so while loop executes as long as there is data to read
                String[] data = line.split(",");
                accounts.add(new FraudDataModel.Account(
                        data[0].trim(),  // accountId
                        data[1].trim(),  // accountType
                        data[2].trim(),
                        Double.parseDouble(data[3].trim())  // initialBalance; // convert string balance to numeric value
                ));
            }
        }
        return accounts;
    }

    // loads transaction data from specified CSV file
    // returns - list of parsed transaction objects
    // throws - exception if file reading fails
    public List<FraudDataModel.Transaction> loadTransactions(String path) throws Exception {
        List<FraudDataModel.Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine(); // skip header row
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                transactions.add(new FraudDataModel.Transaction(
                        data[0],  // transactionId
                        data[1],  // fromAccountId
                        data[2],  // toAccountId
                        Double.parseDouble(data[3]),  // amount
                        data[4],  // timestamp
                        Double.parseDouble(data[5]),
                        Double.parseDouble(data[6]),
                        Double.parseDouble(data[7]),
                        Double.parseDouble(data[8])
                ));
            }
        }
        return transactions;
    }
}