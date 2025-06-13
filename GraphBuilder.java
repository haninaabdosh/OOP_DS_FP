package graph;

import models.FraudDataModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphBuilder {

    public static Map<String, UserNode> buildGraph(List<FraudDataModel.Transaction> transactions) {
        Map<String, UserNode> userNodes = new HashMap<>();

        for (FraudDataModel.Transaction t : transactions) {
            // Get or create fromNode
            UserNode from = userNodes.computeIfAbsent(t.getFromAccountId(),
                    id -> new UserNode(id));
            // Get or create toNode
            UserNode to = userNodes.computeIfAbsent(t.getToAccountId(),
                    id -> new UserNode(id));

            // Create edge for from -> to
            TransactionEdge edge = new TransactionEdge(t);
            from.addEdge(edge);
            from.addNeighbor(to);
        }
        return userNodes;
    }
}
