package com.pixbits.lib.ui.color;

import java.awt.Color;
import java.util.Random;
import java.util.function.Function;

public class LambdaColorGenerator implements ColorGenerator
{
  final private Random r = new Random();
  final private Function<Float, Color> supplier;
  
  public LambdaColorGenerator(Function<Float, Color> supplier)
  {
    this.supplier = supplier;
  }
  
  
  @Override
  public Color getColor()
  {
    return supplier.apply(r.nextFloat());
  }
  
}
