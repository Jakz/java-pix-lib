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
