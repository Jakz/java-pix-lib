package com.pixbits.lib.concurrent;

import java.util.function.Function;

@FunctionalInterface
public interface Operation<T,V> extends Function<T,V>
{

}
