package graph;

import models.FraudDataModel;
import java.util.*;

public class GraphBuilder {
    // stores account ids mapped to their types
    private final Map<String, String> accountTypes;

    // constructor takes a list of accounts to initialize the accountTypes map
    public GraphBuilder(List<FraudDataModel.Account> accounts) {
        // creates a new empty hashmap to store account id -> account type mappings
        this.accountTypes = new HashMap<>();
        // loops through each account in the input list
        for (FraudDataModel.Account acc : accounts) {
            // puts the account id and its type into the map
            accountTypes.put(acc.getAccountId(), acc.getAccountType());
        }
    }

    // builds a graph from transaction data, returns user nodes mapped by account id
    public Map<String, UserNode> buildGraph(List<FraudDataModel.Transaction> transactions) {
        // creates a map to store user nodes where the key is account id
        Map<String, UserNode> userNodes = new HashMap<>();

        // processes each transaction in the input list
        for (FraudDataModel.Transaction t : transactions) {
            // gets the senders account ids from the transaction
            String fromId = t.getFromAccountId();
            // gets the receivers account ids from the transaction
            String toId = t.getToAccountId();

            // gets or creates the sender node if it doesn't exist
            // computeIfAbsent checks if fromId exists in the map:
            // - if yes, returns the existing UserNode
            // - if no, creates new UserNode using the lambda function; replaces if-else
            UserNode from = userNodes.computeIfAbsent(fromId,
                    id -> new UserNode(id, accountTypes.get(id)));

            // same as above but for the receiver node
            UserNode to = userNodes.computeIfAbsent(toId,
                    id -> new UserNode(id, accountTypes.get(id)));

            // creates a new edge representing this transaction
            TransactionEdge edge = new TransactionEdge(t);
            // adds the transaction edge to the sender's edge list
            from.addEdge(edge);
            // adds the receiver as a neighbor to the sender
            from.addNeighbor(to);
        }
        // returns the complete map of all user nodes in the graph
        return userNodes;
    }
}