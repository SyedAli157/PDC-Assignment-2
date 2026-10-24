public class TelemetryThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            int cpu = 40 + (i * 7) % 30;
            int mem = 55 + (i * 5) % 25;
            System.out.println("[TELEMETRY] Update " + i + " | CPU: " + cpu
                    + "% | Memory: " + mem + "% | Thread: " + getName());
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                System.out.println("Telemetry thread interrupted");
            }
        }
    }
}

// Author: Syed Ali Haider (247172)

// Author: Syed Ali Haider (247172)
