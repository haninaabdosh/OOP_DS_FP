package detectors;

import graph.UserNode;
import models.FraudDataModel;
import java.util.*;

//stores user nodes with accountId as key
public class BFSDetector {
    private final Map<String, UserNode> userNodes;

//constructor that initializes the user nodes map
//this creates a new HashMap with the same mappings as the input map
    public BFSDetector(Map<String, UserNode> userNodes) {
        this.userNodes = new HashMap<>(userNodes);
    }
//this detects fraud clusters using BFS
// the list "frauds" stores fraudulent transactions
// the set keeps track of visited nodes
    public List<FraudDataModel.Transaction> detectFraudClusters() {
        List<FraudDataModel.Transaction> frauds = new ArrayList<>();
        Set<String> visited = new HashSet<>();

// the loop iterates through all user nodes in the collection
        for (UserNode node : userNodes.values()) {
            //this checks if the current user node has been visited
            if (!visited.contains(node.getAccountId())) {
                //if not visited; perform BFS-> find connected user nodes forming a cluster + mark visited
                List<UserNode> cluster = performBFS(node, visited);
                // if cluster is suspicious; add all transactions associated with the users in the cluster to "frauds" list
                if (isSuspiciousCluster(cluster)) {
                    addClusterTransactions(cluster, frauds);
                }
            }
        }
        return frauds; // this returns the list f all fraudulent transactions found
    }

//this performs BFS from given node; the list stores nodes in the current cluster
    private List<UserNode> performBFS(UserNode startNode, Set<String> visited) {
        List<UserNode> cluster = new ArrayList<>();
        Queue<UserNode> queue = new LinkedList<>();
// the queue manages the order in which the nodes are visited; level by level.
        // it starts with the initial node
        queue.add(startNode);
        visited.add(startNode.getAccountId());
// this loop allows BFS to continue until all connected nodes are visited
        while (!queue.isEmpty()) {
            // get next node from queue
            UserNode current = queue.poll();
            cluster.add(current); //add to current cluster

            //visit all neighbours
            // loop iterates through all direct neighbours of current node
            for (UserNode neighbor : current.getNeighbors()) {
                //checks if already visited
                if (!visited.contains(neighbor.getAccountId())) {
                    //if not visited yet; add accountId to visited set and mark
                    visited.add(neighbor.getAccountId());
                    queue.add(neighbor); //if not yet visited; add to end of queue
                }
            }
        }
        return cluster;
    }
// determines if a cluster is suspicious based on size + transaction patterns
    private boolean isSuspiciousCluster(List<UserNode> cluster) {
        if (cluster.size() < 3) return false;
        // Minimum cluster size; less than 3= not suspicious

// this calculates avg number of transactions per node in a cluster
        double avgTransactions = cluster.stream()// this creates a stream of UserNode objects from the cluster list
                .mapToInt(node -> node.getEdges().size())
                //each UserNode in the stream transformed into int= no of edges/transactions linked to the node
                // this is counted
                .average()
                .orElse(0); //if cluster is empty; defaults avgTransactions=0

        // total sum of transactions in a cluster
        double totalAmount = cluster.stream()// creates stream of UserNode objects
                // for each UserNode; node.getEdges returns a collection of Edge objects
                // flatMap flattens the collection from each user into a single continuous stream across a cluster
                .flatMap(node -> node.getEdges().stream())
                //extracts transactions linked the edge and gets total amount of transactions
                //sum is in double format
                .mapToDouble(edge -> edge.getTransaction().getAmount())
                .sum();
// cluster is suspicious if either conditions are met
        return avgTransactions > 3 || totalAmount > 50000;
    }
// adds transactions from a suspicious cluster to a fraud list
    private void addClusterTransactions(List<UserNode> cluster,
                                        List<FraudDataModel.Transaction> frauds) {

        // a set that tracks already added transactions
        Set<String> addedIds = new HashSet<>();
        //iterate through all edges of each node
        cluster.forEach(node -> {
            node.getEdges().forEach(edge -> {
                String transId = edge.getTransaction().getTransactionId();
               //add transaction if not already added
                if (!addedIds.contains(transId)) {
                    frauds.add(edge.getTransaction());
                    addedIds.add(transId);
                }
            });
        });
    }
}


