package com.pixbits.lib.concurrent;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

import com.pixbits.lib.concurrent.OperationDetails;
import com.pixbits.lib.functional.TriConsumer;

public abstract class BackgroundWorker<E, T extends OperationDetails> extends SwingWorker<Void, Integer>
{
  protected final List<E> data;
  protected final T operation;
  
  protected final Consumer<Boolean> onComplete;
  protected final Consumer<BackgroundWorker<?,?>> onStart;
  protected final TriConsumer<BackgroundWorker<?,?>, Integer, Integer> onProgressed;
  
  public String getTitle() { return operation.getTitle(); }
  public String getProgressText() { return operation.getProgressText(); }
  
  /*protected BackgroundWorker(T operation, Consumer<Boolean> callback)
  {
    this(new ArrayList<E>(), operation, callback);
  }*/
  
  public BackgroundWorker(List<E> data, T operation, Consumer<BackgroundWorker<?,?>> onStart, Consumer<Boolean> onComplete, TriConsumer<BackgroundWorker<?,?>, Integer, Integer> onProgressed)
  {
    this.data = data;
    this.operation = operation;
    this.onStart = onStart;
    this.onComplete = onComplete;
    this.onProgressed = onProgressed;
  }
  
  //protected void add(E item) { data.add(item); }

  @Override
  public Void doInBackground()
  {
    onStart.accept(this);
    
    for (int i = 0; i < data.size(); ++i)
    {
      int progress = (int)((((float)i)/data.size())*100);
      setProgress(progress);

      E rom = data.get(i); 
      execute(rom);

      publish(i);
    }

    return null;
  }
  
  @Override
  public void process(List<Integer> v)
  {
    onProgressed.accept(this, v.get(v.size()-1), data.size());
  }
  
  @Override
  public void done()
  {
    try
    {
      get();
      onComplete.accept(true);
    }
    catch (ExecutionException e)
    {
      Throwable cause = e.getCause();
      cause.printStackTrace();
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
  
  public abstract void execute(E element);
}
