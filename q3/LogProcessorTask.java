public class LogProcessorTask implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("[LOG] Iteration " + i + " processed by "
                    + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Log thread interrupted");
            }
        }
    }
}

// Author: Syed Ali Haider (247172)
