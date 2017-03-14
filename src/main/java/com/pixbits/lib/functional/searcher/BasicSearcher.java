package com.pixbits.lib.functional.searcher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.pixbits.lib.parser.SimpleParser;

import jack.rm.data.rom.Rom;

public class BasicSearcher<T> extends SearchParser<T>
{
  final private SimpleParser parser;
  private SearchPredicate<T> fallbackPredicate;
  
  BasicSearcher()
  {
    parser = new SimpleParser();
    parser.addWhiteSpace(' ');
    parser.addQuote('\"');
  }
  
  public BasicSearcher(SearchPredicate<T> fallbackPredicate)
  {
    this();
    this.fallbackPredicate = fallbackPredicate;
  }
  
  @Override
  public Function<List<SearchPredicate<T>>, Predicate<T>> parse(String text)
  {
    Function<List<SearchPredicate<T>>, Predicate<T>> lambda = predicates -> {
      Predicate<T> predicate = p -> true;
      
      List<String> tokens = new ArrayList<>();
      Consumer<String> callback = s -> tokens.add(s); 
    
      parser.setCallback(callback);
      parser.reset(new java.io.ByteArrayInputStream(text.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
      
      try {
        parser.parse();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      
      for (String tk : tokens)
      {
        final String token;
        boolean negated = false;
        
        if (tk.startsWith("!"))
        {
          negated = true;
          token = tk.substring(1);
        }
        else
          token = tk;
        
        Predicate<T> cpredicate = buildSinglePredicate(predicates, token);
        
        if (cpredicate != null)
        {
          if (negated)
            predicate = predicate.and(cpredicate.negate());
          else
            predicate = predicate.and(cpredicate);
        }
        else if (fallbackPredicate != null)
        {            
          Predicate<T> fallback = fallbackPredicate.buildPredicate(token);
          
          if (negated) predicate = predicate.and(fallback.negate());
          else predicate = predicate.and(fallback);
        }
      }
    
      return predicate;
    };
    
    return lambda;
  }
  
}