package graph;

import models.FraudDataModel;

// represents a connection between two accounts in the fraud graph
// stores the actual transaction data that created this connection
public class TransactionEdge {
    private final FraudDataModel.Transaction transaction;

    // creates a new edge from transaction data
    public TransactionEdge(FraudDataModel.Transaction transaction) {
        this.transaction = transaction;
    }

    // gives access to the original transaction details
    // returns - the complete transaction object stored in this edge
    public FraudDataModel.Transaction getTransaction() {
        return transaction;
    }
}
