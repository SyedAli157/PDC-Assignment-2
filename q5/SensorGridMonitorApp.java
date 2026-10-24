import java.util.Random;

public class SensorGridMonitorApp {
    public static void main(String[] args) throws InterruptedException {
        int size = 50000;
        int workers = 4;
        double[] data = new double[size];
        Random rand = new Random(7);
        for (int i = 0; i < size; i++) {
            data[i] = 20 + rand.nextDouble() * 80; // readings between 20 and 100
        }
        System.out.println("Sensor readings : " + size + " | Worker threads per search : " + workers);

        final ParallelMinSearch minSearch = new ParallelMinSearch(data, 0, size, workers);
        final ParallelMaxSearch maxSearch = new ParallelMaxSearch(data, 0, size, workers);
        final double[] result = new double[2];
        final long[] time = new long[2];

        // run the min and max reductions at the same time
        Thread minRunner = new Thread(() -> {
            try {
                long t = System.nanoTime();
                result[0] = minSearch.processGrid();
                time[0] = System.nanoTime() - t;
            } catch (InterruptedException e) { }
        }, "MinRunner");
        Thread maxRunner = new Thread(() -> {
            try {
                long t = System.nanoTime();
                result[1] = maxSearch.processGrid();
                time[1] = System.nanoTime() - t;
            } catch (InterruptedException e) { }
        }, "MaxRunner");

        long begin = System.nanoTime();
        minRunner.start();
        maxRunner.start();
        minRunner.join();
        maxRunner.join();
        long total = System.nanoTime() - begin;

        // verify with a plain loop
        double sMin = Double.MAX_VALUE, sMax = -Double.MAX_VALUE;
        for (double v : data) { sMin = Math.min(sMin, v); sMax = Math.max(sMax, v); }

        System.out.println("-------------------------------------------");
        System.out.printf("Global MIN temperature (critical low) : %.4f%n", result[0]);
        System.out.printf("Global MAX vibration   (critical high): %.4f%n", result[1]);
        System.out.printf("Min search time : %.3f ms%n", time[0] / 1e6);
        System.out.printf("Max search time : %.3f ms%n", time[1] / 1e6);
        System.out.printf("Total parallel time : %.3f ms%n", total / 1e6);
        System.out.println("Serial check -> min: " + String.format("%.4f", sMin)
                + " | max: " + String.format("%.4f", sMax)
                + " | Results match: " + (sMin == result[0] && sMax == result[1]));
    }
}
