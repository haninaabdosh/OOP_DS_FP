package utils;

import java.util.BitSet;

public class BloomFilter {
    private BitSet bitset;
    private int size;

    public BloomFilter(int size) {
        this.size = size;
        this.bitset = new BitSet(size);
    }

    private int hash1(String key) {
        return Math.abs(key.hashCode()) % size;
    }

    private int hash2(String key) {
        return Math.abs((key.hashCode() * 31)) % size;
    }

    public void add(String key) {
        bitset.set(hash1(key));
        bitset.set(hash2(key));
    }

    public boolean mightContain(String key) {
        return bitset.get(hash1(key)) && bitset.get(hash2(key));
    }
}


