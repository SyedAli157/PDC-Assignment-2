import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinPortfolioApp {
    public static void main(String[] args) {
        int size = 10000;
        double riskCap = 800.0;
        double[] assets = new double[size];
        Random rand = new Random(42);
        for (int i = 0; i < size; i++) {
            assets[i] = 100 + rand.nextDouble() * 900; // values between 100 and 1000
        }
        double[] copy = assets.clone();

        System.out.println("Portfolio size      : " + size + " assets");
        System.out.println("Risk cap            : " + riskCap);

        // Serial calculation
        long s1 = System.nanoTime();
        double serialSum = 0;
        for (double v : assets) serialSum += v;
        long s2 = System.nanoTime();

        // Parallel calculation
        ForkJoinPool pool = new ForkJoinPool();
        long p1 = System.nanoTime();
        double parallelSum = pool.invoke(new ParallelRiskCalculator(assets, 0, size));
        long p2 = System.nanoTime();

        System.out.printf("Serial total risk   : %.2f  (time: %.4f ms)%n", serialSum, (s2 - s1) / 1e6);
        System.out.printf("Parallel total risk : %.2f  (time: %.4f ms)%n", parallelSum, (p2 - p1) / 1e6);
        System.out.println("Available cores     : " + Runtime.getRuntime().availableProcessors());

        // Stress test (RecursiveAction)
        long t1 = System.nanoTime();
        pool.invoke(new PortfolioThresholdTask(assets, 0, size, riskCap));
        long t2 = System.nanoTime();
        double afterSum = pool.invoke(new ParallelRiskCalculator(assets, 0, size));

        int penalised = 0;
        for (int i = 0; i < size; i++) if (copy[i] > riskCap) penalised++;

        System.out.printf("Stress-test time    : %.4f ms%n", (t2 - t1) / 1e6);
        System.out.println("Assets penalised(5%): " + penalised);
        System.out.printf("Total risk after 5%% penalty: %.2f%n", afterSum);
        System.out.printf("Risk reduced by     : %.2f%n", parallelSum - afterSum);
        pool.shutdown();
    }
}

// Author: Syed Ali Haider (247172)
