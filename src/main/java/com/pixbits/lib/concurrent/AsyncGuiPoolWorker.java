package com.pixbits.lib.concurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import static java.util.stream.Collectors.toList;

import javax.swing.SwingUtilities;

import com.pixbits.lib.functional.StreamException;

public class AsyncGuiPoolWorker<T,V>
{
  private final Operation<T,V> operation;
  private final BiConsumer<Long,Float> guiOperation;
  
  private ThreadPoolExecutor executor;
  private Thread guiThread;
  
  private boolean cancelled;
  private int threadCount;
  
  private long total;
  private long previousCurrent;
  private AtomicLong current;
  
  private CompletableFuture<?>[] results;
  
  private boolean running;
  
  public AsyncGuiPoolWorker(Operation<T,V> operation, BiConsumer<Long,Float> guiOperation, int threadCount)
  {
    this.operation = operation;
    this.guiOperation = guiOperation;
    this.threadCount = threadCount;
  }
  
  public AsyncGuiPoolWorker(Operation<T,V> operation, BiConsumer<Long,Float> guiOperation)
  {
    this(operation, guiOperation, Runtime.getRuntime().availableProcessors());
  }
  
  public void compute(Collection<T> data, Consumer<V> onResult)
  {
    compute(data, onResult, () -> {});
  }
  
  public void compute(Collection<T> data, Consumer<V> onResult, Runnable onComplete)
  {
    executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);
    running = true;
    current = new AtomicLong(0);
    previousCurrent = 0;
    total = data.size();
    
    guiThread = new Thread(() -> {
      while (running)
      {
        try
        {
          if (previousCurrent != current.get())
          {
            previousCurrent = current.get();
            final float progress = current.get() / (float)total;
            SwingUtilities.invokeLater(() -> guiOperation.accept(previousCurrent, progress));
          }
          
          Thread.sleep(50);
        } 
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
      }
    });
    
    guiThread.start();
    
    results = data.stream().map(t ->
      CompletableFuture.supplyAsync(() -> t, executor).thenApplyAsync(tt -> { 
        try
        {
          V v = operation.apply(tt);
        current.incrementAndGet();
        return v;
        }
        catch (Exception e)
        {
          e.printStackTrace();
          return null;
        }
      }).thenAccept(onResult)
    ).toArray(i -> new CompletableFuture<?>[i]);
    
    executor.shutdown();
    
    CompletableFuture.allOf(results).thenRun(() -> {
      running = false;
      onComplete.run();
      this.executor = null;
    });
  }
  
  public void computeAll(Collection<T> data, Consumer<List<V>> onAllComplete)
  {
    List<V> results = Collections.synchronizedList(new ArrayList<>());
    
    Runnable onComplete = () -> {
      onAllComplete.accept(results);
    };
    
    compute(data, v -> results.add(v), onComplete);
  }
  
  public void cancel()
  {
    Arrays.stream(results).forEach(c -> c.cancel(false));
    running = false;
    cancelled = true;
  }
}
