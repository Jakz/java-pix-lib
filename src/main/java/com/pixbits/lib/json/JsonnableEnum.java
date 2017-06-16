package com.pixbits.lib.json;

/**
 * Interface to provide a specific <code>String</code> representation of an enum field. Used to override
 * defautl behavior which utilizes the embedded enum name for serialization.
 * @param <T> an enum type 
 */
public interface JsonnableEnum<T extends JsonnableEnum<T>>
{
  String toJson();
}
