package com.pixbits.lib.lang;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class StringUtils
{
  private static final HexBinaryAdapter hexConverter = new HexBinaryAdapter();

  public static String paddedString(char c, int length)
  {
    char[] data = new char[length];
    Arrays.fill(data, c);
    return new String(data);
  }
  
  public static String humanReadableByteCount(long bytes)
  {
    return humanReadableByteCount(bytes, true, true);
  }
  
  public static String humanReadableByteCount(long bytes, boolean si, boolean includeSpace)
  {
      int unit = si ? 1000 : 1024;
      if (bytes < unit) return bytes + " B";
      int exp = (int) (Math.log(bytes) / Math.log(unit));
      String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
      return String.format("%.1f%s%sB", bytes / Math.pow(unit, exp), includeSpace ? " " : "", pre);
  }
  
  public static String toPercent(float v, int digits)
  {
    if (digits == 0)
      return String.format("%d", (int)(v*100));
    else
      return String.format("%1."+digits+"f", v*100);
  }
  
  public static String toHexString(byte[] bytes)
  {
    return hexConverter.marshal(bytes);
  }
  
  public static String toHexString(int value)
  {
    byte[] buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
    return hexConverter.marshal(buffer);
  }
  
  public static byte[] fromHexString(String value)
  {
    return hexConverter.unmarshal(value);
  }
  
  public static boolean isEmpty(String string)
  {
    return string == null || string.isEmpty();
  }
}
