package strategies;

import graph.UserNode;
import models.FraudDataModel;

import java.util.List;
import java.util.Map;

public interface GraphFraudDetectionStrategy {
    List<FraudDataModel.Transaction> detectFraud(Map<String, UserNode> userNodes);
}

