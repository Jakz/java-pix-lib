package com.pixbits.lib.json;

/**
 * Interface to provide a specific <code>String</code> representation of an enum field. Used to override
 * defautl behavior which utilizes the embedded enum name for serialization.
 * @author Jack
 *
 * @param <T>
 */
public interface JsonnableEnum<T extends JsonnableEnum<T>>
{
  String toJson();
}
