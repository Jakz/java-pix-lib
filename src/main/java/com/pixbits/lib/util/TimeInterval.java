package com.pixbits.lib.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class TimeInterval
{
  private final int years;
  private final int months;
  private final int days;
  private final int hours;
  private final int minutes;
  private final int seconds;
  
  private TimeInterval(int years, int months, int days, int hours, int minutes, int seconds)
  {
    this.years = years;
    this.months = months;
    this.days = days;
    this.hours = hours;
    this.minutes = minutes;
    this.seconds = seconds;
  }
  
  public static TimeInterval of(int years, int months, int days, int hours, int minutes, int seconds)
  {
    return new TimeInterval(years, months, days, hours, minutes, seconds);
  }
  
  public static TimeInterval zero() { return TimeInterval.of(0, 0, 0, 0, 0, 0); }
  
  public static TimeInterval sum(TimeInterval i, TimeInterval o)
  {
    int y = i.years + o.years;
    int M = i.months + o.months;
    int d = i.days + o.days;
    int h = i.hours + o.hours;
    int m = i.minutes + o.minutes;
    int s = i.seconds + o.seconds;
     
    m += s / 60;
    s %= 60;
    
    h += m / 60;
    m %= 60;
    
    d += h / 24;
    h %= 24;
    
    M += d / 30;
    d %= 30;
    
    y += M / 12;
    M %= 12;
    
    return TimeInterval.of(y, M, d, h, m, s);
  }
  
  public TimeInterval add(TimeInterval o)
  {
    return TimeInterval.sum(this, o);
  }
    
  public double asMinutes()
  {
    // TODO: missing day months and years for now
    return 
        seconds / 60.0 +
        minutes +
        hours * 60.0
    ;
  }
  
  public int years() { return years; }
  public int months() { return months; }
  public int days() { return days; }
  public int hours() { return hours; }
  public int minutes() { return minutes; }
  public int seconds() { return seconds; }

  private static final ChronoUnit units[] = {
    ChronoUnit.YEARS,
    ChronoUnit.MONTHS,
    ChronoUnit.DAYS,
    ChronoUnit.HOURS,
    ChronoUnit.MINUTES,
    ChronoUnit.SECONDS
  };
  
  public static TimeInterval of(Temporal start, Temporal end)
  {
    LocalDateTime temp = LocalDateTime.from(start);
    
    int[] result = new int[units.length];
    for (int i = 0; i < units.length; ++i)
    {
      result[i] = (int) temp.until(end, units[i]);
      temp = temp.plus(result[i], units[i]);
    }

    return TimeInterval.of(result[0], result[1], result[2], result[3], result[4], result[5]);
  }
}
