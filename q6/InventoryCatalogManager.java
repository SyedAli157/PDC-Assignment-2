import java.util.*;
import java.util.concurrent.*;

public class InventoryCatalogManager {
    private static final int THREADS = 10;
    private static final int TOTAL_OPS = 50000;
    private static final int PRODUCTS = 1000;

    // runs the workload on the given map using 10 threads
    private static double runWorkload(Map<String, Integer> catalog, String label) throws InterruptedException {
        for (int i = 0; i < PRODUCTS; i++) {
            catalog.put("PROD-" + i, 100);
        }
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        CountDownLatch done = new CountDownLatch(THREADS);
        int opsPerThread = TOTAL_OPS / THREADS;

        long start = System.nanoTime();
        for (int t = 0; t < THREADS; t++) {
            pool.submit(() -> {
                Random r = new Random();
                for (int i = 0; i < opsPerThread; i++) {
                    String id = "PROD-" + r.nextInt(PRODUCTS);
                    Integer stock = catalog.get(id);            // lookup
                    if (stock != null) {
                        catalog.put(id, stock + 1);            // update
                    }
                }
                done.countDown();
            });
        }
        done.await();
        long end = System.nanoTime();
        pool.shutdown();

        double ms = (end - start) / 1e6;
        double throughput = TOTAL_OPS / ((end - start) / 1e9);
        System.out.println("--- " + label + " ---");
        System.out.printf("Threads: %d | Operations: %d%n", THREADS, TOTAL_OPS);
        System.out.printf("Execution time : %.3f ms%n", ms);
        System.out.printf("Throughput     : %.0f operations/second%n", throughput);
        return throughput;
    }

    public static double benchmarkSynchronizedMap() throws InterruptedException {
        Map<String, Integer> map = Collections.synchronizedMap(new HashMap<>());
        return runWorkload(map, "Collections.synchronizedMap(HashMap)");
    }

    public static double benchmarkConcurrentMap() throws InterruptedException {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        return runWorkload(map, "ConcurrentHashMap");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("===== Inventory Catalog Concurrency Benchmark =====");
        System.out.println("Warm-up run (ignored, lets the JVM optimise the code):");
        benchmarkSynchronizedMap();
        System.out.println();
        System.out.println("Real benchmark runs:");
        double sync = benchmarkSynchronizedMap();
        System.out.println();
        double conc = benchmarkConcurrentMap();
        System.out.println();
        System.out.printf("ConcurrentHashMap throughput is %.2f times that of synchronizedMap%n", conc / sync);
    }
}

// Author: Syed Ali Haider (247172)

// Author: Syed Ali Haider (247172)
