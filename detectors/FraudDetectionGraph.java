package detectors;

import graph.UserNode;
import models.FraudDataModel;
import strategies.GraphFraudDetectionStrategy;
import java.util.*;

public class FraudDetectionGraph {

    //list of all transactions
    private final List<FraudDataModel.Transaction> transactions;
    //graph representation of users + their transactions
    private final Map<String, UserNode> userGraph;
    //strategy pattern for fraud detection
    private final GraphFraudDetectionStrategy strategy;

    //constructor
    public FraudDetectionGraph(List<FraudDataModel.Transaction> transactions, Map<String, UserNode> userGraph, GraphFraudDetectionStrategy strategy) {
        this.transactions = transactions;
        this.userGraph = userGraph;
        this.strategy = strategy;
    }

    //passes the fraud detection to the strategy
    public List<FraudDataModel.Transaction> detectFraud() {
        return strategy.detectFraud(userGraph);
    }
}

