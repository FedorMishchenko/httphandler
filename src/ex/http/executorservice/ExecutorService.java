package ex.http.executorservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

//pattern Listener realisation

public class ExecutorService {
    private final ThreadGroup group = new ThreadGroup("group");
    private final Collection<Thread> pool = new ArrayList<>();
    private final BlockingQueue<Callable> taskQueue;

    public ExecutorService(int threadCount, final BlockingQueue<Callable> queue) {
        this.taskQueue = queue;
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(group, () -> {
                while (true){
                    try {
                        Callable nextTask = taskQueue.take();
                        nextTask.call();
                    }catch (InterruptedException e){
                        break;
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
            thread.start();
            pool.add(thread);
        }
    }

    public <T> void submit(Callable<T> task) throws InterruptedException {
        taskQueue.put(task);
    }

    public void shutdown() {
        group.interrupt();
    }
}
