package com.pixbits.lib.ui.color;

import java.awt.Color;

@FunctionalInterface
public interface ColorCache<T>
{

  Color get(T key);

}