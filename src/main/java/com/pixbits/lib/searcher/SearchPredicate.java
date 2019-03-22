package com.pixbits.lib.searcher;

import java.util.Arrays;
import java.util.function.Predicate;

@FunctionalInterface
public abstract interface SearchPredicate<T>
{
  /*abstract String getName();
  abstract String getDescription();
  abstract String getExample();*/
  abstract Predicate<T> buildPredicate(String token);
  
  default String[] splitWithDelimiter(String token, String delim)
  {
    String[] tokens = token.split(delim);
    
    if (tokens.length == 2)
    {
      if (tokens[1].startsWith("\""))
        tokens[1] = tokens[1].substring(1);
      if (tokens[1].endsWith("\""))
        tokens[1] = tokens[1].substring(0, tokens[1].length()-1);
      
      return tokens;
    }
    
    return null;
  }
  
  default boolean isSearchArg(String[] tokens, String... vals)
  {
    boolean firstMatch = tokens != null && tokens[0].equals(vals[0]);  
    return firstMatch && Arrays.stream(vals, 1, vals.length).anyMatch( v -> v.equals(tokens[1]));
  } 
}
