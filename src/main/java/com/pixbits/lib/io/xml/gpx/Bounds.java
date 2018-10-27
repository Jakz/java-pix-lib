package com.pixbits.lib.io.xml.gpx;

public class Bounds
{
  private Coordinate ne;
  private Coordinate sw;
  
  public Bounds()
  {
    
  }
  
  public Bounds(Iterable<Coordinate> coords)
  {
    coords.forEach(c -> updateBound(c));
  }
 
  public void updateBound(Iterable<Coordinate> coords)
  {
    coords.forEach(c -> updateBound(c));
  }
  
  public void updateBound(Coordinate coord)
  {
    if (ne == null)
    {
      ne = new Coordinate(coord);
      sw = new Coordinate(coord);
      return;
    }
    
    double lat = coord.lat(), lng = coord.lng();
    
    sw.setLat(Math.min(sw.lat(), lat));
    sw.setLng(Math.min(sw.lng(), lng));
    
    ne.setLat(Math.max(ne.lat(), lat));
    ne.setLng(Math.max(ne.lng(), lng));
  }
  
  public Coordinate ne() { return ne; }
  public Coordinate sw() { return sw; }
}
