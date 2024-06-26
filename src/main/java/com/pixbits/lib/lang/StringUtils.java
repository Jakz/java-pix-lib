package com.pixbits.lib.lang;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Locale;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public abstract class StringUtils
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
      return String.format(Locale.US, "%.1f%s%sB", bytes / Math.pow(unit, exp), includeSpace ? " " : "", pre);
  }
  
  public static int levenshteinDistance(String x, String y) {
    int[][] dp = new int[x.length() + 1][y.length() + 1];

    for (int i = 0; i <= x.length(); i++) {
        for (int j = 0; j <= y.length(); j++) {
            if (i == 0) {
                dp[i][j] = j;
            }
            else if (j == 0) {
                dp[i][j] = i;
            }
            else {
                dp[i][j] = Arrays.stream(new int[] {
                  dp[i - 1][j - 1] + (x.charAt(i - 1) == y.charAt(j - 1) ? 0 : 1), 
                  dp[i - 1][j] + 1, 
                  dp[i][j - 1] + 1 }).min().orElse(Integer.MAX_VALUE);
            }
        }
    }

    return dp[x.length()][y.length()];
}
  
  public static String toPercent(float v, int digits)
  {
    if (digits == 0)
      return String.format("%d", (int)(v*100));
    else
      return String.format(Locale.US, "%1."+digits+"f", v*100);
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
  
  public static String camelCase(String input)
  {
    String[] tks = input.split(" ");
    StringBuilder o = new StringBuilder();

    for (int i = 0; i < tks.length; ++i)
    {
      o.append(tks[i].substring(0, 1).toUpperCase()).append(tks[i].substring(1));

      if (i < tks.length - 1)
        o.append(" ");
    }

    return o.toString();
  }
}
