package graph;

import models.FraudDataModel;

public class TransactionEdge {
    private FraudDataModel.Transaction transaction;

    public TransactionEdge(FraudDataModel.Transaction transaction) {
        this.transaction = transaction;
    }

    public FraudDataModel.Transaction getTransaction() {
        return transaction;
    }
}

