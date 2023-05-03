package PA;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SumCalculator {
    private final int number;
    private int sum;

    public SumCalculator(int number) {
        this.number = number;
    }

    public void calculateSum() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        int range = number / 5;
        int remainder = number % 5;

        int start = 1;
        int end = start + range - 1;

        for (int i = 0; i < 5; i++) {
            if (remainder > 0) {
                end++;
                remainder--;
            }
            executorService.execute(new SumWorker(start, end));
            start = end + 1;
            end = start + range - 1;
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("The sum of all numbers from 1 to " + number + " is " + sum);
    }

    private class SumWorker implements Runnable {
        private final int start;
        private final int end;

        public SumWorker(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            int partSum = 0;
            for (int i = start; i <= end; i++) {
                partSum += i;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (SumCalculator.this) {
                sum += partSum;
            }
        }
    }
}

