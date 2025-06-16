package utils;

import java.util.BitSet;

// probabilistic set with possible false positives
public class BloomFilter {
    // bit array to track inserted elements
    private final BitSet bitset;
    // size of the bit array
    private final int size;

    // creates new filter with given bit array size
    public BloomFilter(int size) {
        this.size = size;
        this.bitset = new BitSet(size);
    }

    // first hash function
    private int hash1(String key) {
        return Math.abs(key.hashCode()) % size;
    }

    // second hash function (uses prime multiplier for better distribution)
    // 31 helps spread bits differently than hash1
    private int hash2(String key) {
        return Math.abs((key.hashCode() * 31)) % size;
    }

    // adds key by setting bits at both hash positions
    public void add(String key) {
        bitset.set(hash1(key));
        bitset.set(hash2(key));
    }

    // checks if key might be in set
    // two hashes reduce chance of false positives
    public boolean mightContain(String key) {
        return bitset.get(hash1(key)) && bitset.get(hash2(key));
    }
}