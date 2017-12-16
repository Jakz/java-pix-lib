package com.pixbits.lib.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.ImageIcon;

import com.pixbits.lib.lang.Rect;

public class IconCache<T> implements Function<T, ImageIcon>
{
  private final Map<T, ImageIcon> cache;
  private final Function<T, ImageIcon> builder;
  
  public IconCache(Function<T, ImageIcon> builder)
  {
    this.cache = new HashMap<>();
    this.builder = builder;
  }
  
  private ImageIcon generate(T key)
  {
    ImageIcon icon = builder.apply(key);
    cache.put(key,  icon);
    return icon;
  }
  
  public ImageIcon get(T key)
  {
    return cache.computeIfAbsent(key, k -> generate(k));
  }
  
  @Override public ImageIcon apply(T key) { return get(key); }
  
  public void flush(T key) { cache.remove(key); }
  public void flushAll() { cache.clear(); }
  
  public static <T> IconCache<T> of(final BufferedImage image, BiFunction<BufferedImage, T, ImageIcon> builder)
  {
    Objects.requireNonNull(image);
    return new IconCache<>(k -> builder.apply(image, k));
  }
  
  public static IconCache<Rect> ofRect(final BufferedImage image)
  {
    Objects.requireNonNull(image);
    return new IconCache<>(r ->  r != null ? new ImageIcon(image.getSubimage(r.x, r.y, r.w, r.h)) : null);
  }
  
  public static IconCache<Rectangle> ofRectangle(final BufferedImage image)
  {
    Objects.requireNonNull(image);
    return new IconCache<>(r -> r != null ? new ImageIcon(image.getSubimage(r.x, r.y, r.width, r.height)) : null);
  }
}
