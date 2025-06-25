# OOP_DS_FP
Poster link: https://www.canva.com/design/DAGqIAeHHe4/GwseKssfXjBmNfuWyBF7vg/edit?ui=eyJEIjp7IlAiOnsiQiI6ZmFsc2V9fX0  

Final Report link:

# Fraud Detection System

A comprehensive Java-based fraud detection system that uses graph algorithms and hash table operations to identify suspicious financial transactions.

## System Architecture

The system implements two main detection approaches:
- **Graph-based Detection**: Uses BFS clustering, impossible travel detection, and high-frequency detection.
- **Hash Table Detection**: Uses bloom filters and threshold-based analysis for fast lookups.

## Project Structure

src/
├── benchmark/                 # Performance testing  
│   ├── GraphOperationsBenchmark.java
│   └── HashTableOperationsBenchmark.java
├── demo/                      # demos
│   ├── GraphDemo.java
│   └── HashTableDemo.java
├── detectors/                 # Core fraud detection logic
│   ├── BFSDetector.java
│   ├── FraudDetectionGraph.java
│   ├── FraudDetector.java
│   └── HashTableDetection.java
├── graph/                     # Graph data structures
│   ├── GraphBuilder.java
│   ├── TransactionEdge.java
│   └── UserNode.java
├── models/                    # Data models
│   └── FraudDataModel.java
├── repository/                # Data loading
│   └── FraudDataRepository.java
├── strategies/                # Detection strategies
│   ├── GraphFraudDetectionStrategy.java
│   ├── HighFrequencyGraphDetector.java
│   └── ImpossibleTravelGraphDetector.java
└── utils/                     # Utility classes
    ├── BloomFilter.java
    ├── CsvExporter.java
    └── LocationUtils.java

data/
├── accounts_10.csv           # Sample account data (10 records)
├── accounts_50.csv           # Sample account data (50 records)
├── accounts_100.csv          # Sample account data (100 records)
├── accounts_500.csv          # Sample account data (500 records)
├──accounts_1000.csv          # Sample account data (1000 records)
├── transactions_10.csv       # Sample transaction data (10 records)
├── transactions_50.csv       # Sample transaction data (50 records)
├── transactions_100.csv      # Sample transaction data (100 records)
├── transactions_500.csv      # Sample transaction data (500 records)
├── transactions_1000.csv     # Sample transaction data (1000 records)
└── known_accounts.csv        # Known fraudulent accounts
```

### Technology needed
- Java 17 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code)
- CSV data files in the correct format

### Data Format Requirements

#### Accounts CSV Format
```csv
accountId,bank,accountType,balance,latitude,longitude
acc001,BankA,PERSONAL,5000,40.71,-74.01
```

#### Transactions CSV Format
```csv
transactionId,fromAccountId,toAccountId,amount,timestamp,fromLatitude,fromLongitude,toLatitude,toLongitude
tx001,acc004,acc002,12000,2025-06-15T10:00:00,51.51,-0.13,34.05,-118.24
```

#### Known Fraud Accounts CSV Format
```csv
accountId
acc816
acc073
```

### Running the System

#### Option 1: Interactive Graph Demo
```bash
java demo.GraphDemo
```
- Prompts for CSV file paths
- Runs all graph-based detectors
- Exports results to `graph_fraud_results.csv`

#### Option 2: Interactive Hash Table Demo
```bash
java demo.HashTableDemo
```
- Prompts for CSV file paths and known fraud accounts
- Uses bloom filter for fast lookups
- Exports results to `hashtable_fraud_results.csv`

#### Option 3: Performance Benchmarking
```bash
# Test graph operations performance
java benchmark.GraphOperationsBenchmark

