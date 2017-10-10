package com.pixbits.lib.functional;

import java.util.Arrays;
import java.util.Iterator;
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
}
