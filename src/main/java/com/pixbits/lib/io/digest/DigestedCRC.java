package com.pixbits.lib.io.digest;

/**
 * This interface is meant to annotate a type that could provide a computed CRC32
 * value so that it's possible to use the cached value by the <code>Digester</code>
 * instead that computing it.
 * @author Jack
 */
public interface DigestedCRC
{
  long getCRC32();
}
