package com.pixbits.lib.json;

/**
 * Interface to provide a specific <code>String</code> representation of an <code>enum</code> field. Used to override
 * default behavior which utilizes the embedded <code>enum</code> name for serialization.
 * @param <T> an enum type 
 */
public interface JsonnableEnum<T extends JsonnableEnum<T>>
{
  String toJson();
}
