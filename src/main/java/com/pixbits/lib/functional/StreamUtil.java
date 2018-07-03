package com.pixbits.lib.functional;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.pixbits.lib.lang.Pair;

public class StreamUtil
{
  public static <A, B> Stream<Pair<A,B>> zip(final A[] as, final B[] bs) { return zip(Arrays.stream(as), Arrays.stream(bs)); }
  
  public static <A, B> Stream<Pair<A,B>> zip(Stream<A> as, Stream<B> bs)
  {
    Iterator<A> i1 = as.iterator();
    Iterator<B> i2 = bs.iterator();
    Iterable<Pair<A,B>> i = () -> new Iterator<Pair<A,B>>() 
    {
        public boolean hasNext() {
            return i1.hasNext() && i2.hasNext();
        }
        public Pair<A,B> next() {
            return new Pair<A,B>(i1.next(), i2.next());
        }
    };
    return StreamSupport.stream(i.spliterator(), false);
  }
  
  public static <T> Predicate<T> not(Predicate<T> t) {
    return t.negate();
  }
  
  public static class ConsecutiveSpliterator<T> implements Spliterator<List<T>>
  {
    private final Spliterator<T> wrappedSpliterator;
    private final int n;
    private final Deque<T> deque;
    private final Consumer<T> dequeConsumer;

    public ConsecutiveSpliterator(Spliterator<T> wrappedSpliterator, int n)
    {
      this.wrappedSpliterator = wrappedSpliterator;
      this.n = n;
      this.deque = new ArrayDeque<>();
      this.dequeConsumer = deque::addLast;
    }

    @Override
    public boolean tryAdvance(Consumer<? super List<T>> action)
    {
      deque.pollFirst();
      fillDeque();
      if (deque.size() == n)
      {
        List<T> list = new ArrayList<>(deque);
        action.accept(list);
        return true;
      } 
      else
        return false;
    }

    private void fillDeque() 
    {
      while (deque.size() < n && wrappedSpliterator.tryAdvance(dequeConsumer));
    }

    @Override
    public Spliterator<List<T>> trySplit() { return null; }

    @Override
    public long estimateSize() { return wrappedSpliterator.estimateSize(); }

    @Override
    public int characteristics() { return wrappedSpliterator.characteristics(); }
  }
  
  public static <E> Stream<List<E>> assemble(Stream<E> stream, int n) 
  {
    Spliterator<E> spliterator = stream.spliterator();
    Spliterator<List<E>> wrapper = new ConsecutiveSpliterator<>(spliterator, n);
    return StreamSupport.stream(wrapper, false);
  }
  
}
