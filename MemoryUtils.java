package utils;

public class MemoryUtils {

    public static long getUsedMemoryInKB() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();  // Garbage collection before measuring
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024;
    }
}

