public class ParallelMaxSearch extends SensorSearchEngine {
    private int workers;

    public ParallelMaxSearch(double[] data, int startIdx, int endIdx, int workers) {
        super(data, startIdx, endIdx);
        this.workers = workers;
    }

    @Override
    public double processGrid() throws InterruptedException {
        double[] localMax = new double[workers];
        Thread[] threads = new Thread[workers];
        int chunk = (endIdx - startIdx) / workers;

        for (int w = 0; w < workers; w++) {
            final int id = w;
            final int from = startIdx + w * chunk;
            final int to = (w == workers - 1) ? endIdx : from + chunk;
            threads[w] = new Thread(() -> {
                double max = -Double.MAX_VALUE;
                for (int i = from; i < to; i++) {
                    if (sensorData[i] > max) max = sensorData[i];
                }
                localMax[id] = max;
            }, "MaxWorker-" + (w + 1));
            threads[w].start();
        }
        double globalMax = -Double.MAX_VALUE;
        for (int w = 0; w < workers; w++) {
            threads[w].join();
            System.out.printf("   %s local max = %.4f%n", threads[w].getName(), localMax[w]);
            globalMax = Math.max(globalMax, localMax[w]);
        }
        return globalMax;
    }
}

// Author: Syed Ali Haider (247172)
