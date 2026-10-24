public class ParallelMinSearch extends SensorSearchEngine {
    private int workers;

    public ParallelMinSearch(double[] data, int startIdx, int endIdx, int workers) {
        super(data, startIdx, endIdx);
        this.workers = workers;
    }

    @Override
    public double processGrid() throws InterruptedException {
        double[] localMin = new double[workers];
        Thread[] threads = new Thread[workers];
        int chunk = (endIdx - startIdx) / workers;

        for (int w = 0; w < workers; w++) {
            final int id = w;
            final int from = startIdx + w * chunk;
            final int to = (w == workers - 1) ? endIdx : from + chunk;
            threads[w] = new Thread(() -> {
                double min = Double.MAX_VALUE;
                for (int i = from; i < to; i++) {
                    if (sensorData[i] < min) min = sensorData[i];
                }
                localMin[id] = min;
            }, "MinWorker-" + (w + 1));
            threads[w].start();
        }
        double globalMin = Double.MAX_VALUE;
        for (int w = 0; w < workers; w++) {
            threads[w].join();
            System.out.printf("   %s local min = %.4f%n", threads[w].getName(), localMin[w]);
            globalMin = Math.min(globalMin, localMin[w]);
        }
        return globalMin;
    }
}
