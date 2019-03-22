package com.pixbits.lib.functional;

@FunctionalInterface
public interface TetraConsumer<I, J, K, L>
{
  public void accept(I i, J j, K k, L l);
}
