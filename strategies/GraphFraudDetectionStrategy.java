package strategies;

import graph.UserNode;
import models.FraudDataModel;

import java.util.List;
import java.util.Map;

// blueprint for different fraud detection approaches
// all strategies must implement the detectFraud method
public interface GraphFraudDetectionStrategy {

    // analyzes transaction graph to find suspicious activity
    // userNodes - graph of accounts and their transactions
    // returns - list of transactions flagged as potentially fraudulent
    List<FraudDataModel.Transaction> detectFraud(Map<String, UserNode> userNodes);
}
