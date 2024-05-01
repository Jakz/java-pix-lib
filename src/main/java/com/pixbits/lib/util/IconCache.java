package com.pixbits.lib.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.ImageIcon;

import com.pixbits.lib.lang.Rect;

public class IconCache<T> implements Function<T, ImageIcon>
{
  private final Map<T, ImageIcon> cache;
  private final Function<T, ImageIcon> builder;
  private final Set<T> asyncSet;
  
  public IconCache(Function<T, ImageIcon> builder)
  {
    this.cache = new HashMap<>();
    this.builder = builder;
    this.asyncSet = new HashSet<>();
  }
  
  public ImageIcon get(T key)
  {
    return cache.computeIfAbsent(key, k -> builder.apply(k));
  }
  
  public ImageIcon asyncGet(T key, Runnable callback)
  {
    ImageIcon icon = cache.get(key);
    
    if (icon != null)
      return icon;
    else
    {
      synchronized (this)
      {
        if (!asyncSet.contains(key))
        {
          asyncSet.add(key);
          new Thread() {
            @Override
            public void run()
            {
              cache.put(key, builder.apply(key));
              callback.run();
              asyncSet.remove(key);
            }
          }.start();
        }
      }
      
      return null;
    }
  }
  
  @Override public ImageIcon apply(T key) { return get(key); }
  
  public void flush(T key) { cache.remove(key); }
  public void flushAll() { cache.clear(); }
  
  public static <T> IconCache<T> of(final BufferedImage image, BiFunction<BufferedImage, T, ImageIcon> builder)
  {
    Objects.requireNonNull(image);
    return new IconCache<>(k -> builder.apply(image, k));
  }
  
  public static <T> IconCache<T> of(Function<T, ImageIcon> builder)
  {
    return new IconCache<>(k -> builder.apply(k));
  }
  
  public static IconCache<Rect> ofRect(final BufferedImage image)
  {
    Objects.requireNonNull(image);
    return new IconCache<>(r -> r != null && r.w > 0 && r.h > 0 ? new ImageIcon(image.getSubimage(r.x, r.y, r.w, r.h)) : null);
  }
  
  public static IconCache<Rectangle> ofRectangle(final BufferedImage image)
  {
    Objects.requireNonNull(image);
    return new IconCache<>(r -> r != null ? new ImageIcon(image.getSubimage(r.x, r.y, r.width, r.height)) : null);
  }
}