# Test hash table operations performance
java benchmark.HashTableOperationsBenchmark
```

## Detection Methods

### 1. Graph-Based Detection

#### BFS Cluster Detection (`BFSDetector.java`)
- **Purpose**: Identifies groups of connected accounts with suspicious patterns
- **Algorithm**: Breadth-First Search to find transaction clusters
- **Criteria**: 
  - Minimum 3 accounts in cluster
  - Average >3 transactions per account OR total cluster amount >$50,000
- **Use Case**: Detecting money laundering networks

#### Impossible Travel Detection (`ImpossibleTravelGraphDetector.java`)
- **Purpose**: Flags transactions requiring impossible travel speeds
- **Algorithm**: Calculates distance vs. time between consecutive transactions
- **Threshold**: >1000 km/h travel speed
- **Formula**: Uses Haversine formula for geographical distance calculation
- **Use Case**: Detecting card cloning or account takeover

#### High Frequency Detection (`HighFrequencyGraphDetector.java`)
- **Purpose**: Identifies rapid successive transactions and amount thresholds
- **Criteria**:
  - Personal accounts: >$10,000 per transaction
  - Business/Merchant accounts: >$500,000 per transaction  
  - Prepaid accounts: >$1,000 per transaction
  - >3 transactions within 5 minutes from same account
- **Use Case**: Detecting automated fraud scripts or suspicious spending

### 2. Hash Table Detection

#### Bloom Filter Lookup (`HashTableDetection.java`)
- **Purpose**: Fast membership testing for known fraudulent accounts
- **Algorithm**: Probabilistic data structure with two hash functions
- **Advantage**: O(1) lookup time, memory efficient
- **Note**: May have false positives, no false negatives
- **Use Case**: Real-time screening against blacklists

#### Threshold Analysis
- Same amount thresholds as graph-based detection
- Additional rapid transaction detection (>3 in 5 minutes)

## Patterns

### Strategy Pattern
- `GraphFraudDetectionStrategy` interface allows pluggable detection algorithms
- Easy to add new detection methods without modifying existing code

### Repository Pattern  
- `FraudDataRepository` handles all CSV data loading
- Separates data access from business logic

### Builder Pattern
- `GraphBuilder` constructs user node graphs from transaction data
- Creates bidirectional relationships between accounts

## Key Components

### Data Structures

#### UserNode (`graph/UserNode.java`)
- Represents bank accounts in the transaction graph
- Contains account ID, type, transaction edges, and neighbor connections
- Supports adding edges and neighbors dynamically

#### TransactionEdge (`graph/TransactionEdge.java`)
- Represents connections between accounts
- Stores complete transaction information
- Used for graph traversal and analysis

#### BloomFilter (`utils/BloomFilter.java`)
- Probabilistic set membership testing
- Uses two hash functions to reduce false positive rate
- Configurable size for different datasets

### Utilities

#### LocationUtils (`utils/LocationUtils.java`)
- Implements Haversine formula for distance calculation
- Converts latitude/longitude to kilometers
- Essential for impossible travel detection

#### CsvExporter (`utils/CsvExporter.java`)
- Exports fraud detection results to CSV format
- Includes fraud type classification
- Standardized output format for analysis

## 📊 Performance Benchmarking

The system includes comprehensive benchmarks for:

### Graph Operations
- Node addition/removal: ~1-5 µs
- Node lookup: ~0.5-2 µs  
- BFS cluster detection: ~1-50 ms (varies by dataset size)

### Hash Table Operations
- Hash map operations: ~0.5-3 µs
- Bloom filter lookup: ~0.1-1 µs
- Scales well with dataset size

## 🎯 Usage Examples

### Example 1: Detecting High-Value Suspicious Clusters
```java
// Load data
FraudDataRepository repo = new FraudDataRepository();
List<Account> accounts = repo.loadAccounts("accounts.csv");
List<Transaction> transactions = repo.loadTransactions("transactions.csv");

// Build graph and detect clusters  
Map<String, UserNode> graph = new GraphBuilder(accounts).buildGraph(transactions);
List<Transaction> frauds = new BFSDetector(graph).detectFraudClusters();
```

## Important Notes

### File Paths
- Update file paths in benchmark classes to match your system
- Current file paths are Windows-specific

### Account Types
- System recognizes: PERSONAL, BUSINESS, MERCHANT, PREPAID
- Case-insensitive matching
- Unknown types will throw exceptions

### Timestamp Format
- Must use ISO format: `2025-06-15T10:00:00`
- System timezone is used for impossible travel calculations

### Memory Considerations
- Graph-based detection loads entire dataset into memory
- Hash table detection is more memory efficient
- Bloom filter size should be ~10x expected number of fraud accounts

## Troubleshooting

### Common Issues

1. **File Not Found Errors**
   - Verify CSV file paths are correct
   - Ensure files exist and are readable
   - Check file permissions

2. **Invalid Account Type Errors**
   - Verify account types match expected values
   - Check for extra whitespace in CSV data

3. **Timestamp Parsing Errors**
   - Ensure timestamps follow ISO format
   - Check for malformed date/time values

4. **Memory Issues with Large Datasets**
   - Increase JVM heap size: `-Xmx4g`
   - Consider processing data in chunks

## 🎯 System Benefits

- **Scalable**: Handles datasets from 10 to 1000+ records efficiently
- **Modular**: Easy to add new detection methods
- **Fast**: Hash table operations in microseconds, graph analysis in milliseconds
- **Comprehensive**: Multiple detection approaches catch different fraud types
- **Exportable**: Results saved in standard CSV format for further analysis

## 📝 Output Format

Results are exported with the following structure:
```csv
FraudType,TransactionID,FromAccountID,ToAccountID,Amount,Timestamp
GRAPH_FRAUDS,tx001,acc004,acc002,12000,2025-06-15T10:00:00
HASHTABLE_FRAUDS,tx002,acc005,acc006,2000,2025-06-15T09:00:00
```

This enables easy integration with external analysis tools and reporting systems.
