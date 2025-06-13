package utils;

import models.FraudDataModel.Account;
import models.FraudDataModel.Transaction;
import repository.FraudDataRepository;

import java.util.List;

public class DataLoader {
    private FraudDataRepository repository = new FraudDataRepository();

    public List<Account> loadAccounts(String path) {
        return repository.loadAccounts(path);
    }

    public List<Transaction> loadTransactions(String path) {
        return repository.loadTransactions(path);
    }
}
