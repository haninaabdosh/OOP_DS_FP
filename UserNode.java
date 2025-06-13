package graph;

import java.util.ArrayList;
import java.util.List;

public class UserNode {
    private String accountId;
    private double latitude;
    private double longitude;
    private List<TransactionEdge> edges;
    private List<UserNode> neighbors;

    public UserNode(String accountId) {
        this(accountId, 0.0, 0.0);  // default lat/lon
    }

    public UserNode(String accountId, double latitude, double longitude) {
        this.accountId = accountId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.edges = new ArrayList<>();
        this.neighbors = new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<TransactionEdge> getEdges() {
        return edges;
    }

    public List<UserNode> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(UserNode neighbor) {
        neighbors.add(neighbor);
    }

    public void addEdge(TransactionEdge edge) {
        edges.add(edge);
    }
}


