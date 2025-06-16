package graph;

import java.util.ArrayList;
import java.util.List;

// represents an account in the fraud detection graph
// holds connections to other accounts via transactions
public class UserNode {

    private final String accountId;


    private final String accountType;

    // all outgoing transactions from this account
    private final List<TransactionEdge> edges;

    // all connected accounts (transaction recipients)
    private final List<UserNode> neighbors;

    // creates a new account node
    // accountId - cannot be empty/null
    public UserNode(String accountId, String accountType) {
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("Account ID is required");
        }
        this.accountId = accountId;
        this.accountType = accountType;
        this.edges = new ArrayList<>();
        this.neighbors = new ArrayList<>();
    }

    // returns the account identifier
    public String getAccountId() { return accountId; }

    // returns the account types
    public String getAccountType() { return accountType; }

    // returns copy of all outgoing transactions
    public List<TransactionEdge> getEdges() { return new ArrayList<>(edges); }

    // returns copy of all connected accounts
    public List<UserNode> getNeighbors() { return new ArrayList<>(neighbors); }

    // links to another account (transaction recipient)
    // neighbor - the receiving account node
    public void addNeighbor(UserNode neighbor) {
        if (neighbor != null) neighbors.add(neighbor);
    }

    // records a new outgoing transaction
    // edge - the transaction details
    public void addEdge(TransactionEdge edge) {
        if (edge != null) edges.add(edge);
    }
}
