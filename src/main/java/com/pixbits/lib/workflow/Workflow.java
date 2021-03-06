package com.pixbits.lib.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Workflow<T extends WorkflowData>
{
  private Fetcher<T> fetcher;
  private Dumper<T> dumper;
  private List<Mutuator<T>> mutuators;
  
  public Workflow(Fetcher<T> fetcher, Dumper<T> dumper)
  {
    this.fetcher = fetcher;
    this.dumper = dumper;
    this.mutuators = new ArrayList<>();
  }
  
  public void execute()
  {
    Stream<T> stream = fetcher.get();
    Optional<Mutuator<T>> mutuator = mutuators.stream().reduce( (m1, m2) -> (Mutuator<T>)m1.andThen(m2) );
    
    if (mutuator.isPresent())
      dumper.accept(stream.map(mutuator.get()));
    else
      dumper.accept(stream);
  }
  
  public void addBenchmarkedStep(Mutuator<T> mutuator)
  {
    mutuators.add(new BenchmarkMutuator<T>(mutuator));
  }
  
  public void addStep(Mutuator<T> mutuator)
  {
    mutuators.add(mutuator);
  }
}
