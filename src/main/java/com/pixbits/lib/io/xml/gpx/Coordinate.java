package com.pixbits.lib.io.xml.gpx;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import com.pixbits.lib.lang.Point3D;
import com.pixbits.lib.util.PixMath;

public class Coordinate
{
  private double latitude;
  private double longitude;
  private double altitude;
  
  public Coordinate(Coordinate other)
  {
    this.latitude = other.latitude;
    this.longitude = other.longitude;
    this.altitude = other.altitude;
  }
  
  public Coordinate(double lat, double lng)
  {
    this(lat, lng, Double.NaN);
  }
  
  public Coordinate(double lat, double lng, double alt)
  {
    this.latitude = lat;
    this.longitude = lng;
    this.altitude = alt;
  }
  
  public void setLat(double lat) { this.latitude = lat; }
  public void setLng(double lng) { this.longitude = lng; }
  public void setAlt(double alt) { this.altitude = alt; }
    
  public boolean isValid() { return true; }
  public boolean isUnknown() { return Double.isNaN(latitude); }
  
  public double lat() { return latitude; }
  public double lng() { return longitude; }
  public double alt() { return altitude; }
  
  private static final double EARTH_RADIUS = 6371e3;
  
  public double distance(Coordinate other)
  {
    return haversineDistance(other);
  }
  
  private double haversineDistance(Coordinate other)
  {
    double fi1 = Math.toRadians(latitude);
    double fi2 = Math.toRadians(other.latitude);
    
    double deltafi = Math.toRadians(other.latitude - latitude);
    double deltalambda = Math.toRadians(other.longitude - longitude);
    
    double a = Math.sin(deltafi / 2) * Math.sin(deltafi / 2) +
        Math.cos(fi1) * Math.cos(fi2) *
        Math.sin(deltalambda / 2) * Math.sin(deltalambda / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
    return (EARTH_RADIUS * c) / 1000.0;
  }
  
  private double cosineDistance(Coordinate other)
  {
    double fi1 = Math.toRadians(latitude);
    double fi2 = Math.toRadians(other.latitude);
    
    double deltalambda = Math.toRadians(other.longitude - longitude);
    
    return (Math.acos(Math.sin(fi1)*Math.sin(fi2) + Math.cos(fi1)*Math.cos(fi2) * Math.cos(deltalambda)) * EARTH_RADIUS) / 1000.0;
  }

  
  @Override
  public boolean equals(Object o)
  {
    if (o instanceof Coordinate)
    {
      
      Coordinate c = (Coordinate)o;     
      
      if (isUnknown() && isUnknown())
        return true;
      else if (isUnknown() || isUnknown())
        return false;
      else
        return PixMath.areEquivalent(c.latitude, latitude) && 
          PixMath.areEquivalent(c.longitude, longitude);
    }
    else
      return false;
  }
  
  @Override public int hashCode()
  {
    return Objects.hash(latitude, longitude);
  }
  
  @Override
  public String toString()
  {
    return String.format("{ %2.4f, %2.4f }", latitude, longitude);
  }
  
  public static <T extends Coordinate> Coordinate computeCenterOfGravity(Iterable<T> coords)
  {
    final AtomicInteger count = new AtomicInteger();
    Point3D average = StreamSupport.stream(coords.spliterator(), false)
      .map(c -> new Point3D(Math.toRadians(c.lat()), Math.toRadians(c.lng())))
      .map(p -> new Point3D(Math.cos(p.x)*Math.cos(p.y), Math.cos(p.x)*Math.sin(p.y), Math.sin(p.x)))
      .map(p -> { count.incrementAndGet(); return p; })
      .reduce((p1, p2) -> p1.sum(p2)).get();
      
    average = average.divide(count.get());
    
    double lon = Math.atan2(average.y, average.x);
    double hyp = Math.sqrt((average.x*average.x) + (average.y*average.y));
    double lat = Math.atan2(average.z, hyp);
    
    lat = Math.toDegrees(lat);
    lon = Math.toDegrees(lon);
    
    return new Coordinate(lat, lon);
  }
  
  public static Coordinate ofUnknown() { return new Coordinate(Double.NaN, Double.NaN); }
}
