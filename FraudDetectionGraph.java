package detectors;

import graph.UserNode;
import models.FraudDataModel;
import strategies.GraphFraudDetectionStrategy;
import java.util.*;

public class FraudDetectionGraph {

    private final List<FraudDataModel.Transaction> transactions;
    private final Map<String, UserNode> userGraph;
    private final GraphFraudDetectionStrategy strategy;

    public FraudDetectionGraph(List<FraudDataModel.Transaction> transactions, Map<String, UserNode> userGraph, GraphFraudDetectionStrategy strategy) {
        this.transactions = transactions;
        this.userGraph = userGraph;
        this.strategy = strategy;
    }

    public List<FraudDataModel.Transaction> detectFraud() {
        return strategy.detectFraud(userGraph);
    }
}

