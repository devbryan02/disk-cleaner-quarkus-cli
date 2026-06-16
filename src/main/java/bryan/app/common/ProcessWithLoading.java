package bryan.app.common;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class ProcessWithLoading {

    private static final long MINIMUM_DISPLAY_MS = 2500;
    private static final long SPINNER_INTERVAL_MS = 120;
    private static final String[] SPINNER_FRAMES = {"|", "/", "-", "\\"};

    public void execute(String taskMessage, Runnable workerTask) {
        ExecutorService taskExecutor = Executors.newSingleThreadExecutor();
        ScheduledExecutorService spinnerExecutor = Executors.newSingleThreadScheduledExecutor();

        AtomicInteger frameIndex = new AtomicInteger(0);

        try {
            long startTime = System.currentTimeMillis();

            Future<?> futureTask = taskExecutor.submit(workerTask);

            spinnerExecutor.scheduleAtFixedRate(() -> {
                String frame = SPINNER_FRAMES[frameIndex.getAndIncrement() % SPINNER_FRAMES.length];
                System.out.printf("\r %s %s", taskMessage, frame);
                System.out.flush();
            }, 0, SPINNER_INTERVAL_MS, TimeUnit.MILLISECONDS);

            long taskDuration = waitForTask(futureTask, startTime);
            long remaining = MINIMUM_DISPLAY_MS - taskDuration;
            if (remaining > 0) {
                Thread.sleep(remaining);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            spinnerExecutor.shutdownNow();
            taskExecutor.shutdown();
            System.out.printf("\r%s\r", " ".repeat(taskMessage.length() + 5));
            System.out.flush();
        }
    }

    private long waitForTask(Future<?> futureTask, long startTime) {
        try {
            futureTask.get();
        } catch (ExecutionException e) {
            System.err.println("\nError en tarea: " + e.getCause().getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return System.currentTimeMillis() - startTime;
    }
}
