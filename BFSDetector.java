package detectors;

import graph.UserNode;
import models.FraudDataModel;
import java.util.*;

public class BFSDetector {
    private final Map<String, UserNode> userNodes;

    public BFSDetector(Map<String, UserNode> userNodes) {
        this.userNodes = new HashMap<>(userNodes);
    }

    public List<FraudDataModel.Transaction> detectFraudClusters() {
        List<FraudDataModel.Transaction> frauds = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for (UserNode node : userNodes.values()) {
            if (!visited.contains(node.getAccountId())) {
                List<UserNode> cluster = performBFS(node, visited);
                if (isSuspiciousCluster(cluster)) {
                    addClusterTransactions(cluster, frauds);
                }
            }
        }
        return frauds;
    }

    private List<UserNode> performBFS(UserNode startNode, Set<String> visited) {
        List<UserNode> cluster = new ArrayList<>();
        Queue<UserNode> queue = new LinkedList<>();

        queue.add(startNode);
        visited.add(startNode.getAccountId());

        while (!queue.isEmpty()) {
            UserNode current = queue.poll();
            cluster.add(current);

            for (UserNode neighbor : current.getNeighbors()) {
                if (!visited.contains(neighbor.getAccountId())) {
                    visited.add(neighbor.getAccountId());
                    queue.add(neighbor);
                }
            }
        }
        return cluster;
    }

    private boolean isSuspiciousCluster(List<UserNode> cluster) {
        if (cluster.size() < 3) return false; // Minimum cluster size

        double avgTransactions = cluster.stream()
                .mapToInt(node -> node.getEdges().size())
                .average()
                .orElse(0);

        double totalAmount = cluster.stream()
                .flatMap(node -> node.getEdges().stream())
                .mapToDouble(edge -> edge.getTransaction().getAmount())
                .sum();

        return avgTransactions > 2 || totalAmount > 50000;
    }

    private void addClusterTransactions(List<UserNode> cluster,
                                        List<FraudDataModel.Transaction> frauds) {
        Set<String> addedIds = new HashSet<>();
        cluster.forEach(node -> {
            node.getEdges().forEach(edge -> {
                String transId = edge.getTransaction().getTransactionId();
                if (!addedIds.contains(transId)) {
                    frauds.add(edge.getTransaction());
                    addedIds.add(transId);
                }
            });
        });
    }
}


