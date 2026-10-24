public class CacheLocalityBenchmark {
    static final int N = 2000;
    static double[][] matrix = new double[N][N];

    static double traverseRowMajor() {
        double sum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    static double traverseColumnMajor() {
        double sum = 0;
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < N; i++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                matrix[i][j] = (i + j) % 10;

        // warm-up so the JIT compiler does not affect the results
        traverseRowMajor();
        traverseColumnMajor();

        int runs = 10;
        double totalRow = 0, totalCol = 0;
        System.out.println("Matrix size: " + N + " x " + N + " | Runs: " + runs);
        System.out.println("Run | Row-Major (ms) | Column-Major (ms)");
        for (int r = 1; r <= runs; r++) {
            long a = System.nanoTime();
            double s1 = traverseRowMajor();
            long b = System.nanoTime();
            double s2 = traverseColumnMajor();
            long c = System.nanoTime();
            double rowMs = (b - a) / 1e6;
            double colMs = (c - b) / 1e6;
            totalRow += rowMs;
            totalCol += colMs;
            System.out.printf("%3d | %14.3f | %17.3f%n", r, rowMs, colMs);
            if (s1 != s2) System.out.println("Sums differ!");
        }
        double avgRow = totalRow / runs;
        double avgCol = totalCol / runs;
        System.out.println("---------------------------------------------");
        System.out.printf("Average Row-Major    : %.3f ms%n", avgRow);
        System.out.printf("Average Column-Major : %.3f ms%n", avgCol);
        System.out.printf("Speedup (Column / Row): %.2fx faster with Row-Major%n", avgCol / avgRow);
    }
}
